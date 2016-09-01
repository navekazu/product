package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.controller.MainControllerNotification;
import tools.dbcomparator2.entity.*;
import tools.dbcomparator2.enums.CompareType;
import tools.dbcomparator2.enums.DBParseStatus;
import tools.dbcomparator2.enums.RecordCompareStatus;
import tools.dbcomparator2.enums.TableCompareStatus;

import java.util.*;

public class DBCompareService implements DBParseNotification {
    private Logger logger = LoggerFactory.getLogger(DBCompareService.class);
    private static final int MAX_COMPARE_COUNT = 2;
    private MainControllerNotification mainControllerNotification;
    private List<DBCompareEntity> dbCompareEntityList;
    private CompareType compareType;

    // 比較結果
    private Map<String, TableCompareStatus> tableCompareStatusMap;
    private Map<String, Map<String, RecordCompareStatus>> recordCompareStatusMap;

    public DBCompareService() {
        this.mainControllerNotification = null;
        this.dbCompareEntityList = new ArrayList<>();

        this.tableCompareStatusMap = Collections.synchronizedMap(new HashMap<>());
        this.recordCompareStatusMap = Collections.synchronizedMap(new HashMap<>());
    }

    public void setCompareType(CompareType compareType) {
        this.compareType = compareType;
    }

    public boolean canRestartable() {
        // dbCompareEntityListが空なら再開不可
        if (dbCompareEntityList.size()==0) {
            return false;
        }

        // dbCompareEntityListの先頭が解析完了していないなら再開不可
        if (dbCompareEntityList.get(0).getStatus()!=DBParseStatus.SCAN_FINISHED) {
            return false;
        }

        // dbCompareEntityListの先頭が解析完了しているなら再開可
        return true;
    }

    public void setMainControllerNotification(MainControllerNotification mainControllerNotification) {
        this.mainControllerNotification = mainControllerNotification;
    }

    public void startCompare(ConnectEntity connectEntity) {
        dbCompareEntityList.clear();
        addDbCompareEntityList(connectEntity);
        startCompare();
    }

    public void restartCompare(ConnectEntity connectEntity) {
        addDbCompareEntityList(connectEntity);
        startCompare();
    }

    public void startCompare(ConnectEntity firstConnectEntity, ConnectEntity secondConnectEntity) {
        dbCompareEntityList.clear();
        addDbCompareEntityList(firstConnectEntity);
        addDbCompareEntityList(secondConnectEntity);
        startCompare();
    }

    private void addDbCompareEntityList(ConnectEntity connectEntity) {
        dbCompareEntityList.add(DBCompareEntity.builder()
                .connectEntity(connectEntity)
                .build());
    }

    private void startCompare() {
        // DBParseServiceの初期化・登録
        dbCompareEntityList.stream()
                .filter(entity -> entity.getStatus()!= DBParseStatus.SCAN_FINISHED)
                .forEach(entity -> entity.setDbParseService(new DBParseService(this, entity)));

        // DB接続
        dbCompareEntityList.parallelStream()
                .filter(entity -> entity.getStatus() != DBParseStatus.SCAN_FINISHED)
                .forEach(entity -> entity.getDbParseService().connect());

        // テーブル一覧取得
        dbCompareEntityList.parallelStream()
                .filter(entity -> entity.getStatus() != DBParseStatus.SCAN_FINISHED)
                .forEach(entity -> entity.getDbParseService().parseTableList());

        // 解析開始
        dbCompareEntityList.parallelStream()
                .filter(entity -> entity.getStatus() != DBParseStatus.SCAN_FINISHED)
                .forEach(
                        entity -> entity.getTableCompareEntityMap().keySet().parallelStream()
                                .forEach(
                                    tableName -> entity.getDbParseService().parseTableData(tableName)
                                )
                );

        // DB切断
        dbCompareEntityList.parallelStream()
                .filter(entity -> entity.getStatus() != DBParseStatus.SCAN_FINISHED)
                .forEach(entity -> entity.getDbParseService().disconnect());

        // ステータス更新
        dbCompareEntityList.stream()
                .forEach(entity -> entity.setStatus(DBParseStatus.SCAN_FINISHED));
    }

    @Override
    public void parsedTableList(ConnectEntity connectEntity, List<String> tableList) {
        dbCompareEntityList.stream()
                .filter(entity -> entity.getConnectEntity() == connectEntity)
                .forEach(entity ->
                    tableList.stream().forEach(tableName -> {
                        TableCompareEntity tableCompareEntity = TableCompareEntity.builder()
                                .tableName(tableName)
                                .build();
                        entity.addTableCompareEntity(tableCompareEntity);
                        putTableCompareStatusMap(tableName);
                    })
                );
    }

    @Override
    public void countedTableRecord(ConnectEntity connectEntity, String tableName, int recordCount) {
        dbCompareEntityList.stream()
                .filter(entity -> entity.getConnectEntity() == connectEntity)
                .forEach(entity -> {
                    entity.getTableCompareEntity(tableName).setRecordCount(recordCount);

                    // 画面に通知
                    if (mainControllerNotification!=null) {
                        mainControllerNotification.initProgress(tableName, recordCount);
                    }
                });
    }

    @Override
    public void parsedPrimaryKey(ConnectEntity connectEntity, String tableName, List<String> primaryKeyColumnList) {
        dbCompareEntityList.stream()
                .filter(entity -> entity.getConnectEntity() == connectEntity)
                .forEach(entity -> entity.getTableCompareEntity(tableName).setPrimaryKeyColumnList(primaryKeyColumnList));
    }

    @Override
    public void parsedTableRecord(ConnectEntity connectEntity, String tableName, int rowNumber, RecordHashEntity tableRecordEntity) {
        dbCompareEntityList.stream()
                .filter(entity -> entity.getConnectEntity() == connectEntity)
                .forEach(entity -> {
                    entity.getTableCompareEntity(tableName).addRecordHashEntity(tableRecordEntity);
//                    putRecordCompareStatusMap();

                });
    }

    @Override
    public void fatal(ConnectEntity connectEntity, Exception exception) {

    }

    @Override
    public void error(ConnectEntity connectEntity, Exception exception) {

    }


    private void putTableCompareStatusMap(String tableName) {
        synchronized (tableCompareStatusMap) {
            if (tableCompareStatusMap.containsKey(tableName)) {
                tableCompareStatusMap.put(tableName, TableCompareStatus.PAIR);
                return;
            }

            tableCompareStatusMap.put(tableName, TableCompareStatus.ONE_SIDE_ONLY);
            // 画面に通知
            if (mainControllerNotification!=null) {
                mainControllerNotification.addRow(tableName);
            }
        }
    }

    private void putRecordCompareStatusMap(String tableName, RecordHashEntity tableRecordEntity) {
        synchronized (recordCompareStatusMap) {
            if (recordCompareStatusMap.containsKey(tableName)) {
                if (recordCompareStatusMap.get(tableName).containsKey(tableRecordEntity.getPrimaryKeyHashValue())) {
                    recordCompareStatusMap.get(tableName).put(tableRecordEntity.getPrimaryKeyHashValue(), RecordCompareStatus.EQUALITY);
                    return;
                }
                recordCompareStatusMap.get(tableName).put(tableRecordEntity.getPrimaryKeyHashValue(), RecordCompareStatus.INEQUALITY);
                // 画面に通知
                if (mainControllerNotification!=null) {
//                    mainControllerNotification.updateProgress(tableName, rowNumber);
                }
            }
        }
    }
}

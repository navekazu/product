package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.controller.MainControllerNotification;
import tools.dbcomparator2.entity.*;
import tools.dbcomparator2.enums.CompareType;
import tools.dbcomparator2.enums.DBParseStatus;
import tools.dbcomparator2.enums.TableCompareStatus;

import java.util.*;

public class DBCompareService implements DBParseNotification {
    private Logger logger = LoggerFactory.getLogger(DBCompareService.class);
    private static final int MAX_COMPARE_COUNT = 2;
    private MainControllerNotification mainControllerNotification;
    private CompareType compareType;

    // 比較対象
    List<DBCompareEntity> dbCompareEntityList;

    // 比較結果
//    Map<String, TableCompareStatus> tableCompareStatusMap;
//    Map<String, Map<String, RecordCompareStatus>> recordCompareStatusMap;
    List<CompareTableRecord> compareTableRecordList;

    public DBCompareService() {
        this.mainControllerNotification = null;
        this.dbCompareEntityList = new ArrayList<>();
        this.compareTableRecordList = new ArrayList<>(); // NullPointerException防止のためのインスタンス

//        this.tableCompareStatusMap = Collections.synchronizedMap(new HashMap<>());
//        this.recordCompareStatusMap = Collections.synchronizedMap(new HashMap<>());
    }

    public void setCompareType(CompareType compareType) {
        this.compareType = compareType;
    }

    public void setCompareTableRecordList(List<CompareTableRecord> compareTableRecordList) {
        this.compareTableRecordList = compareTableRecordList;
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

    public void updateConnectEntity(List<ConnectEntity> entityList) {
        dbCompareEntityList.clear();
        entityList.stream().forEach(entity -> addDbCompareEntityList(entity));
    }
    public void addConnectEntity(ConnectEntity connectEntity) {
        addDbCompareEntityList(connectEntity);
    }

    private void addDbCompareEntityList(ConnectEntity connectEntity) {
        if (dbCompareEntityList.size()>=MAX_COMPARE_COUNT) {
            switch (compareType) {
                case PIVOT:
                    // 2つ目を削除
                    dbCompareEntityList.remove(1);
                    break;

                case IMMEDIATE:
                case ROTATION:
                    // 先頭を削除
                    dbCompareEntityList.remove(0);
                    break;
            }
        }
        dbCompareEntityList.add(DBCompareEntity.builder()
                .connectEntity(connectEntity)
                .build());
    }

    public void startCompare() {
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

        // この時点でまだ比較が出来ていないものは「出来ていない」のではなく「出来ない」ものなので、ステータスを更新する

        // DB切断
        dbCompareEntityList.parallelStream()
//                .filter(entity -> entity.getStatus() != DBParseStatus.SCAN_FINISHED)
                .forEach(entity -> {
                    entity.getDbParseService().disconnect();
                });

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

                        addCompareTableRecordList(tableName);
                    })
                );
    }

    void addCompareTableRecordList(String tableName) {
        synchronized (compareTableRecordList) {
            if (compareTableRecordList.parallelStream().anyMatch(compareTableRecord -> tableName.equals(compareTableRecord.getTableName()))) {
                compareTableRecordList.stream()
                        .filter(compareTableRecord -> tableName.equals(compareTableRecord.getTableName()))
                        .forEach(compareTableRecord -> compareTableRecord.setTableCompareStatus(TableCompareStatus.PAIR));
                return;
            }

            CompareTableRecord compareTableRecord = new CompareTableRecord();
            compareTableRecord.setTableName(tableName);
            compareTableRecord.setTableCompareStatus(TableCompareStatus.ONE_SIDE_ONLY);
            compareTableRecordList.add(compareTableRecord);
        }
    }

    @Override
    public void countedTableRecord(ConnectEntity connectEntity, String tableName, int recordCount) {
        dbCompareEntityList.stream()
                .filter(entity -> entity.getConnectEntity() == connectEntity)
                .forEach(entity -> {
                    entity.getTableCompareEntity(tableName).setRecordCount(recordCount);
                    updateCompareTableRecordRowCount(tableName, recordCount);
                });
    }

    void updateCompareTableRecordRowCount(String tableName, int recordCount) {
        synchronized (compareTableRecordList) {
            compareTableRecordList.stream()
                    .filter(compareTableRecord -> tableName.equals(compareTableRecord.getTableName()))
                    .forEach(compareTableRecord -> {
                        if (compareTableRecord.getRowCount() < recordCount) {
                            // 現状より大きい方をテーブルの行数とする
                            compareTableRecord.setRowCount(recordCount);
                        }
                    });
        }
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
                .forEach(entity -> entity.getTableCompareEntity(tableName).addRecordHashEntity(tableRecordEntity));
    }

    @Override
    public void fatal(ConnectEntity connectEntity, Exception exception) {

    }

    @Override
    public void error(ConnectEntity connectEntity, Exception exception) {

    }


    private void putTableCompareStatusMap(String tableName) {
/*
        synchronized (tableCompareStatusMap) {
            if (tableCompareStatusMap.containsKey(tableName)) {
                tableCompareStatusMap.put(tableName, TableCompareStatus.PAIR);
                return;
            }

            tableCompareStatusMap.put(tableName, TableCompareStatus.ONE_SIDE_ONLY);

            CompareTableRecord compareTableRecord = new CompareTableRecord();
            compareTableRecord.setTableName(tableName);
            compareTableRecordList.add(compareTableRecord);
        }
*/
    }

    private void putRecordCompareStatusMap(String tableName, RecordHashEntity tableRecordEntity) {
/*
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
*/
    }
}

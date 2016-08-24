package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.entity.TableCompareEntity;
import tools.dbcomparator2.enums.DBCompareStatus;

import java.util.ArrayList;
import java.util.List;

public class DBCompareService implements DBParseNotification {
    private Logger logger = LoggerFactory.getLogger(DBCompareService.class);
    private static final int MAX_COMPARE_COUNT = 2;
    private List<DBCompareEntity> dbCompareEntityList;

    public DBCompareService() {
        dbCompareEntityList = new ArrayList<>();
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
                .filter(entity -> entity.getStatus()!=DBCompareStatus.SCAN_FINISHED)
                .forEach(entity -> entity.setDbParseService(new DBParseService(this, entity)));

        // DB接続
        dbCompareEntityList.parallelStream()
                .filter(entity -> entity.getStatus() != DBCompareStatus.SCAN_FINISHED)
                .forEach(entity -> entity.getDbParseService().connect());

        // テーブル一覧取得
        dbCompareEntityList.parallelStream()
                .filter(entity -> entity.getStatus() != DBCompareStatus.SCAN_FINISHED)
                .forEach(entity -> entity.getDbParseService().parseTableList());

        // 解析開始
        dbCompareEntityList.parallelStream()
                .filter(entity -> entity.getStatus() != DBCompareStatus.SCAN_FINISHED)
                .forEach(
                        entity -> entity.getTableCompareEntityMap().keySet().parallelStream()
                                .forEach(
                                    tableName -> entity.getDbParseService().parseTableData(tableName)
                                )
                );

        // DB切断
        dbCompareEntityList.parallelStream()
                .filter(entity -> entity.getStatus() != DBCompareStatus.SCAN_FINISHED)
                .forEach(entity -> entity.getDbParseService().disconnect());

        // ステータス更新
        dbCompareEntityList.stream()
                .forEach(entity -> entity.setStatus(DBCompareStatus.SCAN_FINISHED));
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
                    })
                );
    }

    @Override
    public void countedTableRecord(ConnectEntity connectEntity, String tableName, int recordCount) {
        dbCompareEntityList.stream()
                .filter(entity -> entity.getConnectEntity() == connectEntity)
                .forEach(entity -> entity.getTableCompareEntity(tableName).setRecordCount(recordCount));
    }

    @Override
    public void parsedPrimaryKey(ConnectEntity connectEntity, String tableName, List<String> primaryKeyColumnList) {
        dbCompareEntityList.stream()
                .filter(entity -> entity.getConnectEntity() == connectEntity)
                .forEach(entity -> entity.getTableCompareEntity(tableName).setPrimaryKeyColumnList(primaryKeyColumnList));
    }

    @Override
    public void parsedTableRecord(ConnectEntity connectEntity, String tableName, RecordHashEntity tableRecordEntity) {
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
}

package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;
import tools.dbcomparator2.entity.TableRecordEntity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
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
        dbCompareEntityList.add(DBCompareEntity.builder().connectEntity(connectEntity).tableRecordCount(new HashMap<>()).tableRecordEntity(new HashMap<>()).build());
        startCompare();
    }

    public void startCompare(ConnectEntity firstConnectEntity, ConnectEntity secondConnectEntity) {
        dbCompareEntityList.clear();
        dbCompareEntityList.add(DBCompareEntity.builder().connectEntity(firstConnectEntity).tableRecordCount(new HashMap<>()).tableRecordEntity(new HashMap<>()).build());
        dbCompareEntityList.add(DBCompareEntity.builder().connectEntity(secondConnectEntity).tableRecordCount(new HashMap<>()).tableRecordEntity(new HashMap<>()).build());
        startCompare();
    }

    private void startCompare() {
        // DBParseServiceの初期化・登録
        dbCompareEntityList.stream().forEach(entity -> entity.setDbParseService(new DBParseService(this, entity.getConnectEntity())));

        // DB接続
        dbCompareEntityList.parallelStream().forEach(entity -> entity.getDbParseService().connect());

        // テーブル一覧取得
        dbCompareEntityList.parallelStream().forEach(entity -> entity.getDbParseService().parseTableList());

        // 解析開始
        dbCompareEntityList.parallelStream().forEach(
                entity -> entity.getTableList().parallelStream().forEach(
                        table -> entity.getDbParseService().parseTableData(table)
                )
        );

        // DB切断
        dbCompareEntityList.parallelStream().forEach(entity -> entity.getDbParseService().disconnect());
    }

    @Override
    public void parsedTableList(ConnectEntity connectEntity, List<String> tableList) {
        dbCompareEntityList.stream().forEach(entity -> {
            if (entity.getConnectEntity()==connectEntity) {
                entity.setTableList(tableList);
            }
        });
    }

    @Override
    public void countedTableRecord(ConnectEntity connectEntity, String tableName, int recordCount) {
        dbCompareEntityList.stream().forEach(entity -> {
            if (entity.getConnectEntity()==connectEntity) {
                entity.putTableRecordCount(tableName, recordCount);
            }
        });
    }

    @Override
    public void parsedTableRecord(ConnectEntity connectEntity, String tableName, TableRecordEntity tableRecordEntity) {
        dbCompareEntityList.stream().forEach(entity -> {
            if (entity.getConnectEntity()==connectEntity) {
                entity.putTableRecordEntity(tableName, tableRecordEntity);
            }
        });
    }

    @Override
    public void fatal(ConnectEntity connectEntity, Exception exception) {

    }

    @Override
    public void error(ConnectEntity connectEntity, Exception exception) {

    }
}

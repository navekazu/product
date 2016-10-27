package tools.dbcomparator2.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.entity.TableCompareEntity;
import tools.dbcomparator2.enums.DBParseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class DBComparator implements DBParseNotification {
    private Logger logger = LoggerFactory.getLogger(DBComparator.class);
    private CompareTask compareTask;
    private DBParseNotification dbParseNotification;

    public DBComparator(DBParseNotification dbParseNotification) {
        this.dbParseNotification = dbParseNotification;
        this.compareTask = new CompareTask(dbParseNotification);
    }

    public void startCompare(List<ConnectEntity> connectEntityList) throws Exception {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        logger.info("■■■execute■■■");
        compareTask.reinitialize();
        compareTask.setConnectEntityList(connectEntityList);
        forkJoinPool.execute(compareTask);

    }

    public void cancelCompare() {
        logger.info("■■■cancel■■■");
        compareTask.cancel(true);
        dbParseNotification.end();
    }

    public List<RecordHashEntity> getReverseSideRecordHashEntity(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity, RecordHashEntity tableRecordEntity) {
        return compareTask.getReverseSideRecordHashEntity(connectEntity, tableCompareEntity, tableRecordEntity);
    }
    static class CompareTask extends ForkJoinTask<Void> {
        private Logger logger = LoggerFactory.getLogger(CompareTask.class);
        private List<ConnectEntity> connectEntityList;
        List<DBParser> dbParserList;
        private DBParseNotification dbParseNotification;

        public CompareTask(DBParseNotification dbParseNotification) {
            this.dbParseNotification = dbParseNotification;
        }

        public void setConnectEntityList(List<ConnectEntity> connectEntityList) {
            this.connectEntityList = connectEntityList;
        }

        public List<RecordHashEntity> getReverseSideRecordHashEntity(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity, RecordHashEntity tableRecordEntity) {
            List<RecordHashEntity> list = new ArrayList<>();
            dbParserList.stream()
                    .filter(dbParserEntity -> !dbParserEntity.getConnectEntity().equals(connectEntity))     // 引数の接続先以外
                    .forEach(dbParserEntity ->
                        dbParserEntity.getTableCompareEntityList().stream()
                                .filter(entity -> entity.getTableName().equals(tableCompareEntity.getTableName()))
                                .filter(entity -> entity.getRecordHashEntityMap().containsKey(tableRecordEntity.getPrimaryKeyHashValue()))
                                .forEach(entity -> list.add(entity.getRecordHashEntityMap().get(tableRecordEntity.getPrimaryKeyHashValue())))

                    );
            return list;
        }

        @Override
        public Void getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(Void value) {

        }

        @Override
        protected boolean exec() {
            dbParserList = new ArrayList<>();

            dbParseNotification.start();

            connectEntityList.stream()
                    .forEach(connectEntity -> {
                        DBParser dbParser = new DBParser(dbParseNotification);
                        try {
                            dbParser.connectDatabase(connectEntity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dbParserList.add(dbParser);
                    });
            if (isParseFailed(dbParserList)) {
                dbParseNotification.end();
                return false;
            }

            dbParserList.parallelStream()
                    .forEach(dbParser -> {
                        try {
                            dbParser.parseDatabase();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            if (isParseFailed(dbParserList)) {
                dbParseNotification.end();
                return false;
            }

            dbParserList.parallelStream()
                    .forEach(dbParser -> {
                        try {
                            dbParser.parseTables();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            if (isParseFailed(dbParserList)) {
                dbParseNotification.end();
                return false;
            }

            dbParserList.parallelStream()
                    .forEach(dbParser -> {
                        try {
                            dbParser.parsePrimaryKey();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            if (isParseFailed(dbParserList)) {
                dbParseNotification.end();
                return false;
            }

            dbParserList.parallelStream()
                    .forEach(dbParser -> {
                        try {
                            dbParser.parseTableData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            if (isParseFailed(dbParserList)) {
                dbParseNotification.end();
                return false;
            }

            dbParserList.stream()
                    .forEach(dbParser -> {
                        try {
                            dbParser.colseDatabase();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

            dbParseNotification.end();

            return true;
        }

        private boolean isParseFailed(List<DBParser> dbParserList) {
            return dbParserList.stream().anyMatch(dbParser -> dbParser.getDbParseStatus() == DBParseStatus.FAILED);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {
    }

    @Override
    public void parsedTableList(ConnectEntity connectEntity, List<TableCompareEntity> tableCompareEntityList) throws Exception {

    }

    @Override
    public void parsedPrimaryKey(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity) throws Exception {

    }

    @Override
    public void countedTableRecord(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity) throws Exception {

    }

    @Override
    public void parsedTableRecord(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity, int rowNumber, RecordHashEntity tableRecordEntity) throws Exception {

    }
}

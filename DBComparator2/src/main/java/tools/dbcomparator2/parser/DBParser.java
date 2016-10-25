package tools.dbcomparator2.parser;

import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.entity.TableCompareEntity;
import tools.dbcomparator2.enums.DBParseStatus;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class DBParser {
    private DBParseNotification dbParseNotification;
    private List<TableCompareEntity> tableCompareEntityList;
    private ConnectEntity connectEntity;
    private Connection connection;

    public DBParser(DBParseNotification dbParseNotification) {
        this.dbParseNotification = dbParseNotification;
        this.tableCompareEntityList = new ArrayList<>();
    }

    public void connectDatabase(ConnectEntity connectEntity) throws Exception {
        this.connectEntity = connectEntity;

        // ユーザ名/パスワード
        Properties info = new Properties();
        if (connectEntity.getUser()!=null) {
            info.setProperty("user", connectEntity.getUser());
        }
        if (connectEntity.getPassword()!=null) {
            info.setProperty("password", connectEntity.getPassword());
        }

        try {
            if (connectEntity.getLibrary()==null && connectEntity.getDriver()==null) {
                // ドライバ指定なし
                connection = DriverManager.getConnection(connectEntity.getUrl(), info);

            } else {
                // ドライバ指定あり
                URL[] lib = new URL[0];
                lib = new URL[]{new File(connectEntity.getLibrary()).toURI().toURL()};

                URLClassLoader loader = URLClassLoader.newInstance(lib);
                Class<Driver> cd = null;
                cd = (Class<Driver>) loader.loadClass(connectEntity.getDriver());
                Driver driver = cd.newInstance();
                connection = driver.connect(connectEntity.getUrl(), info);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void colseDatabase() throws Exception {
        try {
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void parseDatabase() throws Exception {
        DatabaseMetaData dmd = null;
        dmd = connection.getMetaData();

        try (ResultSet resultSet = dmd.getTables(null, connectEntity.getSchema(), "%", new String[]{"TABLE"})) {
            while (resultSet.next()) {
                TableCompareEntity entity = TableCompareEntity.builder()
                        .tableName(resultSet.getString("TABLE_NAME"))
                        .build();
                tableCompareEntityList.add(entity);
            }
        }

        dbParseNotification.parsedTableList(connectEntity, tableCompareEntityList);
    }

    public void parseTables() throws Exception {
        tableCompareEntityList.stream()
                .forEach(entity -> parseRecordCount(entity));

    }

    private void parseRecordCount(TableCompareEntity tableCompareEntity) {
        String query = "select count(*) CNT from "+tableCompareEntity.getTableName();
        try {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                if (!resultSet.next()) {
                    throw new Exception();
                }

                tableCompareEntity.setRecordCount(resultSet.getInt("CNT"));
                dbParseNotification.countedTableRecord(connectEntity, tableCompareEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parsePrimaryKey() throws Exception {
        for (TableCompareEntity entity: tableCompareEntityList) {

            DatabaseMetaData dmd = connection.getMetaData();

            try (ResultSet resultSet = dmd.getPrimaryKeys(null, connectEntity.getSchema(), entity.getTableName())) {
                List<String> primaryKeyColumnList = new ArrayList<>();
                while (resultSet.next()) {
                    primaryKeyColumnList.add(resultSet.getString("COLUMN_NAME"));
                }
                entity.setPrimaryKeyColumnList(primaryKeyColumnList);
            }

            dbParseNotification.parsedPrimaryKey(connectEntity, entity);
        }
    }

    public void parseTableData() throws Exception {
        ForkJoinPool forkJoinPool = new ForkJoinPool(30);
        forkJoinPool.submit(() ->
                tableCompareEntityList.parallelStream()
                        .forEach(entity -> parseTable(entity))
        ).get();
    }

    private void parseTable(TableCompareEntity tableCompareEntity) {
        String query = String.format("select * from %s", tableCompareEntity.getTableName());

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            int rowNumber = 0;
            while (resultSet.next()) {
                List<Object> primaryKeyValueList = new ArrayList<>();
                List<Object> allColumnValueList = new ArrayList<>();
                Map<String, Object> primaryKeyValueMap = new HashMap<>();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                for (int i=1; i<=columnCount; i++) {
                    if (tableCompareEntity.getPrimaryKeyColumnList().contains(resultSetMetaData.getColumnName(i))) {
                        primaryKeyValueList.add(resultSet.getObject(i));
                        primaryKeyValueMap.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                    }
                    allColumnValueList.add(resultSet.getObject(i));
                }

                RecordHashEntity recordHashEntity = RecordHashEntity.builder()
                        .primaryKeyHashValue(RecordHashEntity.createHashValue(primaryKeyValueList))
                        .allColumnHashValue(RecordHashEntity.createHashValue(allColumnValueList))
                        .primaryKeyValueMap(primaryKeyValueMap)
                        .build();
                tableCompareEntity.addRecordHashEntity(recordHashEntity);

                dbParseNotification.parsedTableRecord(connectEntity, tableCompareEntity, rowNumber, recordHashEntity);
                rowNumber++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

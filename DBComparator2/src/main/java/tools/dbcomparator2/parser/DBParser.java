package tools.dbcomparator2.parser;

import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.TableCompareEntity;
import tools.dbcomparator2.enums.DBParseStatus;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

    public void parse() throws Exception {
        try {
            parseDatabase();
            parseTables();
            parsePrimaryKey();

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
}

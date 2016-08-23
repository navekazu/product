package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.enums.RecordCompareStatus;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.*;

/**
 * DB解析を行うクラス
 */
public class DBParseService {
    private Logger logger = LoggerFactory.getLogger(DBParseService.class);
    private DBParseNotification notification;
    private DBCompareEntity dbCompareEntity;
    private Connection connection;

    public DBParseService(DBParseNotification notification, DBCompareEntity connectEntity) {
        this.notification = notification;
        this.dbCompareEntity = connectEntity;
        this.connection = null;
    }

    public void connect() {
        logger.info(String.format("DB connect start. %s", dbCompareEntity.getConnectEntity().getConnectionName()));

        ConnectEntity connectEntity = dbCompareEntity.getConnectEntity();

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

            logger.info(String.format("DB connect end. %s", dbCompareEntity.getConnectEntity().getConnectionName()));

        } catch (Exception e) {
            e.printStackTrace();
            notification.fatal(connectEntity, e);
        }
    }

    public void disconnect() {
        logger.info(String.format("DB disconnect start. %s", dbCompareEntity.getConnectEntity().getConnectionName()));
        try {
            if (connection!=null) {
                connection.close();
            }
            logger.info(String.format("DB disconnect end. %s", dbCompareEntity.getConnectEntity().getConnectionName()));
        } catch (SQLException e) {
            e.printStackTrace();
            notification.fatal(dbCompareEntity.getConnectEntity(), e);
        }
    }

    public void parseTableList() {
        logger.info(String.format("Table parse start. %s", dbCompareEntity.getConnectEntity().getConnectionName()));

        List<String> tableList = new ArrayList<>();
        DatabaseMetaData dmd = null;
        try {
            dmd = connection.getMetaData();

            try (ResultSet resultSet = dmd.getTables(null, dbCompareEntity.getConnectEntity().getSchema(), "%", new String[]{"TABLE"})) {
                while (resultSet.next()) {
                    tableList.add(resultSet.getString("TABLE_NAME"));
                }
            }

            notification.parsedTableList(dbCompareEntity.getConnectEntity(), tableList);
            logger.info(String.format("Table parse end. [count:%,d] %s",tableList.size(), dbCompareEntity.getConnectEntity().getConnectionName()));

        } catch (SQLException e) {
            e.printStackTrace();
            notification.fatal(dbCompareEntity.getConnectEntity(), e);
        }

    }

    public void parseTableData(String tableName) {
        logger.info(String.format("Data parse start. [table:%s] %s", tableName, dbCompareEntity.getConnectEntity().getConnectionName()));

        try {
            // レコード数取得
            int recordCount = getRecordCount(tableName);
            notification.countedTableRecord(dbCompareEntity.getConnectEntity(), tableName, recordCount);
            logger.info(String.format("Counted table record. [count:%,d] [table:%s] %s", recordCount, tableName, dbCompareEntity.getConnectEntity().getConnectionName()));

            // PK情報の取得
            List<String> primaryKeyList = getPrimaryKeyList(tableName);
            notification.parsedPrimaryKey(dbCompareEntity.getConnectEntity(), tableName, primaryKeyList);
            logger.info(String.format("PrimaryKey parse end. [column count:%,d] [table:%s] %s", primaryKeyList.size(), tableName, dbCompareEntity.getConnectEntity().getConnectionName()));

            // データ取得
            int row = 0;
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(String.format("select * from %s", tableName))) {
                    int columnCount = resultSet.getMetaData().getColumnCount();

                    // データのハッシュ化
                    while (resultSet.next()) {
                        List<String> primaryKeyValueList = new ArrayList<>();
                        List<String> allColumnValueList = new ArrayList<>();
                        Map<String, String> primaryKeyValueMap = new HashMap<>();

                        // PKの値一覧
                        for (String pk: primaryKeyList) {
                            String value = resultSet.getString(pk);
                            if (resultSet.wasNull()) {
                                value = "";
                            }
                            primaryKeyValueList.add(value);
                            primaryKeyValueMap.put(pk, value);
                        }

                        // 全カラムの値一覧
                        for (int col=1; col<=columnCount; col++) {
                            String value = resultSet.getString(col);
                            if (resultSet.wasNull()) {
                                value = "";
                            }
                            allColumnValueList.add(value);
                        }

                        RecordHashEntity tableRecordEntity = RecordHashEntity.builder()
                                .primaryKeyHashValue(RecordHashEntity.createHashValue(primaryKeyValueList))
                                .allColumnHashValue(RecordHashEntity.createHashValue(allColumnValueList))
                                .primaryKeyValueMap(primaryKeyValueMap)
                                .recordCompareStatus(RecordCompareStatus.READY)
                                .build();
                        notification.parsedTableRecord(dbCompareEntity.getConnectEntity(), tableName, tableRecordEntity);

                        row++;
                        logger.info(String.format("Parsed table record. [row:%,d] [columns:%,d] [table:%s] %s", row, allColumnValueList.size(), tableName, dbCompareEntity.getConnectEntity().getConnectionName()));
                    }
                }
            }



            logger.info(String.format("Data parse end. [table:%s] %s", tableName, dbCompareEntity.getConnectEntity().getConnectionName()));
        } catch (SQLException e) {
            e.printStackTrace();
            notification.fatal(dbCompareEntity.getConnectEntity(), e);
        }
    }

    private int getRecordCount(String tableName) throws SQLException {
        int recordCount = 0;

        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(String.format("select count(*) as CNT from %s", tableName))) {
                if (!resultSet.next()) {
                    throw new RuntimeException();
                }
                recordCount = resultSet.getInt(1);
            }
        }

        return recordCount;
    }

    private List<String> getPrimaryKeyList(String tableName) throws SQLException {
        List<String> primaryKeyList = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        try (ResultSet resultSet = metaData.getPrimaryKeys(null, dbCompareEntity.getConnectEntity().getSchema(), tableName)) {
            while (resultSet.next()) {
                primaryKeyList.add(resultSet.getString("COLUMN_NAME"));
            }
        }

        return primaryKeyList;
    }
}

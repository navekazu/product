package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;
import tools.dbcomparator2.entity.PrimaryKeyValue;
import tools.dbcomparator2.enums.DBCompareStatus;
import tools.dbcomparator2.enums.DBParseStatus;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.*;

public class __DBParseService {
    private Logger logger = LoggerFactory.getLogger(__DBParseService.class);

    private DBParseStatus status;
    private Connection connection;
    private List<String> tableList;

    public __DBParseService() {
        status = DBParseStatus.OK;
    }

    public DBParseStatus getStatus() {
        return status;
    }

    public void prepare(DBCompareEntity dbCompareEntity) {
        try {
            connection = createConnection(dbCompareEntity.getConnectEntity());
        } catch (Exception e) {
            e.printStackTrace();
            status = DBParseStatus.FAILED;
        }
    }

    public void parseTables(DBCompareEntity dbCompareEntity) {
        try {
            logger.info("Table parse start: [url={} user={}]", dbCompareEntity.getConnectEntity().getUrl(), dbCompareEntity.getConnectEntity().getUser());
            tableList = getTables(dbCompareEntity.getConnectEntity(), connection);
            logger.info("Table parse end: {} tables [url={} user={}]", tableList.size(), dbCompareEntity.getConnectEntity().getUrl(), dbCompareEntity.getConnectEntity().getUser());
        } catch (Exception e) {
            e.printStackTrace();
            status = DBParseStatus.FAILED;
        }
    }

    public void startParse(DBCompareEntity dbCompareEntity) {
        logger.info("DB parse start: [url={} user={}]", dbCompareEntity.getConnectEntity().getUrl(), dbCompareEntity.getConnectEntity().getUser());

        try {
            // テーブル毎にPrimaryKeyValueを作成
            Map<String, List<PrimaryKeyValue>> tableValues = new HashMap<>();
            tableList.parallelStream()
                    .forEach(table -> {
                        try {
                            logger.info("Primary key parse start: [url={} user={} table={}]", dbCompareEntity.getConnectEntity().getUrl(), dbCompareEntity.getConnectEntity().getUser(), table);
                            List<PrimaryKeyValue> primaryKeyValueList = createPrimaryKeyValue(connection, table, getPrimaryKeyColumns(connection, table, dbCompareEntity.getConnectEntity()));
                            tableValues.put(table, primaryKeyValueList);
                            logger.info("Primary key parse end: {} primary keys [url={} user={} table={}]", primaryKeyValueList.size(), dbCompareEntity.getConnectEntity().getUrl(), dbCompareEntity.getConnectEntity().getUser(), table);
                        } catch(SQLException e) {
                            e.printStackTrace();
                        }
                    });


            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        dbCompareEntity.setStatus(DBCompareStatus.SCAN_FINISHED);
        logger.info("DB parse finished: url={} user={}", dbCompareEntity.getConnectEntity().getUrl(), dbCompareEntity.getConnectEntity().getUser());
    }

    private Connection createConnection(ConnectEntity connectEntity) throws SQLException, MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        // ユーザ名/パスワード
        Properties info = new Properties();
        if (connectEntity.getUser()!=null) {
            info.setProperty("user", connectEntity.getUser());
        }
        if (connectEntity.getPassword()!=null) {
            info.setProperty("password", connectEntity.getPassword());
        }

        // ドライバ指定なし
        if (connectEntity.getLibrary()==null && connectEntity.getDriver()==null) {
            return DriverManager.getConnection(connectEntity.getUrl(), info);
        }

        // ドライバ指定あり
        URL[] lib = {new File(connectEntity.getLibrary()).toURI().toURL()};
        URLClassLoader loader = URLClassLoader.newInstance(lib);
        Class<Driver> cd = (Class<Driver>) loader.loadClass(connectEntity.getDriver());
        Driver driver = cd.newInstance();
        return driver.connect(connectEntity.getUrl(), info);
    }

    private List<String> getTables(ConnectEntity connectEntity, Connection connection) throws SQLException {
        List<String> tableList = new ArrayList<>();
        DatabaseMetaData dmd = connection.getMetaData();

        try (ResultSet resultSet = dmd.getTables(null, connectEntity.getSchema(), "%", new String[]{"TABLE"})) {
            while (resultSet.next()) {
                tableList.add(resultSet.getString("TABLE_NAME"));
            }
        }

        return tableList;
    }

    private List<String> getPrimaryKeyColumns(Connection connection, String table, ConnectEntity connectEntity) throws SQLException {
        List<String> primaryKeyColumnList = new ArrayList<>();
        DatabaseMetaData dmd = connection.getMetaData();

        try (ResultSet resultSet = dmd.getPrimaryKeys(null, connectEntity.getSchema(), table)) {
            while (resultSet.next()) {
                primaryKeyColumnList.add(resultSet.getString("COLUMN_NAME"));
            }
        }

        return primaryKeyColumnList;
    }

    private List<PrimaryKeyValue> createPrimaryKeyValue(Connection connection, String table, List<String> primaryKeyColumns) throws SQLException {
        List<PrimaryKeyValue> primaryKeyValueList = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("select * from "+table)) {
                while (resultSet.next()) {
                    PrimaryKeyValue primaryKeyValue = new PrimaryKeyValue();

                    // ラムダの例外ハンドリング問題・・・
                    try {
                        primaryKeyColumns.stream().forEach(col -> {
                            try {
                                primaryKeyValue.addPrimaryKeyValue(col, resultSet.getString(col));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch(RuntimeException e) {
                        throw (SQLException)e.getCause();
                    }

                    primaryKeyValueList.add(primaryKeyValue);
                }
            }
        }

        return primaryKeyValueList;
    }

}

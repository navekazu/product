package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.TableRecordEntity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * DB解析を行うクラス
 */
public class DBParseService {
    private Logger logger = LoggerFactory.getLogger(DBParseService.class);
    private DBParseNotification notification;
    private ConnectEntity connectEntity;
    private Connection connection;

    public DBParseService(DBParseNotification notification, ConnectEntity connectEntity) {
        this.notification = notification;
        this.connectEntity = connectEntity;
        this.connection = null;
    }

    public void connect() {
        logger.info(String.format("DB connect start. %s", connectEntity.getConnectionName()));

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

            logger.info(String.format("DB connect end. %s", connectEntity.getConnectionName()));

        } catch (Exception e) {
            e.printStackTrace();
            notification.fatal(connectEntity, e);
        }
    }

    public void disconnect() {
        logger.info(String.format("DB disconnect start. %s", connectEntity.getConnectionName()));
        try {
            if (connection!=null) {
                connection.close();
            }
            logger.info(String.format("DB disconnect end. %s", connectEntity.getConnectionName()));
        } catch (SQLException e) {
            e.printStackTrace();
            notification.fatal(connectEntity, e);
        }
    }

    public void parseTableList() {
        logger.info(String.format("Table parse start. %s", connectEntity.getConnectionName()));

        List<String> tableList = new ArrayList<>();
        DatabaseMetaData dmd = null;
        try {
            dmd = connection.getMetaData();

            try (ResultSet resultSet = dmd.getTables(null, connectEntity.getSchema(), "%", new String[]{"TABLE"})) {
                while (resultSet.next()) {
                    tableList.add(resultSet.getString("TABLE_NAME"));
                }
            }

            notification.parsedTableList(connectEntity, tableList);
            logger.info(String.format("Table parse end. [count:%,d] %s",tableList.size(), connectEntity.getConnectionName()));

        } catch (SQLException e) {
            e.printStackTrace();
            notification.fatal(connectEntity, e);
        }

    }

    public void parseTableData(String tableName) {
        logger.info(String.format("Data parse start. [table:%s] %s", tableName, connectEntity.getConnectionName()));

        int recordCount = 0;
        notification.countedTableRecord(connectEntity, tableName, recordCount);
        logger.info(String.format("Counted table record. [count:%,d] [table:%s] %s", recordCount, tableName, connectEntity.getConnectionName()));

        int row = 0;
        notification.parsedTableRecord(connectEntity, tableName, null);
        logger.info(String.format("Parsed table record. [row:%,d] [table:%s] %s", row, tableName, connectEntity.getConnectionName()));
    }
}

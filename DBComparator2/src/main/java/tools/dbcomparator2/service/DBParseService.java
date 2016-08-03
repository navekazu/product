package tools.dbcomparator2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;
import tools.dbcomparator2.entity.PrimaryKeyValue;
import tools.dbcomparator2.enums.DBCompareStatus;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.*;

public class DBParseService {
    Logger logger = LoggerFactory.getLogger(DBParseService.class);

    public void startParse(DBCompareEntity dbCompareEntity) {
        logger.info("DB parse start: url={} user={}", dbCompareEntity.getConnectEntity().getUrl(), dbCompareEntity.getConnectEntity().getUser());

        try {
            Connection connection = createConnection(dbCompareEntity.getConnectEntity());
            List<String> tableList = getTables(dbCompareEntity.getConnectEntity(), connection);

            Map<String, List<PrimaryKeyValue>> tableValues = new HashMap<>();

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
}

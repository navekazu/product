package tools.dbconnector8.logic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

import tools.dbconnector8.model.ConnectionModel;
import tools.dbconnector8.persistence.config.ConnectionConfig;

public class ConnectionLogic extends LogicBase<ConnectionConfig, ConnectionModel> {

	@Override
	public ConnectionModel execute(ConnectionConfig i) throws SQLException, MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Connection conn = null;

        Properties info = new Properties();

        if (Objects.nonNull(i.getUser()) && !Objects.equals(i.getUser(), "")) {
            info.setProperty("user", i.getUser());
        }
        if (Objects.nonNull(i.getPassword()) && !Objects.equals(i.getPassword(), "")) {
            info.setProperty("password", i.getPassword());
        }

        if (Objects.nonNull(i.getLibraryPath()) && !Objects.equals(i.getLibraryPath(), "") &&
        		Objects.nonNull(i.getDriver()) && !Objects.equals(i.getDriver(), "")) {
            // ドライバ指定あり
            URL[] lib = {new File(i.getLibraryPath()).toURI().toURL()};
            URLClassLoader loader = URLClassLoader.newInstance(lib);
            Class<Driver> cd = (Class<Driver>) loader.loadClass(i.getDriver());
            Driver driver = cd.newInstance();
            conn = driver.connect(i.getUrl(), info);
        } else {
            // ドライバ指定なし
//          Class.forName(entity.getDriver());
            conn = DriverManager.getConnection(i.getUrl(), info);
        }
        conn.setAutoCommit(false);

        return ConnectionModel.builder()
        		.connection(conn)
        		.connectionConfig(i)
        		.tables(new HashMap<>())
        		.columns(new HashMap<>())
        		.functions(new ArrayList<>())
        		.build();
	}

}

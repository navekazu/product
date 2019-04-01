package tools.salt.saltserver.config;

import java.sql.Connection;
import java.util.TimeZone;

import com.gs.fw.common.mithra.bulkloader.BulkLoader;
import com.gs.fw.common.mithra.bulkloader.BulkLoaderException;
import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager;
import com.gs.fw.common.mithra.connectionmanager.XAConnectionManager;
import com.gs.fw.common.mithra.databasetype.DatabaseType;
import com.gs.fw.common.mithra.databasetype.MariaDatabaseType;

public class ConnectionManager implements SourcelessConnectionManager {

    protected static ConnectionManager instance;
    protected static final String MAX_POOL_SIZE_KEY = "maxPoolSize";
    protected final int DEFAULT_MAX_WAIT = 500;
    protected static final int DEFAULT_POOL_SIZE = 10;
    private static final TimeZone TIMEZONE = TimeZone.getTimeZone("Asia/Tokyo");

    private final String driverClassname = "com.mysql.jdbc.Driver";
    private final String serverName = "";
    private final String resourceName = "";
    private final String userName = "";
    private final String password = "";

    private XAConnectionManager xaConnectionManager;

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    protected ConnectionManager() {
        this.createConnectionManager();
    }

    private XAConnectionManager createConnectionManager() {
        xaConnectionManager = new XAConnectionManager();
        xaConnectionManager.setDriverClassName(driverClassname);
        xaConnectionManager.setMaxWait(DEFAULT_MAX_WAIT);
        xaConnectionManager.setLdapName(serverName);
        xaConnectionManager.setDefaultSchemaName(resourceName);
        xaConnectionManager.setJdbcUser(userName);
        xaConnectionManager.setJdbcPassword(password);
        xaConnectionManager.setPoolName("connection pool");
        xaConnectionManager.setInitialSize(1);
        xaConnectionManager.setPoolSize(DEFAULT_POOL_SIZE);
        xaConnectionManager.setUseStatementPooling(true); // Only good for DB2
        xaConnectionManager.initialisePool();
        return xaConnectionManager;
    }

    @Override
    public BulkLoader createBulkLoader() throws BulkLoaderException {
        return this.getDatabaseType().createBulkLoader(
                this.xaConnectionManager.getJdbcUser(),
                this.xaConnectionManager.getJdbcPassword(),
                this.xaConnectionManager.getHostName(),
                this.xaConnectionManager.getPort());
    }

    @Override
    public Connection getConnection() {
        return xaConnectionManager.getConnection();
    }

    @Override
    public DatabaseType getDatabaseType() {
        // return SybaseDatabaseType.getInstance();
        // return PostgresDatabaseType.getInstance();
        // return OracleDatabaseType.getInstance();
        // return MsSqlDatabaseType.getInstance();
        // return H2DatabaseType.getInstance();
        // return DerbyDatabaseType.getInstance();
        return MariaDatabaseType.getInstance();
    }

    @Override
    public TimeZone getDatabaseTimeZone() {
        return TIMEZONE;
    }

    @Override
    public String getDatabaseIdentifier() {
        return xaConnectionManager.getServerName()+":"+xaConnectionManager.getResourceName();
    }

}

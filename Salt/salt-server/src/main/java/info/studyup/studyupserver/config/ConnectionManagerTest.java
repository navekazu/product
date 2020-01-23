package info.studyup.studyupserver.config;

import java.sql.Connection;
import java.util.TimeZone;

import com.gs.fw.common.mithra.bulkloader.BulkLoader;
import com.gs.fw.common.mithra.bulkloader.BulkLoaderException;
import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager;
import com.gs.fw.common.mithra.connectionmanager.XAConnectionManager;
import com.gs.fw.common.mithra.databasetype.DatabaseType;
import com.gs.fw.common.mithra.databasetype.MariaDatabaseType;

public class ConnectionManagerTest implements SourcelessConnectionManager {

    protected static ConnectionManagerTest instance;
    protected static final String MAX_POOL_SIZE_KEY = "maxPoolSize";
    protected final int DEFAULT_MAX_WAIT = 500;
    protected static final int DEFAULT_POOL_SIZE = 10;
    private static final TimeZone TIMEZONE = TimeZone.getTimeZone("Asia/Tokyo");

    private final String driverClassname = "org.postgresql.Driver";
    private final String serverName = "studyup-rds-instance.c9hbdsmfn2w8.ap-northeast-1.rds.amazonaws.com";
    private final String resourceName = "studyup_rds_instance";
    private final String userName = "studyup_rds_instance_admin";
    private final String password = "studyup_rds_instance_password_01!";
    private final int port = 3306;

    private XAConnectionManager xaConnectionManager;

    public static synchronized ConnectionManagerTest getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerTest();
        }
        return instance;
    }

    protected ConnectionManagerTest() {
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
        xaConnectionManager.setPort(port);
        xaConnectionManager.setPoolName("connection pool");
        xaConnectionManager.setInitialSize(1);
        xaConnectionManager.setPoolSize(DEFAULT_POOL_SIZE);
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

package tools.dbcomparator2.parser;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.entity.TableCompareEntity;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DBComparatorTest {
    private Logger logger = LoggerFactory.getLogger(DBComparatorTest.class);

    private static final String JDBC_DRIVER = "org.h2.Driver";
    //    private static final String JDBC_URL1 = "jdbc:h2:file:./testdb/test1";
    private static final String JDBC_URL1 = "jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1";
    //    private static final String JDBC_URL2 = "jdbc:h2:file:./testdb/test2";
    private static final String JDBC_URL2 = "jdbc:h2:mem:test2;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @BeforeClass
    public static void beforeClass() throws Exception {
        RunScript.execute(JDBC_URL1, USER, PASSWORD, "schema_DBParserTest.sql", Charset.forName("UTF-8"), false);
        RunScript.execute(JDBC_URL2, USER, PASSWORD, "schema_DBParserTest.sql", Charset.forName("UTF-8"), false);
    }

    @AfterClass
    public static void afterClass() throws Exception {
    }

    @Before
    public void before() throws Exception {
        insertTestData(JDBC_URL1, "dataset_DBParserTest.xls");
        insertTestData(JDBC_URL2, "dataset_DBParserTest.xls");
    }
    private void insertTestData(String url, String script) throws Exception {
        IDataSet dataSet = new XlsDataSet(new File(script));
        IDatabaseTester databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, url, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void dbComparatorTest() throws Exception {

        List<ConnectEntity> connectEntityList = new ArrayList<>();
        connectEntityList.add(ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver(JDBC_DRIVER)
                .url(JDBC_URL1)
                .user(USER)
                .password(PASSWORD)
                .schema("PUBLIC")
                .build());
        connectEntityList.add(ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver(JDBC_DRIVER)
                .url(JDBC_URL2)
                .user(USER)
                .password(PASSWORD)
                .schema("PUBLIC")
                .build());

        DBComparator dbComparator = new DBComparator(new DBParseNotification() {
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
        });
        dbComparator.startCompare(connectEntityList);
    }

    @Test
    public void compareTaskTest() throws Exception {
        DBComparator.CompareTask compareTask = new DBComparator.CompareTask(new DBParseNotification() {

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
        });

    }
}

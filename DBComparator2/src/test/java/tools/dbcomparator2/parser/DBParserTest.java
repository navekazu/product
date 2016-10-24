package tools.dbcomparator2.parser;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.*;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.TableCompareEntity;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DBParserTest {
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
    public void parseTablesTest() throws Exception {
        DBParser dbParser = new DBParser(new DBParseNotification(){
            @Override
            public void parsedTableList(ConnectEntity connectEntity, List<TableCompareEntity> tableCompareEntityList) {
                assertEquals(3, tableCompareEntityList.size());
            }

            @Override
            public void parsedPrimaryKey(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity) throws Exception {
                switch(tableCompareEntity.getTableName()) {
                    case "TABLE01":
                        assertEquals(1, tableCompareEntity.getPrimaryKeyColumnList().size());
                        assertEquals("COL01", tableCompareEntity.getPrimaryKeyColumnList().get(0));
                        break;
                    case "TABLE02":
                        assertEquals(1, tableCompareEntity.getPrimaryKeyColumnList().size());
                        assertEquals("COL01", tableCompareEntity.getPrimaryKeyColumnList().get(0));
                        break;
                    case "TABLE03":
                        assertEquals(2, tableCompareEntity.getPrimaryKeyColumnList().size());
                        assertEquals("COL01", tableCompareEntity.getPrimaryKeyColumnList().get(0));
                        assertEquals("COL02", tableCompareEntity.getPrimaryKeyColumnList().get(1));
                        break;
                }
            }

            @Override
            public void countedTableRecord(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity) {
                switch(tableCompareEntity.getTableName()) {
                    case "TABLE01":
                        assertEquals(3, tableCompareEntity.getRecordCount());
                        break;
                    case "TABLE02":
                        assertEquals(20, tableCompareEntity.getRecordCount());
                        break;
                    case "TABLE03":
                        assertEquals(10, tableCompareEntity.getRecordCount());
                        break;
                }
            }
        });

        dbParser.connectDatabase(ConnectEntity.builder()
                        .library("h2-1.3.176.jar")
                        .driver(JDBC_DRIVER)
                        .url(JDBC_URL1)
                        .user(USER)
                        .password(PASSWORD)
                        .schema("PUBLIC")
                        .build()
        );

        dbParser.parse();

        dbParser.colseDatabase();
    }
}

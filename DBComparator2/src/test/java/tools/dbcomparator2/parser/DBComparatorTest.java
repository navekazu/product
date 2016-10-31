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
import java.util.*;

import static org.junit.Assert.assertEquals;

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
        DBParseNotification dbParseNotification = new DBParseNotification() {
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
        };

        Map<String, Object> primaryKeyValueMapAA1 = new HashMap<>();
        primaryKeyValueMapAA1.put("COL1", "COL1VALUE");
        primaryKeyValueMapAA1.put("COL2", "COL2VALUE");
        primaryKeyValueMapAA1.put("COL3", "COL3VALUE");

        Map<String, Object> primaryKeyValueMapAA2 = new HashMap<>();
        primaryKeyValueMapAA2.put("COL1", "COL1VALUE");
        primaryKeyValueMapAA2.put("COL2", "COL2VALUE");
        primaryKeyValueMapAA2.put("COL3", "COL3VALUE");

        Map<String, Object> primaryKeyValueMapAA3 = new HashMap<>();
        primaryKeyValueMapAA3.put("COL1", "COL1VALUE");
        primaryKeyValueMapAA3.put("COL2", "COL2VALUE");
        primaryKeyValueMapAA3.put("COL3", "COL3VALUE");

        RecordHashEntity recordHashEntityAA1 = RecordHashEntity.builder()
                .primaryKeyHashValue("a")
                .allColumnHashValue("b")
                .primaryKeyValueMap(primaryKeyValueMapAA1)
                .build();
        RecordHashEntity recordHashEntityAA2 = RecordHashEntity.builder()
                .primaryKeyHashValue("a")
                .allColumnHashValue("b")
                .primaryKeyValueMap(primaryKeyValueMapAA2)
                .build();
        RecordHashEntity recordHashEntityAA3 = RecordHashEntity.builder()
                .primaryKeyHashValue("a")
                .allColumnHashValue("b")
                .primaryKeyValueMap(primaryKeyValueMapAA3)
                .build();

        Map<String, RecordHashEntity> recordHashEntityMapAA = new HashMap<>();
        recordHashEntityMapAA.put(recordHashEntityAA1.getPrimaryKeyHashValue(), recordHashEntityAA1);
        recordHashEntityMapAA.put(recordHashEntityAA2.getPrimaryKeyHashValue(), recordHashEntityAA2);
        recordHashEntityMapAA.put(recordHashEntityAA3.getPrimaryKeyHashValue(), recordHashEntityAA3);

        TableCompareEntity tableCompareEntityAA = TableCompareEntity.builder()
                .tableName("TABLEA")
                .recordCount(3)
                .recordHashEntityMap(recordHashEntityMapAA)
                .build();

        DBParser dbParserA = new DBParser(dbParseNotification);
        dbParserA.tableCompareEntityList.add(tableCompareEntityAA);
        dbParserA.connectEntity = ConnectEntity.builder()
                .url("A")
                .build();


        Map<String, Object> primaryKeyValueMapBA1 = new HashMap<>();
        primaryKeyValueMapBA1.put("COL1", "COL1VALUE");
        primaryKeyValueMapBA1.put("COL2", "COL2VALUE");
        primaryKeyValueMapBA1.put("COL3", "COL3VALUE");

        Map<String, Object> primaryKeyValueMapBA2 = new HashMap<>();
        primaryKeyValueMapBA2.put("COL1", "COL1VALUE");
        primaryKeyValueMapBA2.put("COL2", "COL2VALUE");
        primaryKeyValueMapBA2.put("COL3", "COL3VALUE");

        Map<String, Object> primaryKeyValueMapBA3 = new HashMap<>();
        primaryKeyValueMapBA3.put("COL1", "COL1VALUE");
        primaryKeyValueMapBA3.put("COL2", "COL2VALUE");
        primaryKeyValueMapBA3.put("COL3", "COL3VALUE");

        RecordHashEntity recordHashEntityBA1 = RecordHashEntity.builder()
                .primaryKeyHashValue("a")
                .allColumnHashValue("b")
                .primaryKeyValueMap(primaryKeyValueMapBA1)
                .build();
        RecordHashEntity recordHashEntityBA2 = RecordHashEntity.builder()
                .primaryKeyHashValue("a")
                .allColumnHashValue("b")
                .primaryKeyValueMap(primaryKeyValueMapBA2)
                .build();
        RecordHashEntity recordHashEntityBA3 = RecordHashEntity.builder()
                .primaryKeyHashValue("a")
                .allColumnHashValue("b")
                .primaryKeyValueMap(primaryKeyValueMapBA3)
                .build();

        Map<String, RecordHashEntity> recordHashEntityMapBA = new HashMap<>();
        recordHashEntityMapBA.put(recordHashEntityBA1.getPrimaryKeyHashValue(), recordHashEntityBA1);
        recordHashEntityMapBA.put(recordHashEntityBA2.getPrimaryKeyHashValue(), recordHashEntityBA2);
        recordHashEntityMapBA.put(recordHashEntityBA3.getPrimaryKeyHashValue(), recordHashEntityBA3);

        TableCompareEntity tableCompareEntityBA = TableCompareEntity.builder()
                .tableName("TABLEA")
                .recordCount(3)
                .recordHashEntityMap(recordHashEntityMapBA)
                .build();

        DBParser dbParserB = new DBParser(dbParseNotification);
        dbParserB.tableCompareEntityList.add(tableCompareEntityBA);
        dbParserB.connectEntity = ConnectEntity.builder()
                .url("B")
                .build();


        DBComparator.CompareTask compareTask = new DBComparator.CompareTask(dbParseNotification);
        compareTask.dbParserList = new ArrayList<>();
        compareTask.dbParserList.add(dbParserA);
        compareTask.dbParserList.add(dbParserB);
        List<RecordHashEntity> list;

        list= compareTask.getReverseSideRecordHashEntity(
                ConnectEntity.builder()
                        .url("B")
                .build(),
                tableCompareEntityBA, recordHashEntityBA1);
        assertEquals(1, list.size());
        assertEquals(recordHashEntityAA1.getPrimaryKeyHashValue(), list.get(0).getPrimaryKeyHashValue());
        assertEquals(recordHashEntityAA1.getPrimaryKeyHashValue(), list.get(0).getPrimaryKeyHashValue());
        assertEquals(recordHashEntityAA1.getPrimaryKeyValueMap(), list.get(0).getPrimaryKeyValueMap());

        list= compareTask.getReverseSideRecordHashEntity(
                ConnectEntity.builder()
                        .url("B")
                        .build(),
                tableCompareEntityBA, recordHashEntityBA2);
        assertEquals(1, list.size());
        assertEquals(recordHashEntityAA2.getPrimaryKeyHashValue(), list.get(0).getPrimaryKeyHashValue());
        assertEquals(recordHashEntityAA2.getPrimaryKeyHashValue(), list.get(0).getPrimaryKeyHashValue());
        assertEquals(recordHashEntityAA2.getPrimaryKeyValueMap(), list.get(0).getPrimaryKeyValueMap());
    }

}

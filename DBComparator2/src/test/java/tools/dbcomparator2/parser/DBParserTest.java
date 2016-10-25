package tools.dbcomparator2.parser;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.*;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.entity.TableCompareEntity;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                        // TABLE01のプライマリーキーは1つで、COL01
                        assertEquals(1, tableCompareEntity.getPrimaryKeyColumnList().size());
                        assertEquals("COL01", tableCompareEntity.getPrimaryKeyColumnList().get(0));
                        break;
                    case "TABLE02":
                        // TABLE02のプライマリーキーは1つで、COL01
                        assertEquals(1, tableCompareEntity.getPrimaryKeyColumnList().size());
                        assertEquals("COL01", tableCompareEntity.getPrimaryKeyColumnList().get(0));
                        break;
                    case "TABLE03":
                        // TABLE03のプライマリーキーは2つで、COL01とCOL02
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
                        // TABLE01のレコード数は3つ
                        assertEquals(3, tableCompareEntity.getRecordCount());
                        break;
                    case "TABLE02":
                        // TABLE02のレコード数は20つ
                        assertEquals(20, tableCompareEntity.getRecordCount());
                        break;
                    case "TABLE03":
                        // TABLE03のレコード数は10つ
                        assertEquals(10, tableCompareEntity.getRecordCount());
                        break;
                }
            }

            @Override
            public void parsedTableRecord(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity, int rowNumber, RecordHashEntity tableRecordEntity) throws Exception {
                List<Object> primaryKeyValueList = new ArrayList<>();
                List<Object> allColumnValueList = new ArrayList<>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calendar = Calendar.getInstance();

                switch(tableCompareEntity.getTableName()) {
                    case "TABLE01":
                        switch(rowNumber) {
                            case 0:
                                // TABLE01の1レコード目

                                // プライマリーキーのハッシュ
                                primaryKeyValueList.add(1);
                                assertEquals(RecordHashEntity.createHashValue(primaryKeyValueList), tableRecordEntity.getPrimaryKeyHashValue());

                                // 全カラム値のハッシュ
                                allColumnValueList.add(1);
                                allColumnValueList.add("Value0001");
                                assertEquals(RecordHashEntity.createHashValue(allColumnValueList), tableRecordEntity.getAllColumnHashValue());

                                // プライマリーキー値
                                assertEquals(1, tableRecordEntity.getPrimaryKeyValueMap().size());
                                assertEquals(BigDecimal.valueOf(1), tableRecordEntity.getPrimaryKeyValueMap().get("COL01"));
                                break;
                            case 1:
                                // TABLE01の2レコード目

                                // プライマリーキーのハッシュ
                                primaryKeyValueList.add(2);
                                assertEquals(RecordHashEntity.createHashValue(primaryKeyValueList), tableRecordEntity.getPrimaryKeyHashValue());

                                // 全カラム値のハッシュ
                                allColumnValueList.add(2);
                                allColumnValueList.add("Value0002");
                                assertEquals(RecordHashEntity.createHashValue(allColumnValueList), tableRecordEntity.getAllColumnHashValue());

                                // プライマリーキー値
                                assertEquals(1, tableRecordEntity.getPrimaryKeyValueMap().size());
                                assertEquals(BigDecimal.valueOf(2), tableRecordEntity.getPrimaryKeyValueMap().get("COL01"));
                                break;
                            case 2:
                                // TABLE01の3レコード目

                                // プライマリーキーのハッシュ
                                primaryKeyValueList.add(3);
                                assertEquals(RecordHashEntity.createHashValue(primaryKeyValueList), tableRecordEntity.getPrimaryKeyHashValue());

                                // 全カラム値のハッシュ
                                allColumnValueList.add(3);
                                allColumnValueList.add("Value0003");
                                assertEquals(RecordHashEntity.createHashValue(allColumnValueList), tableRecordEntity.getAllColumnHashValue());

                                // プライマリーキー値
                                assertEquals(1, tableRecordEntity.getPrimaryKeyValueMap().size());
                                assertEquals(BigDecimal.valueOf(3), tableRecordEntity.getPrimaryKeyValueMap().get("COL01"));
                                break;
                        }
                        break;

                    case "TABLE02":
                        switch(rowNumber) {
                            case 0:
                                // TABLE02の1レコード目

                                // プライマリーキーのハッシュ
                                primaryKeyValueList.add(1);
                                assertEquals(RecordHashEntity.createHashValue(primaryKeyValueList), tableRecordEntity.getPrimaryKeyHashValue());

                                // 全カラム値のハッシュ
                                allColumnValueList.add(1);
                                allColumnValueList.add("Value0001");
                                allColumnValueList.add("Value0001");
                                allColumnValueList.add(new java.sql.Date(simpleDateFormat.parse("2016/01/01").getTime()));
                                assertEquals(RecordHashEntity.createHashValue(allColumnValueList), tableRecordEntity.getAllColumnHashValue());

                                // プライマリーキー値
                                assertEquals(1, tableRecordEntity.getPrimaryKeyValueMap().size());
                                assertEquals(BigDecimal.valueOf(1), tableRecordEntity.getPrimaryKeyValueMap().get("COL01"));
                                break;
                        }
                        break;

                    case "TABLE03":
                        switch(rowNumber) {
                            case 0:
                                // TABLE03の1レコード目

                                // プライマリーキーのハッシュ
                                primaryKeyValueList.add(1);
                                primaryKeyValueList.add("Value0001");
                                assertEquals(RecordHashEntity.createHashValue(primaryKeyValueList), tableRecordEntity.getPrimaryKeyHashValue());

                                // 全カラム値のハッシュ
                                allColumnValueList.add(1);
                                allColumnValueList.add("Value0001");
                                allColumnValueList.add("Value0001");
                                allColumnValueList.add(new java.sql.Date(simpleDateFormat.parse("2016/01/01").getTime()));
                                assertEquals(RecordHashEntity.createHashValue(allColumnValueList), tableRecordEntity.getAllColumnHashValue());

                                // プライマリーキー値
                                assertEquals(2, tableRecordEntity.getPrimaryKeyValueMap().size());
                                assertEquals(BigDecimal.valueOf(1), tableRecordEntity.getPrimaryKeyValueMap().get("COL01"));
                                assertEquals("Value0001", tableRecordEntity.getPrimaryKeyValueMap().get("COL02"));
                                break;
                        }
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

        dbParser.parseDatabase();
        dbParser.parseTables();
        dbParser.parsePrimaryKey();
        dbParser.parseTableData();

        dbParser.colseDatabase();
    }
/*
    @Test
    public void stringJoinTest() throws Exception {
        List<String> list;

        list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        assertEquals("aaa,bbb,ccc", String.join(",", list));

        list = new ArrayList<>();
        assertEquals("", String.join(",", list));
    }
*/
}

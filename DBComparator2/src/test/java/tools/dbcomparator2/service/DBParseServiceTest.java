package tools.dbcomparator2.service;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.enums.DBCompareStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DBParseServiceTest {
    private static Logger logger = LoggerFactory.getLogger(DBParseServiceTest.class);
    private static ConnectEntity connectEntity1;
    private static ConnectEntity connectEntity2;

    private static final String CREATE_TABLE01 =
            "create table TABLE01 ("+
                    "    COL01 number,"+
                    "    COL02 varchar(100),"+
                    "    primary key(COL01)"+
                    ");";
    private static final String INSERT_TABLE01 = "insert into TABLE01 values(%d, 'TEST%010d')";
    private static final String CREATE_TABLE02 =
            "create table TABLE02 ("+
                    "    COL01 number,"+
                    "    COL02 number,"+
                    "    COL03 varchar(100),"+
                    "    primary key(COL01, COL02)"+
                    ");";
    private static final String INSERT_TABLE02 = "insert into TABLE02 values(%d, %d, 'TEST%010d')";

    @BeforeClass
    public static void beforeClass() throws Exception {
        //UT用H2を作る
        Files.createDirectories(Paths.get(".\\ut\\DBParseServiceTest"));

        Date date = new Date();
        connectEntity1 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url(String.format("jdbc:h2:file:./ut/DBParseServiceTest/testdb1_%tY%tm%td_%tH%tM%tS%tL", date, date, date, date, date, date, date))
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();
        connectEntity2 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url(String.format("jdbc:h2:file:./ut/DBParseServiceTest/testdb2_%tY%tm%td_%tH%tM%tS%tL", date, date, date, date, date, date, date))
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();


        // テストデータ投入
        DBParseNotification dbParseNotification = new DBParseNotificationAdapter();
        DBCompareEntity dbCompareEntity =  DBCompareEntity.builder()
                .connectEntity(connectEntity1)
                .build();

        DBParseService dbParseService = new DBParseService(dbParseNotification, dbCompareEntity);
        dbParseService.connect();

        try (Statement statement = dbParseService.connection.createStatement()) {

            // TABLE01
            statement.execute(CREATE_TABLE01);
            IntStream.range(0, 1000).forEach(i -> {
                try{
                    statement.execute(String.format(INSERT_TABLE01, i, i));
                } catch(Exception e) {
                    e.printStackTrace();
                    fail();
                }
            });

            // TABLE02
            statement.execute(CREATE_TABLE02);
            IntStream.range(0, 1000).forEach(i -> {
                try{
                    String.format(INSERT_TABLE02, i, i, i);
                } catch(Exception e) {
                    e.printStackTrace();
                    fail();
                }
            });

            dbParseService.connection.commit();
        } catch(Exception e) {
            e.printStackTrace();
            fail();
        }

        dbParseService.disconnect();
    }

    @AfterClass
    public static void afterClass() throws Exception {
    }

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void connectTest() {
        DBParseNotification dbParseNotification = new DBParseNotificationAdapter();
        DBCompareEntity dbCompareEntity =  DBCompareEntity.builder()
                .connectEntity(connectEntity1)
                .build();

        DBParseService dbParseService = new DBParseService(dbParseNotification, dbCompareEntity);
        dbParseService.connect();
        assertEquals(DBCompareStatus.CONNECTED, dbCompareEntity.getStatus());
        dbParseService.disconnect();
    }

    @Test
    public void parseTableListTest() {
        DBParseNotification dbParseNotification = new DBParseNotificationAdapter(){
            @Override
            public void parsedTableList(ConnectEntity connectEntity, List<String> tableList) {
                Collections.sort(tableList);
                assertEquals(2, tableList.size());
                assertEquals("TABLE01", tableList.get(0));
                assertEquals("TABLE02", tableList.get(1));
            }
        };

        DBCompareEntity dbCompareEntity =  DBCompareEntity.builder()
                .connectEntity(connectEntity1)
                .build();

        DBParseService dbParseService = new DBParseService(dbParseNotification, dbCompareEntity);
        dbParseService.connect();
        dbParseService.parseTableList();

        assertEquals(DBCompareStatus.SCANNED_TABLE_LIST, dbCompareEntity.getStatus());

        dbParseService.disconnect();
    }

    @Test
    public void countedTableRecordTest() {
        DBParseNotification dbParseNotification = new DBParseNotificationAdapter(){
            @Override
            public void countedTableRecord(ConnectEntity connectEntity, String tableName, int recordCount) {
                assertEquals("TABLE01", tableName);
                assertEquals(1000, recordCount);
            }
        };

        DBCompareEntity dbCompareEntity =  DBCompareEntity.builder()
                .connectEntity(connectEntity1)
                .build();

        DBParseService dbParseService = new DBParseService(dbParseNotification, dbCompareEntity);
        dbParseService.connect();
        dbParseService.parseTableList();
        dbParseService.parseTableData("TABLE01");

        assertEquals(DBCompareStatus.SCAN_FINISHED, dbCompareEntity.getStatus());

        dbParseService.disconnect();
    }

    @Test
    public void parsedPrimaryKeyTest() {
        DBParseNotification dbParseNotification = new DBParseNotificationAdapter(){
            @Override
            public void parsedPrimaryKey(ConnectEntity connectEntity, String tableName, List<String> primaryKeyColumnList) {
                switch (tableName) {
                    case "TABLE01":
                        assertEquals(1, primaryKeyColumnList.size());
                        assertEquals("COL01", primaryKeyColumnList.get(0));
                        break;
                    case "TABLE02":
                        assertEquals(2, primaryKeyColumnList.size());
                        assertEquals("COL01", primaryKeyColumnList.get(0));
                        assertEquals("COL02", primaryKeyColumnList.get(1));
                        break;
                    default:
                        fail();
                }
            }
        };

        DBCompareEntity dbCompareEntity =  DBCompareEntity.builder()
                .connectEntity(connectEntity1)
                .build();

        DBParseService dbParseService = new DBParseService(dbParseNotification, dbCompareEntity);
        dbParseService.connect();
        dbParseService.parseTableList();
        dbParseService.parseTableData("TABLE01");
        dbParseService.parseTableData("TABLE02");

        assertEquals(DBCompareStatus.SCAN_FINISHED, dbCompareEntity.getStatus());

        dbParseService.disconnect();
    }

    @Test
    public void parsedTableRecordTest() {
        DBParseNotification dbParseNotification = new DBParseNotificationAdapter(){
            @Override
            public void parsedTableRecord(ConnectEntity connectEntity, String tableName, RecordHashEntity tableRecordEntity) {
                switch (tableName) {
                    case "TABLE01":
                        assertEquals(1, tableRecordEntity.getPrimaryKeyValueMap().entrySet().size());
                        break;
                    case "TABLE02":
                        assertEquals(2, tableRecordEntity.getPrimaryKeyValueMap().entrySet().size());
                        break;
                    default:
                        fail();
                }
                assertEquals(RecordHashEntity.createHashValue(new ArrayList<>(tableRecordEntity.getPrimaryKeyValueMap().values()))
                        , tableRecordEntity.getPrimaryKeyHashValue());
            }
        };

        DBCompareEntity dbCompareEntity =  DBCompareEntity.builder()
                .connectEntity(connectEntity1)
                .build();

        DBParseService dbParseService = new DBParseService(dbParseNotification, dbCompareEntity);
        dbParseService.connect();
        dbParseService.parseTableList();
        dbParseService.parseTableData("TABLE01");
        dbParseService.parseTableData("TABLE02");

        assertEquals(DBCompareStatus.SCAN_FINISHED, dbCompareEntity.getStatus());

        dbParseService.disconnect();
    }
}

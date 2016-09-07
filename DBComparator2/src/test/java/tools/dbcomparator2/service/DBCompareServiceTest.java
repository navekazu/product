package tools.dbcomparator2.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tools.dbcomparator2.entity.CompareTableRecord;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.enums.CompareType;
import tools.dbcomparator2.enums.RecordCompareStatus;
import tools.dbcomparator2.enums.TableCompareStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DBCompareServiceTest {

    @BeforeClass
    public static void beforeClass() throws Exception {
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
    public void 即時比較の実行() {

        List<ConnectEntity> entityList = new ArrayList<>();
        entityList.add(ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb1/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build());
        entityList.add(ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb2/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build());

        DBCompareService service = new DBCompareService();
        service.updateConnectEntity(entityList);
        service.startCompare();
        assertEquals(true, true);
    }

    @Test
    public void updateConnectEntityのテスト() {
        List<ConnectEntity> entityList = new ArrayList<>();
        ConnectEntity connectEntity1 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb1/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();
        ConnectEntity connectEntity2 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb2/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();
        ConnectEntity connectEntity3 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb3/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();
        ConnectEntity connectEntity4 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb4/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();


        DBCompareService service = new DBCompareService();

        // 2件入れて即時実行の場合 -> 2件とも順番どおり
        entityList.clear();
        entityList.add(connectEntity1);
        entityList.add(connectEntity2);
        service.setCompareType(CompareType.IMMEDIATE);
        service.updateConnectEntity(entityList);
        assertEquals(2, service.dbCompareEntityList.size());
        assertEquals(connectEntity1, service.dbCompareEntityList.get(0).getConnectEntity());
        assertEquals(connectEntity2, service.dbCompareEntityList.get(1).getConnectEntity());

        // 3件入れて即時実行の場合 -> 先頭が消えて2件目以降が入る
        entityList.clear();
        entityList.add(connectEntity1);
        entityList.add(connectEntity2);
        entityList.add(connectEntity3);
        service.setCompareType(CompareType.IMMEDIATE);
        service.updateConnectEntity(entityList);
        assertEquals(2, service.dbCompareEntityList.size());
        assertEquals(connectEntity2, service.dbCompareEntityList.get(0).getConnectEntity());
        assertEquals(connectEntity3, service.dbCompareEntityList.get(1).getConnectEntity());

        // 2件入れて即時実行した後に別の2件を入れて即時実行した場合 -> あとに入れた2件が順番どおり
        entityList.clear();
        entityList.add(connectEntity1);
        entityList.add(connectEntity2);
        service.setCompareType(CompareType.IMMEDIATE);
        service.updateConnectEntity(entityList);
        assertEquals(2, service.dbCompareEntityList.size());
        assertEquals(connectEntity1, service.dbCompareEntityList.get(0).getConnectEntity());
        assertEquals(connectEntity2, service.dbCompareEntityList.get(1).getConnectEntity());
        entityList.clear();
        entityList.add(connectEntity3);
        entityList.add(connectEntity4);
        service.setCompareType(CompareType.IMMEDIATE);
        service.updateConnectEntity(entityList);
        assertEquals(2, service.dbCompareEntityList.size());
        assertEquals(connectEntity3, service.dbCompareEntityList.get(0).getConnectEntity());
        assertEquals(connectEntity4, service.dbCompareEntityList.get(1).getConnectEntity());

        // 1件入れた後に1件追加してピボット実行の場合 -> 2件とも順番どおり
        entityList.clear();
        entityList.add(connectEntity1);
        service.setCompareType(CompareType.PIVOT);
        service.updateConnectEntity(entityList);
        assertEquals(1, service.dbCompareEntityList.size());
        assertEquals(connectEntity1, service.dbCompareEntityList.get(0).getConnectEntity());
        service.addConnectEntity(connectEntity2);
        assertEquals(2, service.dbCompareEntityList.size());
        assertEquals(connectEntity1, service.dbCompareEntityList.get(0).getConnectEntity());
        assertEquals(connectEntity2, service.dbCompareEntityList.get(1).getConnectEntity());

        // そのあと1件追加してピボット実行の場合 -> 1件目は最初に追加したもので、2件目は最後に追加したもの
        service.addConnectEntity(connectEntity3);
        assertEquals(2, service.dbCompareEntityList.size());
        assertEquals(connectEntity1, service.dbCompareEntityList.get(0).getConnectEntity());
        assertEquals(connectEntity3, service.dbCompareEntityList.get(1).getConnectEntity());

        // 1件入れた後に1件追加してローテーション実行の場合 -> 2件とも順番どおり
        entityList.clear();
        entityList.add(connectEntity1);
        service.setCompareType(CompareType.ROTATION);
        service.updateConnectEntity(entityList);
        assertEquals(1, service.dbCompareEntityList.size());
        assertEquals(connectEntity1, service.dbCompareEntityList.get(0).getConnectEntity());
        service.addConnectEntity(connectEntity2);
        assertEquals(2, service.dbCompareEntityList.size());
        assertEquals(connectEntity1, service.dbCompareEntityList.get(0).getConnectEntity());
        assertEquals(connectEntity2, service.dbCompareEntityList.get(1).getConnectEntity());

        // そのあと1件追加してローテーション実行の場合 -> 1件目は2回目に追加したもので、2件目は最後に追加したもの
        service.addConnectEntity(connectEntity3);
        assertEquals(2, service.dbCompareEntityList.size());
        assertEquals(connectEntity2, service.dbCompareEntityList.get(0).getConnectEntity());
        assertEquals(connectEntity3, service.dbCompareEntityList.get(1).getConnectEntity());
    }

    @Test
    public void canRestartableTest() {
        List<ConnectEntity> entityList = new ArrayList<>();
        DBCompareService service = new DBCompareService();

        // 未実行なら再開不可
        assertEquals(false, service.canRestartable());

        ConnectEntity connectEntity1 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb1/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();
        ConnectEntity connectEntity2 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb2/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();

        entityList.add(connectEntity1);
        entityList.add(connectEntity2);
        service.updateConnectEntity(entityList);

        // リストに追加していても、未実行なら再開不可
        assertEquals(false, service.canRestartable());

        // 実行済みなら再開可
        service.startCompare();
        assertEquals(true, service.canRestartable());

    }

    @Test
    public void compareTableRecordListTest_リストに追加する際のテスト() {

        DBCompareService service = new DBCompareService();

        // 初期状態はゼロ件
        assertFalse(service.compareTableRecordList.stream().anyMatch(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())));

        // 追加するとヒットし、ステータスはONE_SIDE_ONLY
        service.addCompareTableRecordList("TEST_TABLE01");
        assertTrue(service.compareTableRecordList.stream().anyMatch(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())));
        List<CompareTableRecord> records1 = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(TableCompareStatus.ONE_SIDE_ONLY, records1.get(0).getTableCompareStatus());

        // 2回目の追加は、ステータスがPAIRになる
        service.addCompareTableRecordList("TEST_TABLE01");
        assertTrue(service.compareTableRecordList.stream().anyMatch(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())));
        List<CompareTableRecord> records2 = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(TableCompareStatus.PAIR, records2.get(0).getTableCompareStatus());
    }

    @Test
    public void compareTableRecordListTest_テーブルのレコード件数更新のテスト() {
        DBCompareService service = new DBCompareService();
        service.addCompareTableRecordList("TEST_TABLE01");
        service.addCompareTableRecordList("TEST_TABLE01");
        service.addCompareTableRecordList("TEST_TABLE02");
        service.addCompareTableRecordList("TEST_TABLE02");

        List<CompareTableRecord> records;

        // TEST_TABLE01 （TEST_TABLE02からの影響はない？）

        // 初期値はゼロ
        records = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(0, records.get(0).getRowCount());

        // 更新したらその値になる
        service.updateCompareTableRecordRowCount("TEST_TABLE01", 10);
        records = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(10, records.get(0).getRowCount());

        // 一度更新した値より小さい値への更新は行えない
        service.updateCompareTableRecordRowCount("TEST_TABLE01", 5);
        records = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(10, records.get(0).getRowCount());

        // 一度更新した値より大きい値への更新は行える
        service.updateCompareTableRecordRowCount("TEST_TABLE01", 15);
        records = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(15, records.get(0).getRowCount());

        // TEST_TABLE02 （TEST_TABLE01からの影響はない？）

        // 初期値はゼロ
        records = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE02".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(0, records.get(0).getRowCount());

        // 更新したらその値になる
        service.updateCompareTableRecordRowCount("TEST_TABLE02", 100);
        records = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE02".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(100, records.get(0).getRowCount());

        // 一度更新した値より小さい値への更新は行えない
        service.updateCompareTableRecordRowCount("TEST_TABLE02", 50);
        records = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE02".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(100, records.get(0).getRowCount());

        // 一度更新した値より大きい値への更新は行える
        service.updateCompareTableRecordRowCount("TEST_TABLE01", 15);
        records = service.compareTableRecordList.stream().filter(compareTableRecord -> "TEST_TABLE01".equals(compareTableRecord.getTableName())).collect(Collectors.toList());
        assertEquals(15, records.get(0).getRowCount());
    }

    @Test
    public void compareTableRecordListTest_値比較のテスト() {
        List<ConnectEntity> entityList = new ArrayList<>();
        ConnectEntity entity1 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb1/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();
        entityList.add(entity1);
        ConnectEntity entity2 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb2/testdb")
                .user("sa")
                .password(null)
                .schema("PUBLIC")
                .build();
        entityList.add(entity2);

        DBCompareService service = new DBCompareService();
        service.updateConnectEntity(entityList);
        service.setCompareTableRecordList(new ArrayList<>());

        service.addCompareTableRecordList("TEST_TABLE01");
        service.addCompareTableRecordList("TEST_TABLE02");

        List<String> tableList1 = new ArrayList<>();
        List<String> tableList2 = new ArrayList<>();

        tableList1.add("TEST_TABLE01");
        tableList1.add("TEST_TABLE02");
        tableList2.add("TEST_TABLE01");
        tableList2.add("TEST_TABLE02");

        service.parsedTableList(entity1, tableList1);
        service.parsedTableList(entity2, tableList2);

        service.updateCompareTableRecordRowCount("TEST_TABLE01", 10);
        service.updateCompareTableRecordRowCount("TEST_TABLE02", 10);

        RecordHashEntity tableRecordEntity1_01 = RecordHashEntity.builder()
                .primaryKeyHashValue("A1")
                .allColumnHashValue("B1")
                .build();
        RecordHashEntity tableRecordEntity1_02 = RecordHashEntity.builder()
                .primaryKeyHashValue("A2")
                .allColumnHashValue("B2")
                .build();
        RecordHashEntity tableRecordEntity2_01 = RecordHashEntity.builder()
                .primaryKeyHashValue("A1")
                .allColumnHashValue("B1")
                .build();
        RecordHashEntity tableRecordEntity2_02 = RecordHashEntity.builder()
                .primaryKeyHashValue("A2")
                .allColumnHashValue("B2_")
                .build();

        service.parsedTableRecord(entity1, "TEST_TABLE01", 0, tableRecordEntity1_01);
        assertEquals(2, service.compareTableRecordList.size());
        assertEquals("TEST_TABLE01", service.compareTableRecordList.get(0).getTableName());
        assertEquals(1, service.compareTableRecordList.get(0).getPrimaryKeyHashValueMap().size());
        assertEquals(RecordCompareStatus.ONE_SIDE_ONLY, service.compareTableRecordList.get(0).getPrimaryKeyHashValueMap().get("A1"));

        service.parsedTableRecord(entity2, "TEST_TABLE01", 0, tableRecordEntity2_01);
        assertEquals(RecordCompareStatus.EQUALITY, service.compareTableRecordList.get(0).getPrimaryKeyHashValueMap().get("A1"));

        service.parsedTableRecord(entity1, "TEST_TABLE02", 0, tableRecordEntity1_02);
        service.parsedTableRecord(entity2, "TEST_TABLE02", 0, tableRecordEntity2_02);
        assertEquals(2, service.compareTableRecordList.size());
        assertEquals("TEST_TABLE02", service.compareTableRecordList.get(1).getTableName());
        assertEquals(1, service.compareTableRecordList.get(1).getPrimaryKeyHashValueMap().size());
        assertEquals(RecordCompareStatus.EQUALITY, service.compareTableRecordList.get(1).getPrimaryKeyHashValueMap().get("A2"));
//        service.parsedTableRecord(entity1, "TEST_TABLE01", 0, tableRecordEntity1_02);
//        service.parsedTableRecord(entity1, "TEST_TABLE02", 0, tableRecordEntity2_01);
//        service.parsedTableRecord(entity1, "TEST_TABLE02", 0, tableRecordEntity2_02);
    }
}

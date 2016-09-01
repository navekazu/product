package tools.dbcomparator2.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.enums.CompareType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
}

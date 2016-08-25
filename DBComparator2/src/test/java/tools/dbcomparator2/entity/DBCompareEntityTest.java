package tools.dbcomparator2.entity;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DBCompareEntityTest {

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
    public void tableCompareEntityMapフィールドの生き死にテスト() {
        // インスタンス生成直後はnullか？
        DBCompareEntity dbCompareEntity = DBCompareEntity.builder().build();
        assertNull(dbCompareEntity.getTableCompareEntityMap());

        // TableCompareEntityをaddすると、addしたTableCompareEntityのテーブル名をキーにして格納しているか？
        TableCompareEntity tableCompareEntity = TableCompareEntity.builder()
                .tableName("TABLE01")
                .build();
        dbCompareEntity.addTableCompareEntity(tableCompareEntity);
        assertNotNull(dbCompareEntity.getTableCompareEntityMap());
        assertEquals(1, dbCompareEntity.getTableCompareEntityMap().size());
        assertEquals(tableCompareEntity, dbCompareEntity.getTableCompareEntityMap().get("TABLE01"));
        assertEquals(tableCompareEntity, dbCompareEntity.getTableCompareEntity("TABLE01"));         // mapから取得するのと同じ結果か？
    }
}

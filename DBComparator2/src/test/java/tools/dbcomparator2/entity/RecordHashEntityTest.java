package tools.dbcomparator2.entity;

import org.junit.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecordHashEntityTest {

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
    public void ハッシュ値のテスト() {
        List<String> list = new ArrayList<>();
        list.add("test");

        // ハッシュ値は下記サイトで作成
        // http://www.convertstring.com/ja/Hash/SHA512
        assertEquals("EE26B0DD4AF7E749AA1A8EE3C10AE9923F618980772E473F8819A5D4940E0DB27AC185F8A0E1D5F84F88BC887FD67B143732C304CC5FA9AD8E6F57F50028A8FF".toLowerCase()
                , RecordHashEntity.createHashValue((List) list));
    }

    @Test
    public void primaryKeyValueMapフィールドの生き死にテスト() {
        // インスタンス生成直後はnullか？
        RecordHashEntity recordHashEntity = RecordHashEntity.builder().build();
        assertNull(recordHashEntity.getPrimaryKeyValueMap());

        // putするとprimaryKeyValueMapのインスタンスが作られるか？
        String value = "value01";
        recordHashEntity.putPrimaryKeyValueMap("col01", value);
        assertNotNull(recordHashEntity.getPrimaryKeyValueMap());
        assertEquals(1, recordHashEntity.getPrimaryKeyValueMap().size());
        assertEquals(value, recordHashEntity.getPrimaryKeyValueMap().get("col01"));

    }
}

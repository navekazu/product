package tools.dbcomparator2;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tools.dbcomparator2.entity.ConnectEntity;

import static org.junit.Assert.assertEquals;

public class EntityTest {

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
    public void test() {
        ConnectEntity e = ConnectEntity.builder()
                .library("a")
                .driver("b")
                .url("c")
                .user("d")
                .password("e")
                .build();

        // eを基にbuilderを作って、必要な箇所だけ更新
        ConnectEntity e2 = ConnectEntity.copyBuilder(e)
                .user("f")
                .build();

        assertEquals("a", e2.getLibrary());
        assertEquals("b", e2.getDriver());
        assertEquals("c", e2.getUrl());
        assertEquals("f", e2.getUser());
        assertEquals("e", e2.getPassword());
    }
}

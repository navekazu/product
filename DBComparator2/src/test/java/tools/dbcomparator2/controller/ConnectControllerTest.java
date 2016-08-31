package tools.dbcomparator2.controller;

import org.junit.*;

import static org.junit.Assert.assertEquals;

public class ConnectControllerTest {
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
    public void backslashToSlashTest() {
        // 単体の「\」を「/」に変換できるか？
        assertEquals("/", ConnectController.backslashToSlash("\\"));

        // 連続した「\」を「/」に変換できるか？
        assertEquals("//", ConnectController.backslashToSlash("\\\\"));

        // 文字列内の「\」を「/」に変換できるか？
        assertEquals("jdbc:sqlite:D:/aaa/bbb/ccc.db", ConnectController.backslashToSlash("jdbc:sqlite:D:\\aaa\\bbb\\ccc.db"));

        // 「/」は「/」のままか？
        assertEquals("/", ConnectController.backslashToSlash("/"));
    }
}

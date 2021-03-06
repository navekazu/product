package tools.dbconnector6.mapper;

import org.junit.*;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class MapperBaseTest {
    @BeforeClass
    public static void beforeClass() throws Exception {
        MapperBase.setUtMode(true);
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
    public void blankToOneSpaceのテスト() {
        ConnectMapper connectMapper = new ConnectMapper();
        assertEquals(connectMapper.blankToOneSpace(""), " ");
        assertEquals(connectMapper.blankToOneSpace(" "), " ");
        assertEquals(connectMapper.blankToOneSpace("\t"), " ");
        assertEquals(connectMapper.blankToOneSpace("\n"), " ");
        assertEquals(connectMapper.blankToOneSpace("a"), "a");
    }

    @Test
    public void getArchiveFilePathのテスト() throws IOException {
        ConnectMapper connectMapper = new ConnectMapper();
        assertEquals(Paths.get(System.getProperty("user.home"), ".DBConnector6", "config", connectMapper.getArchiveFileName()+"_test")
                , connectMapper.getArchiveFilePath());
    }

}

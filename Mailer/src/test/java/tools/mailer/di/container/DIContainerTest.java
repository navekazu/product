package tools.mailer.di.container;

import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class DIContainerTest {
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
    public void test() throws Exception {
        DIContainer diContainer = new DIContainer();
        diContainer.loadPlugin();
        assertTrue(true);
    }
    @Test
    public void getLoadedJarFilesTest() throws Exception {
        DIContainer diContainer = new DIContainer();
        List<String> jars = diContainer.getLoadedJarFiles();
        jars.forEach(System.out::println);
        assertTrue(true);
    }
}

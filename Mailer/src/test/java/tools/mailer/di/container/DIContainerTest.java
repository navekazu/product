package tools.mailer.di.container;

import org.junit.*;

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
}

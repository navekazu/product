package tools.githubclient;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.junit.*;

import static org.junit.Assert.assertTrue;

public class EgitTest {
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
    public void authenticationTest() throws Exception {
        GitHubClient client = new GitHubClient();
        client.setCredentials("navekazu", "passw0rd");

        assertTrue(true);
    }

    @Test
    public void cloneRepositoryTest() throws Exception {

        RepositoryService service = new RepositoryService();
//        service.getRepositories("navekazu").stream()
//                .forEach(System.out::println);
        assertTrue(true);
    }

}

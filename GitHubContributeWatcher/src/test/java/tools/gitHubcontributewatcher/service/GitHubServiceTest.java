package tools.githubcontributewatcher.service;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class GitHubServiceTest {
    @Test
    public void getContributeListTest() throws Exception {
        GitHubService service = new GitHubService();
        List<String> list = service.getContributeList();

        assertNotEquals(0, list.size());
    }
}

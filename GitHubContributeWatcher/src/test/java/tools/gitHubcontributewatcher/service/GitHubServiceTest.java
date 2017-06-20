package tools.githubcontributewatcher.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class GitHubServiceTest {
    @Autowired
    private GitHubService service;

    @Test
    public void getContributeListTest() throws Exception {
        service = new GitHubService();
        List<String> list = service.getContributeList();

        assertNotEquals(0, list.size());
    }
}

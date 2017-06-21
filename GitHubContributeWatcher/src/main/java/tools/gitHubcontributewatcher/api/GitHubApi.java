package tools.githubcontributewatcher.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import tools.githubcontributewatcher.service.GitHubService;

import java.util.List;

@RestController
public class GitHubApi {
    @Autowired
    private GitHubService gitHubService;

    public List<String> getContributeList() {
        return null;
    }
}

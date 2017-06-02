package tools.githubcontributewatcher.service;

import org.springframework.stereotype.Service;
import tools.githubcontributewatcher.GitHubHandler;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubService {
    public List<String> getContributeList() {
        List<String> list = new ArrayList<>();
        GitHubHandler handler = new GitHubHandler();

        return list;
    }
}

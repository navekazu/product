package tools.githubcontributewatcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import tools.githubcontributewatcher.service.GitHubService;

@Controller
public class MainController {
    @Autowired 
    private GitHubService gitHubService;
}

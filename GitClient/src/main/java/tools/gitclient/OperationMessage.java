package tools.gitclient;

import java.awt.Frame;
import java.io.File;
import java.net.URL;
import java.util.List;

import tools.gitclient.config.CredentialsConfigManager;
import tools.gitclient.config.RepositoryCredentialsConfigManager.RepositoryCredentials;

public interface OperationMessage {
    public Frame getMainFrame();

    public void openRepository(File local);
    public void cloneRepository(URL remote);
    public void startLocalRepository(File local);

    public void addRecentOpenRepository(File local);
    public File getRecentOpenRepository();
    public List<File> getRecentOpenRepositoryList();

    public void addOpeningRepository(File local);
    public List<File> getOpeningRepositoryList();

    public List<CredentialsConfigManager.Credentials> getCredentialsConfig();
    public void setCredentialsConfig(List<CredentialsConfigManager.Credentials> list);
    public void updateCredencialsConfig();

    public RepositoryCredentials getRepositoryCredentials(String repository);
    public void setRepositoryCredentials(RepositoryCredentials repositoryCredentials);
    public void clearRepositoryCredentials(String repository);
}

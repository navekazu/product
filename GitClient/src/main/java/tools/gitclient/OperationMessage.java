package tools.gitclient;

import java.awt.Frame;
import java.io.File;
import java.net.URL;
import java.util.List;

import tools.gitclient.config.CredentialsConfigManager;

public interface OperationMessage {
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

    public Frame getMainFrame();
}

package tools.gitclient;

import java.io.File;
import java.net.URL;
import java.util.List;

public interface OperationMessage {
    public void openRepository(File local);
    public void cloneRepository(URL remote);
    public void startLocalRepository(File local);

    public void addRecentOpenRepository(File local);
    public File getRecentOpenRepository();
    public List<File> getRecentOpenRepositoryList();

    public void addOpeningRepository(File local);
    public List<File> getOpeningOpenRepositoryList();
}

package tools.gitclient;

import java.io.File;
import java.net.URL;

public interface OperationMessage {
    public void openRepository(File local);
    public void cloneRepository(URL remote);
    public void startLocalRepository(File local);
}

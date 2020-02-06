package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import tools.gitclient.OperationMessage;

public class RepositoryTab extends Container {
    private OperationMessage operationMessage;
    private Repository repository;
    private File repositoryPath;

    private static final String GIT_CONF_DIR = ".git";

    public RepositoryTab(OperationMessage operationMessage) {
        this.operationMessage = operationMessage;
        createContents();
    }

    private void createContents() {
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createBranchPanel(), createBranchPanel());
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        add(splitPane, BorderLayout.CENTER);
    }

    private Container createBranchPanel() {
        return new JPanel();
    }

    public void openRepository(File local) {
        if (GIT_CONF_DIR.equals(local.getName())) {
            local = new File(local, GIT_CONF_DIR);
        }
        repositoryPath = local;

        try {
            repository = new FileRepositoryBuilder()
                    .setGitDir(local)
                    .build();
            operationMessage.addRecentOpenRepository(local);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

    }

    public String getRepositoryName() {
//        try {
//            if (repository!=null) {
//                return repository.getRefDatabase().toString();
//            }
//        } catch (IOException e) {
//            // TODO 自動生成された catch ブロック
//            e.printStackTrace();
//        }

        if (repositoryPath==null) {
            return "empty";
        }

        if (GIT_CONF_DIR.equals(repositoryPath.getName())) {
            return repositoryPath.getParentFile().getName();
        }

        return repositoryPath.getName();
    }
}

package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;

import tools.gitclient.OperationMessage;
import tools.gitclient.config.CredentialsConfigManager;
import tools.gitclient.config.CredentialsConfigManager.Credentials;
import tools.gitclient.config.RepositoryCredentialsConfigManager.RepositoryCredentials;

public class RepositoryTab extends Container implements RepositoryTabOperationMessage {
    private OperationMessage operationMessage;
    private Repository repository;
    private File repositoryPath;
    private JLabel repositoryNameLabel;
    private JLabel repositoryPathLabel;
    private BranchPanel branchPanel;
    private CommitPanel commitPanel;
    private CredentialsProvider credentialsProvider;

    private JComboBox<String> credentialsComboBox;

    static final String GIT_CONF_DIR = ".git";
    static final String LOCAL_BRANCH_PREFIX = "refs/heads/";
    static final String REMOTE_BRANCH_PREFIX = "refs/remotes/origin/";

    public RepositoryTab(OperationMessage operationMessage) {
        this.operationMessage = operationMessage;
        this.credentialsProvider = null;
        createContents();
    }

    private void createContents() {
        setLayout(new BorderLayout());

        createRepositoryLabel();
        add(createToolbar(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createBranchPanel(), createMainPanel());
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        add(splitPane, BorderLayout.CENTER);
    }

    private Container createRepositoryLabel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        repositoryNameLabel = new JLabel();
        panel.add(repositoryNameLabel);

        repositoryPathLabel = new JLabel();
        panel.add(repositoryPathLabel);
        Font font = repositoryPathLabel.getFont();
        font = new Font(font.getFontName(), Font.PLAIN, font.getSize());
        repositoryPathLabel.setFont(font);

        return panel;
    }

    private Container createToolbar() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JToolBar toolBar = new JToolBar();
        panel.add(toolBar);

        JButton fetchButton = new JButton("FETCH");
        toolBar.add(fetchButton);
        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onFetchButton();
            }
        });

        JButton pullButton = new JButton("PULL");
        toolBar.add(pullButton);
        pullButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onPullButton();
            }
        });

        toolBar.addSeparator();

        JButton pushButton = new JButton("PUSH");
        toolBar.add(pushButton);
        pushButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onPushButton();
            }
        });

        toolBar.addSeparator();

        credentialsComboBox = new JComboBox<>();
        toolBar.add(credentialsComboBox);

        return panel;
    }

    private Container createBranchPanel() {
        branchPanel = new BranchPanel(operationMessage, this);
        return branchPanel;
    }

    private Container createMainPanel() {
        commitPanel = new CommitPanel(operationMessage, this);
        return commitPanel;
    }

    public void reopenRepository(File local) {
        openRepository(local, false);
    }

    public void openRepository(File local) {
        openRepository(local, true);
    }

    private void openRepository(File local, boolean addOpeningRepository) {
        if (GIT_CONF_DIR.equals(local.getName())) {
            local = local.getParentFile();
        }
        repositoryPath = local;
        setRepositoryLabel(local);

        try {
            repository = new FileRepositoryBuilder()
                    .setGitDir(new File(local, GIT_CONF_DIR))
                    .build();

            branchPanel.updateLocalBranchList(repositoryPath);
            branchPanel.updateRemoteBranchList(repositoryPath);
            branchPanel.expandBranchTree();
            commitPanel.updateStage();

            updateCredencialsConfig();

            operationMessage.addRecentOpenRepository(local);

            if (addOpeningRepository) {
                operationMessage.addOpeningRepository(local);
            }
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

    }

    private void setRepositoryLabel(File local) {
        repositoryNameLabel.setText(local.getName());
        repositoryPathLabel.setText(local.getPath());
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

    private void onFetchButton() {
        try (Git git = Git.open(repositoryPath)) {
            FetchCommand fetch = git.fetch();
            FetchResult result = fetch.setProgressMonitor(new ProgressMonitorPane("FETCH", operationMessage))
                                    .setCredentialsProvider(credentialsProvider).call();
            List<RefSpec> l = fetch.getRefSpecs();
            for (RefSpec s: l) {
                String ss = s.getSource();
            }

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    private void onPullButton() {
        try (Git git = Git.open(repositoryPath)) {
            PullCommand pull = git.pull();
            pull.setProgressMonitor(new ProgressMonitorPane("PULL", operationMessage))
                .setCredentialsProvider(credentialsProvider).call();

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    private void onPushButton() {
        try (Git git = Git.open(repositoryPath)) {
            PushCommand push = git.push();
            push.setProgressMonitor(new ProgressMonitorPane("PUSH", operationMessage))
                .setCredentialsProvider(credentialsProvider).call();

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    public void updateCredencialsConfig() {
        for (ActionListener actionListener: credentialsComboBox.getActionListeners()) {
            credentialsComboBox.removeActionListener(actionListener);
        }

        credentialsComboBox.removeAllItems();
        credentialsComboBox.addItem("None credentials");

        List<Credentials> credentialsList = operationMessage.getCredentialsConfig();
        if (credentialsList!=null) {
            credentialsList.stream()
                .forEach(c -> credentialsComboBox.addItem(c.name));
        }

        credentialsComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onCredentialsComboBoxStateChanged();
            }
        });

        credentialsProvider = null;
        if (repositoryPath==null) {
            return;
        }

        RepositoryCredentials rc = operationMessage.getRepositoryCredentials(repositoryPath.getPath());
        if (rc==null) {
            return ;
        }

        if (credentialsList!=null) {
            for (Credentials c: credentialsList) {
                if (rc.credentials==c.no) {
                    credentialsProvider = CredentialsConfigManager.createCredentialsProvider(c);
                }
            }
        }

        for (int index=0; index<credentialsList.size(); index++) {
            Credentials c = credentialsList.get(index);
            if (rc.credentials==c.no) {
                credentialsComboBox.setSelectedIndex(index+1);
            }
        }
    }

    private void onCredentialsComboBoxStateChanged() {
        if (repositoryPath==null) {
            return;
        }

        if (credentialsComboBox.getSelectedIndex()==0) {
            operationMessage.clearRepositoryCredentials(repositoryPath.getPath());
            return;
        }

        RepositoryCredentials rc = operationMessage.getRepositoryCredentials(repositoryPath.getPath());
        if (rc==null) {
            rc = new RepositoryCredentials();
            rc.repository = repositoryPath.getPath();
        }

        List<Credentials> credentialsList = operationMessage.getCredentialsConfig();
        rc.credentials = credentialsList.get(credentialsComboBox.getSelectedIndex()-1).no;
        operationMessage.setRepositoryCredentials(rc);
    }

    @Override
    public File getRepository() {
        return repositoryPath;
    }
}

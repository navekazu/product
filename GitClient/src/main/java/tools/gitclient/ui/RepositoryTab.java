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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.UserConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;

import tools.gitclient.OperationMessage;
import tools.gitclient.config.CredentialsConfigManager.Credentials;

public class RepositoryTab extends Container {
    private OperationMessage operationMessage;
    private Repository repository;
    private File repositoryPath;
    private JLabel repositoryNameLabel;
    private JLabel repositoryPathLabel;
    private JTree branchTree;
    private DefaultMutableTreeNode localBranchNode;
    private DefaultMutableTreeNode remoteBranchNode;
    private CredentialsProvider credentialsProvider;

    private JComboBox<String> credentialsComboBox;

    // stage
    private JButton addAllButton;

    // commit
    private JTextField summaryField;
    private JTextArea descriptionField;
    private JButton commitButton;

    private static final String GIT_CONF_DIR = ".git";
    private static final String LOCAL_BRANCH_PREFIX = "refs/heads/";
    private static final String REMOTE_BRANCH_PREFIX = "refs/remotes/origin/";

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

        JButton configButton = new JButton("CONF");
        toolBar.add(configButton);
        configButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onConfigButton();
            }
        });

        credentialsComboBox = new JComboBox<>();
        updateCredencialsConfig();
        toolBar.add(credentialsComboBox);

        return panel;
    }

    private Container createBranchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
        branchTree = new JTree(rootNode);
//        branchTree.setRootVisible(false);

        localBranchNode = new DefaultMutableTreeNode("Local branch");
        rootNode.add(localBranchNode);

        remoteBranchNode = new DefaultMutableTreeNode("Remote branch");
        rootNode.add(remoteBranchNode);

        JScrollPane scroll = new JScrollPane(branchTree);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private Container createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        addAllButton = new JButton("Add all");
        addAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onAddAllButton();
            }
        });
        panel.add(addAllButton, BorderLayout.NORTH);

        panel.add(createCommitPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private Container createCommitPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        summaryField = new JTextField();
        panel.add(summaryField, BorderLayout.NORTH);

        descriptionField = new JTextArea();
        descriptionField.setRows(5);
        panel.add(descriptionField, BorderLayout.CENTER);

        commitButton = new JButton("Commit");
        commitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onCommitButton();
            }
        });
        panel.add(commitButton, BorderLayout.SOUTH);

        return panel;
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

            updateLocalBranchList();
            updateRemoteBranchList();

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

    private void updateLocalBranchList() throws IOException, GitAPIException {
        try (Git git = Git.open(repositoryPath)) {
            List<Ref> list = git.branchList().call();

            removeAllClidNode(localBranchNode);

            for (Ref ref: list) {
                String name = ref.getName();
                name = name.substring(LOCAL_BRANCH_PREFIX.length());
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
                localBranchNode.add(node);
            }
        }
    }
    private void updateRemoteBranchList() throws IOException, GitAPIException {
        try (Git git = Git.open(repositoryPath)) {
            List<Ref> list = git.branchList().setListMode(ListMode.REMOTE).call();

            removeAllClidNode(remoteBranchNode);

            for (Ref ref: list) {
                String name = ref.getName();
                name = name.substring(REMOTE_BRANCH_PREFIX.length());
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
                remoteBranchNode.add(node);
            }
        }
    }

    private void removeAllClidNode(DefaultMutableTreeNode node) {
        while (node.getChildCount()!=0) {
            node.remove(0);
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

    private void onAddAllButton() {
/*
        Git git = new Git(repository);
        AddCommand add = git.add();
        add.addFilepattern("*");

        try {
            add.call();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
*/
        try (Git git = Git.open(repositoryPath)) {
            AddCommand add = git.add();
            add.addFilepattern(".").call();

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    private void onCommitButton() {
        String message = summaryField.getText();


        if (descriptionField.getText().length()!=0) {
            message += "\n\n";
            message += descriptionField.getText();
        }

        try (Git git = Git.open(repositoryPath)) {
            Config config = git.getRepository().getConfig();
            String authorName = config.get(UserConfig.KEY).getAuthorName();
            String authorEmail = config.get(UserConfig.KEY).getAuthorEmail();

            CommitCommand commit = git.commit();
            commit.setMessage(message).setAuthor(authorName, authorEmail).call();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO 自動生成された catch ブロック
            e1.printStackTrace();
        }

    }

    private void onFetchButton() {
        try (Git git = Git.open(repositoryPath)) {
            FetchCommand fetch = git.fetch();
            fetch.setCredentialsProvider(credentialsProvider).call();

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
            pull.setCredentialsProvider(credentialsProvider).call();

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
            push.setCredentialsProvider(credentialsProvider).call();

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    private void onConfigButton() {
        ConfigDialog dialog = new ConfigDialog(operationMessage);
        dialog.pack();
        dialog.setVisible(true);

        if (dialog.getConfigResult().isOK) {
            credentialsProvider = dialog.getConfigResult().credentialsProvider;
        }
    }

    public void updateCredencialsConfig() {
        credentialsComboBox.removeAllItems();
        credentialsComboBox.addItem("None credentials");

        List<Credentials> credentialsList = operationMessage.getCredentialsConfig();
        if (credentialsList!=null) {
            credentialsList.stream()
                .forEach(c -> credentialsComboBox.addItem(c.name));
        }

    }
}

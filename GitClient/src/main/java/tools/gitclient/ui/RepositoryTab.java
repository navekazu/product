package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import tools.gitclient.OperationMessage;

public class RepositoryTab extends Container {
    private OperationMessage operationMessage;
    private Repository repository;
    private File repositoryPath;
    private JLabel repositoryNameLabel;
    private JLabel repositoryPathLabel;

    // stage
    private JButton addAllButton;

    // commit
    private JTextField summaryField;
    private JTextArea descriptionField;
    private JButton commitButton;

    private static final String GIT_CONF_DIR = ".git";

    public RepositoryTab(OperationMessage operationMessage) {
        this.operationMessage = operationMessage;
        createContents();
    }

    private void createContents() {
        setLayout(new BorderLayout());

        add(createRepositoryLabel(), BorderLayout.NORTH);

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

    private Container createBranchPanel() {
        return new JPanel();
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
            local = new File(local, GIT_CONF_DIR);
        }
        repositoryPath = local;
        setRepositoryLabel(local);

        try {
            repository = new FileRepositoryBuilder()
                    .setGitDir(local)
                    .build();

            operationMessage.addRecentOpenRepository(local);

            if (addOpeningRepository) {
                operationMessage.addOpeningRepository(local);
            }
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

    }

    private void setRepositoryLabel(File local) {
        if (GIT_CONF_DIR.equals(local.getName())) {
            local = local.getParentFile();
        }
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
        try (Git git = Git.open(new File(repositoryPathLabel.getText()))) {
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

        try (Git git = Git.open(new File(repositoryPathLabel.getText()))) {
            CommitCommand commit = git.commit();
            commit.setMessage(message).call();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO 自動生成された catch ブロック
            e1.printStackTrace();
        }

    }
}

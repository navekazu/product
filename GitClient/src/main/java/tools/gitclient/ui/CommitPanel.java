package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.UserConfig;

import tools.gitclient.OperationMessage;

public class CommitPanel extends JPanel {
    private OperationMessage operationMessage;
    private RepositoryTabOperationMessage repositoryTabOperationMessage;

    // stage
    private JButton addAllButton;
    private JList stagedList;
    private JList notStageList;

    // commit
    private JTextField summaryField;
    private JTextArea descriptionField;
    private JButton commitButton;

    public CommitPanel(OperationMessage operationMessage, RepositoryTabOperationMessage repositoryTabOperationMessage) {
        this.operationMessage = operationMessage;
        this.repositoryTabOperationMessage = repositoryTabOperationMessage;
        createContents();
    }

    private void createContents() {
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createStage(), createLog());
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);
    }

    private Container createStage() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        addAllButton = new JButton("Add all");
        addAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onAddAllButton();
            }
        });
        panel.add(addAllButton, BorderLayout.NORTH);

        // JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        //      createStagedFileList(), createLog());
        // splitPane.setOneTouchExpandable(true);

        panel.add(createCommitPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private Container createLog() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

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
        try (Git git = Git.open(repositoryTabOperationMessage.getRepository())) {
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

        try (Git git = Git.open(repositoryTabOperationMessage.getRepository())) {
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
}

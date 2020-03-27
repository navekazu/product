package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.UserConfig;

import tools.gitclient.OperationMessage;

public class CommitPanel extends JPanel {
    private OperationMessage operationMessage;
    private RepositoryTabOperationMessage repositoryTabOperationMessage;

    // stage
    private JList<String> stagedList;
    private DefaultListModel<String> stagedListModel;
    private JList<String> notStageList;
    private DefaultListModel<String> notStageListModel;
    private JButton unstageButton;
    private JButton unstageAllButton;
    private JButton stageButton;
    private JButton stageAllButton;

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

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
             createStagedFileList(), createNotStageFileList());
        splitPane.setOneTouchExpandable(true);
        panel.add(splitPane, BorderLayout.CENTER);

        panel.add(createCommitPanel(), BorderLayout.SOUTH);

        return panel;
    }

    public void updateStage() {
        try (Git git = Git.open(repositoryTabOperationMessage.getRepository())) {
            Status status = git.status().call();
            Set<String> added = status.getAdded();
            Set<String> changed = status.getChanged();
            Set<String> conflicting = status.getConflicting();
            // Set<String> ignoredNotInIndex = status.getIgnoredNotInIndex();
            Set<String> missing = status.getMissing();
            Set<String> modified = status.getModified();
            Set<String> removed = status.getRemoved();
            Set<String> uncommittedChanges = status.getUncommittedChanges();
             Set<String> untracked = status.getUntracked();
            // Set<String> untrackedFolders = status.getUntrackedFolders();

            stagedListModel.removeAllElements();
            changed.stream().forEach(s -> stagedListModel.addElement(s));

            notStageListModel.removeAllElements();
            added.stream().forEach(s -> notStageListModel.addElement(s));
            conflicting.stream().forEach(s -> notStageListModel.addElement(s));
            missing.stream().forEach(s -> notStageListModel.addElement(s));
            modified.stream().forEach(s -> notStageListModel.addElement(s));
            removed.stream().forEach(s -> notStageListModel.addElement(s));
            // uncommittedChanges.stream().forEach(s -> notStageListModel.addElement(s));
            untracked.stream().forEach(s -> notStageListModel.addElement(s));

        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        changeStageComponent();
    }

    private void changeStageComponent() {
        unstageButton.setEnabled(false);
        unstageAllButton.setEnabled(false);
        stageButton.setEnabled(false);
        stageAllButton.setEnabled(false);

        if (stagedList.getSelectedIndex()!=-1) {
            unstageButton.setEnabled(true);
        }
        if (stagedList.getModel().getSize()!=0) {
            unstageAllButton.setEnabled(true);
        }

        if (notStageList.getSelectedIndex()!=-1) {
            stageButton.setEnabled(true);
        }
        if (notStageList.getModel().getSize()!=0) {
            stageAllButton.setEnabled(true);
        }
    }

    private Container createStagedFileList() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(new JLabel("Staged Files"), BorderLayout.WEST);

        unstageButton = new JButton("Unstage");
        unstageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onUnstageButton();
            }
        });

        unstageAllButton = new JButton("Unstage All");
        unstageAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onUnstageAllButton();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(unstageButton);
        buttonPanel.add(unstageAllButton);
        titlePanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);

        stagedListModel = new DefaultListModel<>();
        stagedList = new JList<>(stagedListModel);
        stagedList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                onStagedListValueChanged();
            }

        });
        JScrollPane scroll = new JScrollPane(stagedList);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private Container createNotStageFileList() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(new JLabel("Not Stage Files"), BorderLayout.WEST);

        stageButton = new JButton("Stage");
        stageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onStageButton();
            }
        });

        stageAllButton = new JButton("Stage All");
        stageAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onStageAllButton();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stageButton);
        buttonPanel.add(stageAllButton);
        titlePanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);

        notStageListModel = new DefaultListModel<>();
        notStageList = new JList<>(notStageListModel);
        notStageList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                onNotStagedListValueChanged();
            }

        });
        JScrollPane scroll = new JScrollPane(notStageList);
        panel.add(scroll, BorderLayout.CENTER);

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

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        summaryField = new JTextField();
        northPanel.add(new JLabel("Commit"), BorderLayout.NORTH);
        northPanel.add(summaryField, BorderLayout.SOUTH);
        panel.add(northPanel, BorderLayout.NORTH);

        descriptionField = new JTextArea();
        descriptionField.setRows(5);
        panel.add(descriptionField, BorderLayout.CENTER);

        commitButton = new JButton("Commit Message");
        commitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onCommitButton();
            }
        });
        panel.add(commitButton, BorderLayout.SOUTH);

        return panel;
    }

    private void onUnstageButton() {
        List<String> list = stagedList.getSelectedValuesList();
        if (list.size()==0) {
            return;
        }

        try (Git git = Git.open(repositoryTabOperationMessage.getRepository())) {
            ResetCommand reset = git.reset();
            for (String s: list) {
                reset.addPath(s).call();
            }
            updateStage();

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }
    private void onUnstageAllButton() {
        if (stagedListModel.size()==0) {
            return;
        }

        try (Git git = Git.open(repositoryTabOperationMessage.getRepository())) {
            ResetCommand reset = git.reset();
            for (int index=0; index<stagedListModel.size(); index++) {
                reset.addPath(stagedListModel.get(index));
            }
            reset.call();
            updateStage();

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }
    private void onStageButton() {
        List<String> list = notStageList.getSelectedValuesList();
        if (list.size()==0) {
            return;
        }

        try (Git git = Git.open(repositoryTabOperationMessage.getRepository())) {
            AddCommand add = git.add();
            for (String s: list) {
                add.addFilepattern(s).call();
            }
            updateStage();

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }
    private void onStageAllButton() {
        if (notStageListModel.size()==0) {
            return;
        }

        try (Git git = Git.open(repositoryTabOperationMessage.getRepository())) {
            AddCommand add = git.add();
            add.addFilepattern(".").call();
            updateStage();

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }
    private void onStagedListValueChanged() {
        notStageList.setSelectedIndices(new int[] {});
        changeStageComponent();
    }
    private void onNotStagedListValueChanged() {
        stagedList.setSelectedIndices(new int[] {});
        changeStageComponent();

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
            updateStage();

        } catch (GitAPIException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO 自動生成された catch ブロック
            e1.printStackTrace();
        }

    }
}

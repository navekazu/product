package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import tools.gitclient.OperationMessage;

public class BranchPanel extends JPanel {
    private OperationMessage operationMessage;
    private RepositoryTabOperationMessage repositoryTabOperationMessage;
    private JTree branchTree;
    private DefaultMutableTreeNode localBranchNode;
    private DefaultMutableTreeNode remoteBranchNode;
    private static final String LOCAL_BRANCH_NAME = "Local branch";
    private static final String REMOTE_BRANCH_NAME = "Remote branch";

    public BranchPanel(OperationMessage operationMessage, RepositoryTabOperationMessage repositoryTabOperationMessage) {
        this.operationMessage = operationMessage;
        this.repositoryTabOperationMessage = repositoryTabOperationMessage;
        createContents();
    }

    private void createContents() {
        setLayout(new BorderLayout());

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
        branchTree = new JTree(rootNode);
        branchTree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()>=2 &&
                        e.getButton() == MouseEvent.BUTTON1) {
                    changeBranch();
                }
            }
        });

        localBranchNode = new DefaultMutableTreeNode(LOCAL_BRANCH_NAME);
        rootNode.add(localBranchNode);

        remoteBranchNode = new DefaultMutableTreeNode(REMOTE_BRANCH_NAME);
        rootNode.add(remoteBranchNode);

        JScrollPane scroll = new JScrollPane(branchTree);
        add(scroll, BorderLayout.CENTER);
    }

    private void changeBranch() {
        TreePath path = branchTree.getSelectionPath();
        TreePath parent = path.getParentPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)parent.getLastPathComponent();
        String name = node.toString();

        switch (name) {
        case LOCAL_BRANCH_NAME:
            checkoutLocal();
            break;
        case REMOTE_BRANCH_NAME:
            checkoutRemote();
            break;
        }

    }

    private void checkoutLocal() {
    }

    private void checkoutRemote() {
    }

    public void updateLocalBranchList(File repositoryPath) throws IOException, GitAPIException {
        try (Git git = Git.open(repositoryPath)) {
            List<Ref> list = git.branchList().call();

            removeAllClidNode(localBranchNode);

            String current = git.getRepository().getBranch();

            for (Ref ref: list) {
                String name = ref.getName();
                name = name.substring(RepositoryTab.LOCAL_BRANCH_PREFIX.length());
                String mark = name.equals(current)? " *": "";

                DefaultMutableTreeNode node = new DefaultMutableTreeNode(name + mark);
                localBranchNode.add(node);
            }
        }
        updatePullTarget(repositoryPath);
    }

    public void updateRemoteBranchList(File repositoryPath) throws IOException, GitAPIException {
        try (Git git = Git.open(repositoryPath)) {
            List<Ref> list = git.branchList().setListMode(ListMode.REMOTE).call();

            removeAllClidNode(remoteBranchNode);

            for (Ref ref: list) {
                String name = ref.getName();
                name = name.substring(RepositoryTab.REMOTE_BRANCH_PREFIX.length());
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
                remoteBranchNode.add(node);
            }
        }
    }

    public void expandBranchTree() {
        DefaultTreeModel model = (DefaultTreeModel) branchTree.getModel();
        branchTree.expandPath(new TreePath(model.getPathToRoot(localBranchNode)));
        branchTree.expandPath(new TreePath(model.getPathToRoot(remoteBranchNode)));
    }

    private void removeAllClidNode(DefaultMutableTreeNode node) {
        while (node.getChildCount()!=0) {
            node.remove(0);
        }
    }

    public void updatePullTarget(File repositoryPath) throws IOException, GitAPIException {
        try (Git git = Git.open(repositoryPath)) {
            Status status = git.status().call();
            Set<String> added = status.getAdded();
            Set<String> changed = status.getChanged();
            Set<String> conflicting = status.getConflicting();
            Set<String> ignoredNotInIndex = status.getIgnoredNotInIndex();
            Set<String> missing = status.getMissing();
            Set<String> modified = status.getModified();
            Set<String> removed = status.getRemoved();
            Set<String> uncommittedChanges = status.getUncommittedChanges();
            Set<String> untracked = status.getUntracked();
            Set<String> untrackedFolders = status.getUntrackedFolders();

            Repository r = new RepositoryBuilder()
                    .setGitDir(repositoryPath)
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();

            //CLIGitCommand

            // Log log = git.log().call();
            String a = status.toString();
            String s = a;

            // https://qiita.com/esplo/items/7bdb736eb0b8ad3b382a
/*
            Repository repository = git.getRepository();
            PlotWalk revWalk = new PlotWalk(repository);
            ObjectId rootId = repository.resolve("refs/heads/master");
            RevCommit root = revWalk.parseCommit(rootId);
            PlotCommitList<PlotLane> plotCommitList = new PlotCommitList<PlotLane>();
            plotCommitList.source(revWalk);
            plotCommitList.fillTo(Integer.MAX_VALUE);
            for( PlotCommit<PlotLane> c: plotCommitList ) {
                String message = c.getFullMessage();
                String sa = message;
            }
*/
        } catch (Exception e) {
            MessageUtil.exceptionMessage(operationMessage.getMainFrame(), e);
            e.printStackTrace();
        }
    }
}

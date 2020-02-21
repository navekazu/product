package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import tools.gitclient.OperationMessage;

public class MainFrame extends JFrame {
    private JTabbedPane tab;
    private OperationMessage operationMessage;

    public MainFrame(OperationMessage operationMessage) {
        this.operationMessage = operationMessage;
        createContents();
    }

    private void createContents() {
        tab = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        Container root = getRootPane();
        root.setLayout(new BorderLayout());
        root.add(createMenubar(), BorderLayout.NORTH);
        root.add(tab, BorderLayout.CENTER);

        addNewTab();

        List<File> list = operationMessage.getOpeningRepositoryList();
        if (list!=null) {
            list.stream()
                .forEach(f -> {
                    RepositoryTab repositoryTab = new RepositoryTab(operationMessage);

                    repositoryTab.reopenRepository(f);
                    String repositoryName = repositoryTab.getRepositoryName();

                    tab.addTab(repositoryName, repositoryTab);
                });
        }

    }

    private Container createMenubar() {
        JPanel panel = new JPanel();

        JMenuBar menuBar = new JMenuBar();
        panel.add(menuBar);

        return panel;
    }

    private void addNewTab() {
        tab.addTab("＋", new NewTab(operationMessage));
    }

    public void addRepositoryTab(File local) {
        RepositoryTab repositoryTab = new RepositoryTab(operationMessage);

        repositoryTab.openRepository(local);
        String repositoryName = repositoryTab.getRepositoryName();

        tab.addTab(repositoryName, repositoryTab);
        tab.setSelectedIndex(tab.getTabCount()-1);
    }
}
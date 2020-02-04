package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;
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
        root.add(tab, BorderLayout.CENTER);

        addNewTab();
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
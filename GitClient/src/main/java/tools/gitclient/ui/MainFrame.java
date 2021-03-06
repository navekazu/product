package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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


//        JRootPane root = getRootPane();
//        root.setLayout(new BorderLayout());
//        root.add(tab, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(tab, BorderLayout.CENTER);
        setJMenuBar(createMenubar());

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

    public JMenuBar createMenubar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem credentialManagerMenu = new JMenuItem("Credential Manager");
        credentialManagerMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCredentialManagerMenu();
            }

        });
        fileMenu.add(credentialManagerMenu);

        return menuBar;
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

    private void onCredentialManagerMenu() {
        CredentialManagerDialog dialog = new CredentialManagerDialog(operationMessage);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setVisible(true);
    }

    public void updateCredencialsConfig() {
        for (int index=0; index<tab.getTabCount(); index++) {
            Component c = tab.getTabComponentAt(index);
            if (c instanceof RepositoryTab) {
                RepositoryTab r = (RepositoryTab)c;
                r.updateCredencialsConfig();
            }
        }
    }

}
package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    private JTabbedPane tab;

    public MainFrame() {
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
        tab.addTab("＋", new NewTab());
    }
}
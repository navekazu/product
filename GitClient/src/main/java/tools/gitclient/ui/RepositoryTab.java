package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import tools.gitclient.OperationMessage;

public class RepositoryTab extends Container {
    private OperationMessage operationMessage;

    public RepositoryTab(OperationMessage operationMessage) {
        this.operationMessage = operationMessage;
        createContents();
    }
    private void createContents() {
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createBranchPanel(), createBranchPanel());
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        add(splitPane, BorderLayout.CENTER);
    }

    private Container createBranchPanel() {
        return new JPanel();
    }
}

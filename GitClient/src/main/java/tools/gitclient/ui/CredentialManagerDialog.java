package tools.gitclient.ui;

import javax.swing.JDialog;

import tools.gitclient.OperationMessage;

public class CredentialManagerDialog extends JDialog {
    private OperationMessage operationMessage;

    public CredentialManagerDialog(OperationMessage operationMessage) {
        super(operationMessage.getMainFrame(), "Credential Manager", true);
        this.operationMessage = operationMessage;
        createContents();
    }

    private void createContents() {

    }

}

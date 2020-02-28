package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import tools.gitclient.OperationMessage;

public class CredentialManagerDialog extends JDialog {
    private OperationMessage operationMessage;

    public CredentialManagerDialog(OperationMessage operationMessage) {
        super(operationMessage.getMainFrame(), "Credential Manager", true);
        this.operationMessage = operationMessage;
        createContents();
    }

    private void createContents() {
        setLayout(new BorderLayout());
        add(createCredentialList(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Container createCredentialList() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private Container createFooter() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onOKButton();
            }
        });
        panel.add(okButton);

        return panel;
    }

    private void onOKButton() {

        setVisible(false);
    }
}

package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import tools.gitclient.OperationMessage;

public class CredentialManagerDialog extends JDialog {
    private OperationMessage operationMessage;
    private JTable credentialTable;
    private TableColumnModel columnModel;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JComboBox<String> typeBox;
    private JTextField userField;
    private JPasswordField passwordField;

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

        credentialTable = new JTable();
        credentialTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        columnModel = credentialTable.getColumnModel();
        tableModel = new DefaultTableModel();
        credentialTable.setModel(tableModel);

        columnModel.addColumn(createTableColumn("Name"));
        columnModel.addColumn(createTableColumn("Type"));
        columnModel.addColumn(createTableColumn("User"));

        panel.add(new JScrollPane(credentialTable), BorderLayout.CENTER);
        panel.add(createCredentialEditArea(), BorderLayout.SOUTH);

        return panel;
    }

    private TableColumn createTableColumn(String title) {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setHeaderValue(title);
        tableColumn.setResizable(true);
        return tableColumn;
    }

    private Container createCredentialEditArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel labelPanel = new JPanel();
        JPanel editPanel = new JPanel();
        panel.add(labelPanel, BorderLayout.WEST);
        panel.add(editPanel, BorderLayout.CENTER);

        labelPanel.setLayout(new GridLayout(5, 1));
        editPanel.setLayout(new GridLayout(5, 1));

        labelPanel.add(new JLabel("Name"));
        labelPanel.add(new JLabel("Type"));
        labelPanel.add(new JLabel("User"));
        labelPanel.add(new JLabel("Password"));
        labelPanel.add(new JLabel(""));

        nameField = new JTextField();
        typeBox = new JComboBox<>();
        userField = new JTextField();
        passwordField = new JPasswordField();
        editPanel.add(nameField);
        editPanel.add(typeBox);
        editPanel.add(userField);
        editPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(deleteButton);

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

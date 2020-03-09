package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import tools.gitclient.OperationMessage;
import tools.gitclient.config.CredentialsConfigManager.Credentials;

public class CredentialManagerDialog extends JDialog {
    private OperationMessage operationMessage;
    private JTable credentialTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JComboBox<String> typeBox;
    private JTextField userField;
    private JPasswordField passwordField;
    private List<Credentials> credentialList;
    private Credentials selectedCredentials;

    public CredentialManagerDialog(OperationMessage operationMessage) {
        super(operationMessage.getMainFrame(), "Credential Manager", true);
        this.operationMessage = operationMessage;
        credentialList = operationMessage.getCredentialsConfig();
        if (credentialList==null) {
            credentialList = new ArrayList<>();
        }
        selectedCredentials = null;
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
        tableModel = new DefaultTableModel(new String[] {"Name", "Type", "User"}, 0);
        credentialTable.setModel(tableModel);

        credentialTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onCredentialTableClicked(e);
            }
        });

        panel.add(new JScrollPane(credentialTable), BorderLayout.CENTER);
        panel.add(createCredentialEditArea(), BorderLayout.SOUTH);

        return panel;
    }

    private Container createCredentialEditArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel labelPanel = new JPanel();
        JPanel editPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        panel.add(labelPanel, BorderLayout.WEST);
        panel.add(editPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        labelPanel.setLayout(new GridLayout(4, 1));
        editPanel.setLayout(new GridLayout(4, 1));

        labelPanel.add(new JLabel("Name"));
        labelPanel.add(new JLabel("Type"));
        labelPanel.add(new JLabel("User"));
        labelPanel.add(new JLabel("Password"));

        nameField = new JTextField();
        typeBox = new JComboBox<>(new String[] {Credentials.Type.USER_PASSWORD.name()});
        userField = new JTextField();
        passwordField = new JPasswordField();
        editPanel.add(nameField);
        editPanel.add(typeBox);
        editPanel.add(userField);
        editPanel.add(passwordField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onAddButton();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onUpdateButton();
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onDeleteButton();
            }
        });
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

    private void clearSelectedCredentials() {
        selectedCredentials = null;
        nameField.setText("");
        typeBox.setSelectedIndex(0);
        userField.setText("");
        passwordField.setText("");
    }

    private void onAddButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Credentials credentials = new Credentials();
        credentials.no = Long.parseLong(sdf.format(new Date()));
        credentials.name = nameField.getText();
        credentials.type = Credentials.Type.USER_PASSWORD;
        credentials.user = userField.getText();
        credentials.password = new String(passwordField.getPassword());
        credentialList.add(credentials);

        operationMessage.setCredentialsConfig(credentialList);
        clearSelectedCredentials();
    }

    private void onUpdateButton() {
        if (selectedCredentials==null) {
            onAddButton();
            return;
        }

        selectedCredentials.name = nameField.getText();
        selectedCredentials.type = Credentials.Type.USER_PASSWORD;
        selectedCredentials.user = userField.getText();
        selectedCredentials.password = new String(passwordField.getPassword());

        operationMessage.setCredentialsConfig(credentialList);
        clearSelectedCredentials();
    }

    private void onDeleteButton() {
        if (selectedCredentials==null) {
            clearSelectedCredentials();
            return;
        }

        credentialList.remove(selectedCredentials);
        operationMessage.setCredentialsConfig(credentialList);
        clearSelectedCredentials();
    }

    private void onCredentialTableClicked(MouseEvent e) {
        if (e.getClickCount() != 2) {
            return;
        }

        Point point = e.getPoint();
        int index = credentialTable.rowAtPoint(point);
        if (index < 0) {
            return;
        }

        int row = credentialTable.convertRowIndexToModel(index);
        selectedCredentials = credentialList.get(row);
    }
}

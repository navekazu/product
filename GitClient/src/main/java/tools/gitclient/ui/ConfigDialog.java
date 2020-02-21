package tools.gitclient.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import tools.gitclient.OperationMessage;

public class ConfigDialog extends JDialog {
    private OperationMessage operationMessage;
    private ConfigResult configResult;
    private JRadioButton noneButton;
    private JRadioButton userPasswordButton;
    private JTextField credentialsUser;
    private JPasswordField credentialsPassword;

    public ConfigDialog(OperationMessage operationMessage) {
        super(operationMessage.getMainFrame(), "Config", true);
        this.operationMessage = operationMessage;
        this.configResult = new ConfigResult();
        createContents();
    }

    private void createContents() {
        Container root = getRootPane();
        root.setLayout(new BorderLayout());

        JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        root.add(tab, BorderLayout.CENTER);

        tab.addTab("Credentials", createCredentialsTab());

        root.add(createFooter(), BorderLayout.SOUTH);
    }

    private Container createCredentialsTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        ButtonGroup group = new ButtonGroup();

        noneButton = new JRadioButton("None");
        noneButton.setAlignmentX(0.0f);
        noneButton.setSelected(true);
        panel.add(noneButton);
        group.add(noneButton);

        userPasswordButton = new JRadioButton("User/Password");
        userPasswordButton.setAlignmentX(0.0f);
        panel.add(userPasswordButton);
        group.add(userPasswordButton);

        JPanel userPasswordPanel = new JPanel();
        Border userPasswordBorder = new CompoundBorder(new EmptyBorder(0, 30, 0, 0), userPasswordPanel.getBorder());
        userPasswordPanel.setBorder(userPasswordBorder);
        userPasswordPanel.setAlignmentX(0.0f);
        panel.add(userPasswordPanel);

        userPasswordPanel.setLayout(new BorderLayout());
        JPanel userPasswordLabelPanel = new JPanel();
        userPasswordLabelPanel.setLayout(new GridLayout(2, 1));
        userPasswordLabelPanel.add(new JLabel("User"));
        userPasswordLabelPanel.add(new JLabel("Password"));
        userPasswordPanel.add(userPasswordLabelPanel, BorderLayout.WEST);

        JPanel userPasswordFieldPanel = new JPanel();
        userPasswordFieldPanel.setLayout(new GridLayout(2, 1));
        credentialsUser = new JTextField();
        credentialsPassword = new JPasswordField();
        userPasswordFieldPanel.add(credentialsUser);
        userPasswordFieldPanel.add(credentialsPassword);
        userPasswordPanel.add(userPasswordFieldPanel, BorderLayout.CENTER);

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

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                onCancelButton();
            }
        });
        panel.add(cancelButton);

        return panel;
    }

    private void onOKButton() {
        configResult.isOK = true;

        if (noneButton.isSelected()) {
            configResult.credentialsProvider = null;

        } else if (userPasswordButton.isSelected()) {
            configResult.credentialsProvider = new UsernamePasswordCredentialsProvider(
                    credentialsUser.getText(), credentialsPassword.getText());

        }

        setVisible(false);
    }

    private void onCancelButton() {
        configResult.isOK = false;
        setVisible(false);
    }

    public ConfigResult getConfigResult() {
        return configResult;
    }

    public static class ConfigResult {
        public boolean isOK = false;
        public CredentialsProvider credentialsProvider = null;
    }
}

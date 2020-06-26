package tools.pwm.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class Login extends JDialog {
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private boolean loginSuccess;

    public Login() {
        loginSuccess = false;
        initContent();
    }

    private void initContent() {
        setModal(true);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new JLabel("Password"), BorderLayout.WEST);

        passwordField = new JPasswordField(30);
        add(passwordField, BorderLayout.CENTER);

        JPanel bottonPanel = new JPanel();
        bottonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoginButton();
            }
        });

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExitButton();
            }
        });

        bottonPanel.add(loginButton);
        bottonPanel.add(exitButton);
        add(bottonPanel, BorderLayout.SOUTH);
    }

    private void onLoginButton() {
        dispose();

    }

    private void onExitButton() {
        dispose();
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }
}

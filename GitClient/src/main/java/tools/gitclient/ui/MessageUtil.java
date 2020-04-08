package tools.gitclient.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageUtil {
    public static void exceptionMessage(Component parentComponent, Exception e) {
        JOptionPane.showMessageDialog(
                parentComponent,
                e.getLocalizedMessage(),
                "Exception",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void message(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(
                parentComponent,
                message,
                "Message",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

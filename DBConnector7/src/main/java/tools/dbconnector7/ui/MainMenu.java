package tools.dbconnector7.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tools.dbconnector7.NoticeInterface;

public class MainMenu extends JMenuBar {
    private NoticeInterface uiUpdateNotice;

    public MainMenu(NoticeInterface uiUpdateNotice) {
        this.uiUpdateNotice = uiUpdateNotice;
        this.add(createFileMenu());
        this.add(createEditMenu());
        this.add(createMiscMenu());
	}

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.add(new JMenuItem("Exit"));
        return menu;
    }

    private JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");
        return menu;
    }

    private JMenu createMiscMenu() {
        JMenu menu = new JMenu("MISC");
        menu.add(createLookAndFeelMenu());
        return menu;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu("Look & Feel");

        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info: infos) {
            JMenuItem item = new JMenuItem(info.getName());
            final String className = info.getClassName();

            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        UIManager.setLookAndFeel(className);
                        uiUpdateNotice.notice();
                    } catch(ClassNotFoundException exception) {
                        exception.printStackTrace();
                    } catch(InstantiationException exception) {
                        exception.printStackTrace();
                    } catch(IllegalAccessException exception) {
                        exception.printStackTrace();
                    } catch(UnsupportedLookAndFeelException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            menu.add(item);
        }

        return menu;
    }
}

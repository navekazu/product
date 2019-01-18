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
    private NoticeInterface connectNotice;
    private NoticeInterface disconnectNotice;

    public MainMenu(NoticeInterface uiUpdateNotice
            , NoticeInterface connectNotice
            , NoticeInterface disconnectNotice) {
        this.uiUpdateNotice = uiUpdateNotice;
        this.connectNotice = connectNotice;
        this.disconnectNotice = disconnectNotice;

        this.add(createFileMenu());
        this.add(createEditMenu());
        this.add(createDatabaseMenu());
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

    private JMenu createDatabaseMenu() {
        JMenu menu = new JMenu("Database");
        JMenuItem item;

        menu.add(createMenuItem("Connect", connectNotice));
        menu.add(createMenuItem("Disconnect", disconnectNotice));
        menu.addSeparator();
        menu.add(createMenuItem("Execute query", null));
        menu.add(createMenuItem("Cancel query", null));
        menu.add(createMenuItem("Query script", null));
        menu.addSeparator();
        menu.add(createMenuItem("Commit", null));
        menu.add(createMenuItem("Rollback", null));
        menu.addSeparator();
        menu.add(createIsolationMenu());

        return menu;
    }

    private JMenu createIsolationMenu() {
        JMenu menu = new JMenu("Isolation");

        menu.add(createMenuItem("Change isolation", null));
        menu.addSeparator();
        menu.add(createMenuItem("Change to READ UNCOMMITTED", null));
        menu.add(createMenuItem("Change to READ COMMITTED", null));
        menu.add(createMenuItem("Change to REPEATABLE READ", null));
        menu.add(createMenuItem("Change to SERIALIZABLE", null));

        return menu;
    }

    private JMenu createMiscMenu() {
        JMenu menu = new JMenu("MISC");
        menu.add(createLookAndFeelMenu());
        return menu;
    }

    private JMenuItem createMenuItem(String label, NoticeInterface notice) {
        JMenuItem item = new JMenuItem(label);

        if (notice!=null) {
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    notice.notice();
                }
            });
        }

        return item;
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

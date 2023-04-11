package tools.dbconnector7.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import tools.dbconnector7.AppHandle;
import tools.dbconnector7.NoticeInterface;
import tools.dbconnector7.menuhandler.AppClose;
import tools.dbconnector7.menuhandler.CancelQuery;
import tools.dbconnector7.menuhandler.Commit;
import tools.dbconnector7.menuhandler.Connect;
import tools.dbconnector7.menuhandler.Disconnect;
import tools.dbconnector7.menuhandler.ExecuteQuery;
import tools.dbconnector7.menuhandler.NewConnection;
import tools.dbconnector7.menuhandler.Rollback;
import tools.dbconnector7.menuhandler.SwingUiChanger;

public class MainMenu extends JMenuBar {
	private final AppHandle appHandle;

    public MainMenu(AppHandle appHandle) {
    	this.appHandle = appHandle;

        this.add(createFileMenu());
        this.add(createEditMenu());
        this.add(createDatabaseMenu());
        this.add(createMiscMenu());
	}

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.add(createMenuItem("Close", new AppClose(appHandle)));
        return menu;
    }

    private JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");
        return menu;
    }

    private JMenu createDatabaseMenu() {
        JMenu menu = new JMenu("Database");
        JMenuItem item;

        menu.add(createMenuItem("New connection", new NewConnection(appHandle)));
        menu.add(createMenuItem("Connect", new Connect(appHandle)));
        menu.add(createMenuItem("Disconnect", new Disconnect(appHandle)));
        menu.addSeparator();
        menu.add(createMenuItem("Execute query", new ExecuteQuery(appHandle)));
        menu.add(createMenuItem("Cancel query", new CancelQuery(appHandle)));
//        menu.add(createMenuItem("Query script", null));
        menu.addSeparator();
        menu.add(createMenuItem("Commit", new Commit(appHandle)));
        menu.add(createMenuItem("Rollback", new Rollback(appHandle)));
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
                    notice.notice(label);
                }
            });
        }

        return item;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu("Look & Feel");
        SwingUiChanger changer = new SwingUiChanger(appHandle);

        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info: infos) {
            menu.add(createMenuItem(info.getName(), changer));
        }

        return menu;
    }

}

package tools.dbconnector8.ui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import tools.dbconnector8.menu.AppClose;
import tools.dbconnector8.menu.DbConnect;
import tools.dbconnector8.menu.MenuBase;

public class MainMenu extends JMenuBar implements UiBase {

	public MainMenu() {
		initContents();
	}

	@Override
	public void initContents() {
        add(createFileMenu());
	}

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.add(createMenuItem("Connect", new DbConnect()));
        menu.addSeparator();
        menu.add(createMenuItem("Close", new AppClose()));
        return menu;
    }

    private JMenuItem createMenuItem(String label, MenuBase menuBase) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(menuBase);
        return item;
    }
}

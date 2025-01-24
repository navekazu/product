package tools.dbconnector8.menu;

import java.awt.event.ActionEvent;

import tools.dbconnector8.ui.ConnectDialog;

public class DbConnect extends MenuBase {

	public DbConnect() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new ConnectDialog().setVisible(true);
	}

}

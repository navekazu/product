package tools.dbconnector8.menu;

import java.awt.event.ActionEvent;

import tools.dbconnector8.AppHandle;

public class AppClose extends MenuBase {

	public AppClose() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AppHandle.getAppHandle().getApp().close();
	}

}

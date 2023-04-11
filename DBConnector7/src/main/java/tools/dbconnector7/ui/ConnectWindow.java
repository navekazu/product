package tools.dbconnector7.ui;

import javax.swing.JWindow;

import tools.dbconnector7.AppHandle;

public class ConnectWindow extends JWindow {
	private final AppHandle appHandle;

	public ConnectWindow(AppHandle appHandle) {
		this.appHandle = appHandle;
        init();
	}

	private void init() {
		
    	pack();
    }

}

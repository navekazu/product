package tools.dbconnector7.menuhandler;

import tools.dbconnector7.AppHandle;

public class AppClose extends MenuHandler {

	public AppClose(AppHandle appHandle) {
		super(appHandle);
	}

	@Override
	public void notice(String command) {
		appHandle.getMainFrame().dispose();
	}

}

package tools.dbconnector7.menuhandler;

import tools.dbconnector7.AppHandle;
import tools.dbconnector7.NoticeInterface;

public abstract class MenuHandler implements NoticeInterface {
	protected final AppHandle appHandle;

	public MenuHandler(AppHandle appHandle) {
		this.appHandle = appHandle;		
	}
}

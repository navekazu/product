package tools.dbconnector7.menuhandler;

import tools.dbconnector7.AppHandle;
import tools.dbconnector7.ui.ConnectWindow;

public class NewConnection extends MenuHandler {

	public NewConnection(AppHandle appHandle) {
		super(appHandle);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void notice(String command) {
		// TODO 自動生成されたメソッド・スタブ
		ConnectWindow connectWindow = new ConnectWindow(appHandle);
		connectWindow.setVisible(true);
	}

}

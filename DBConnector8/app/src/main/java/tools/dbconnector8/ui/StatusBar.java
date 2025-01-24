package tools.dbconnector8.ui;

import javax.swing.JLabel;

import tools.dbconnector8.AppHandle;

public class StatusBar extends JLabel implements UiBase {

	public StatusBar() {
		AppHandle.getAppHandle().setStatusBar(this);
		initContents();
	}

	@Override
	public void initContents() {
		setText(" ");
	}

}

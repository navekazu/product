package tools.dbconnector8.ui;

import javax.swing.JLabel;

public class StatusBar extends JLabel implements UiBase {

	public StatusBar() {
		initContents();
	}

	@Override
	public void initContents() {
		setText(" ");
	}

}

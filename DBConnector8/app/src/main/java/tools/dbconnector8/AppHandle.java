package tools.dbconnector8;

import lombok.Data;
import tools.dbconnector8.ui.MainFrame;

@Data
public class AppHandle {
	private static AppHandle appHandle = null;

	private AppHandle() {
	}

	public static AppHandle getAppHandle() {
		if (appHandle == null) {
			appHandle = new AppHandle();
		}

		return appHandle;
	}
	
	private MainFrame mainFrame;
	private App app;
}

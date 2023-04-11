package tools.dbconnector7.menuhandler;

import java.util.Objects;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tools.dbconnector7.AppHandle;

public class SwingUiChanger extends MenuHandler {

	public SwingUiChanger(AppHandle appHandle) {
		super(appHandle);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void notice(String command) {
        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info: infos) {
        	if (!Objects.equals(command, info.getName())) {
        		continue;
        	}
	        final String className = info.getClassName();
            try {
				UIManager.setLookAndFeel(className);
				SwingUtilities.updateComponentTreeUI(appHandle.getMainFrame());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
        }

	}
}

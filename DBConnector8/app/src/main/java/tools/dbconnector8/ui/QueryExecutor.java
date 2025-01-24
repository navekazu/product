package tools.dbconnector8.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;

import tools.dbconnector8.AppHandle;

public class QueryExecutor extends JTextArea implements UiBase {
	public QueryExecutor() {
		initContents();
	}

	@Override
	public void initContents() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				keyTypedHandler(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				keyTypedHandler(e);
			}
		});
	}
	
	private void keyTypedHandler(KeyEvent e) {
		
		// クエリ実行？
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
			executeQuery();
		}
	}

	private void executeQuery() {
		// 選択範囲から実行するクエリーを抜き出す
		// 選択していなかったら全体をクエリーとして扱う
		String query = (getSelectedText() == null? getText(): getSelectedText());
		AppHandle.getAppHandle().getResultView().executeQuery(query);
	}
}

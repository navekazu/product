package tools.dbconnector8.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JTextArea;

import tools.dbconnector8.AppHandle;
import tools.dbconnector8.persistence.PersistenceManager;

public class QueryExecutor extends JTextArea implements UiBase {
	public QueryExecutor() {
		initContents();
	}

	@Override
	public void initContents() {
		setTabSize(4);

		PersistenceManager pm = new PersistenceManager();
		try {
			String query = pm.getPersistenceQuery();
			setText(query);

		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

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
		persistenceQuery();

		// 選択範囲から実行するクエリーを抜き出す
		// 選択していなかったら全体をクエリーとして扱う
		String query = (getSelectedText() == null? getText(): getSelectedText());
		AppHandle.getAppHandle().getResultView().executeQuery(query);
	}
	
	public void persistenceQuery() {
		PersistenceManager pm = new PersistenceManager();
		try {
			String allQuery = getText();
			pm.writePersistenceQuery(allQuery);

		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}
	
	
}

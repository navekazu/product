package tools.dbconnector8.ui;

import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import tools.dbconnector8.AppHandle;
import tools.dbconnector8.logic.AutocompleteLogic;
import tools.dbconnector8.persistence.PersistenceManager;

public class QueryExecutor extends JTextArea implements UiBase {

	private JPopupMenu popupMenu;
	private JList<String> autoCompleteList;
	private DefaultListModel<String> autoCompleteModel;
	
	public QueryExecutor() {
		initContents();
	}

	@Override
	public void initContents() {
		setTabSize(4);
		setMargin(new Insets(5, 5, 5, 5));

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
			@Override
			public void keyPressed(KeyEvent e) {
				keyTypedHandler(e);
			}
		});

		// 入力補完用ポップアップメニュー
		popupMenu = new JPopupMenu();
		autoCompleteModel = new DefaultListModel<>();
		autoCompleteList = new JList<>(autoCompleteModel);
		popupMenu.add(new JScrollPane(autoCompleteList));

		

        // Enterキーで選択されたアイテムを取得
		autoCompleteList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String selectedItem = autoCompleteList.getSelectedValue();
                    if (selectedItem != null) {
                    	autoComplete(selectedItem);
                    }
                }
            }
        });

        // ダブルクリックで選択されたアイテムを取得
		autoCompleteList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // ダブルクリック判定
                    int index = autoCompleteList.locationToIndex(e.getPoint()); // クリックされた位置のアイテム
                    if (index >= 0) {
                        String selectedItem = autoCompleteList.getModel().getElementAt(index);
                    	autoComplete(selectedItem);
                    }
                }
            }
        });		
	}
	
	private void keyTypedHandler(KeyEvent e) {
		
		// クエリ実行？
		if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
			executeQuery();
			return ;
		}

		// KeyRelease時に補完を表示
		if (e.getID() == KeyEvent.KEY_RELEASED) {
			showAutoComplete();
		}
		
		// 入力補完が表示されていたらフォーカス移動
		if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_TAB) {
            e.consume(); // タブキーの通常の動作をキャンセル
            autoCompleteList.requestFocusInWindow(); // 次のコンポーネントにフォーカス移動
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
	
	private void showAutoComplete() {
		// キャレットの前の単語を取得
		String word = getCurrentWord();
		
		if (Objects.equals(word, "")) {
			popupMenu.setVisible(false);
			return ;
		}

		AutocompleteLogic logic = new AutocompleteLogic();
		List<String> list = null;

		try {
			list = logic.execute(word);
			
			if (list.size() == 0) {
				popupMenu.setVisible(false);
				return ;
			}
			
			
		} catch (Exception e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	
		if (list == null) {
			popupMenu.setVisible(false);
			return ;
		}

		// 入力補完を表示
        try {
            // キャレットの位置を取得
        	Rectangle2D rect = modelToView2D(getCaretPosition());
            if (rect != null) {
            	autoCompleteModel.removeAllElements();
            	autoCompleteModel.addAll(list);
            	autoCompleteList.setSelectedIndex(0);

          		popupMenu.show(this, (int)rect.getX(), (int)(rect.getY() + rect.getHeight()));

            	this.requestFocusInWindow(); // ← フォーカスを元に戻す
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
	}

	private String getCurrentWord() {
        try {
            int caretPos = getCaretPosition();
            String textBeforeCaret = getText(0, caretPos);

            // キャレット手前が改行なら終了
            if (caretPos == 0 || textBeforeCaret.charAt(caretPos - 1) == '\n') {
            	return "";
            }
            
            // 単語を抽出（英単語の場合: \b\w+$ で最後の単語を取得）
            Pattern pattern = Pattern.compile("\\b\\w+$");
            Matcher matcher = pattern.matcher(textBeforeCaret);

            if (matcher.find()) {
                return matcher.group(); // 最後の単語を返す
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
	}
	
	private void autoComplete(String word) {
		String inputWord = getCurrentWord();
		
		// 現在のキャレット位置から入力文字分削除
        int caretPos = getCaretPosition(); // 現在のキャレット位置
        int deleteLength = inputWord.length(); // 削除する文字数

        if (caretPos > 0) {
            int start = Math.max(caretPos - deleteLength, 0); // 負の値を防ぐ
            try {
                getDocument().remove(start, caretPos - start);
                getDocument().insertString(start, word, null);

            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        
		popupMenu.setVisible(false);
        requestFocusInWindow();
	}

}

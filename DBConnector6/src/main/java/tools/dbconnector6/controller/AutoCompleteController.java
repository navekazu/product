package tools.dbconnector6.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import tools.dbconnector6.entity.AutoComplete;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 入力補完画面用コントローラ。<br>
 * 入力補完はSQL予約語・テーブル名・カラム名を対象に、SQL入力欄へポップアップ表示して行う。<br>
 */
public class AutoCompleteController extends SubController implements Initializable {

    // Scene overview
    // +----------------------------------+
    // | autoCompleteListView             |
    // |                                  |
    // |                                  |
    // +----------------------------------+
    @FXML private ListView autoCompleteListView;        // 入力補完表示用

    // 入力補完一覧の参照（SQLの予約語・全テーブル名・全カラム名が入る）
    // インスタンスの生成はMainControllerが行う。
    private Set<AutoComplete> autoCompleteList;

    /**
     * 入力補完一覧の参照をセットする
     * @param autoCompleteList 入力補完一覧の参照
     */
    public void setAutoCompleteList(Set<AutoComplete> autoCompleteList) {
        this.autoCompleteList = autoCompleteList;
    }

    /**
     * 補完対象を入力したか、入力補完一覧と突き合わせる
     * @param event キーイベントの内容
     * @param query キャレット位置にある入力済みSQL
     * @return 入力補完一覧に入力済みSQLがあれば true、なければ false
     */
    public boolean isInputAutoComplete(KeyEvent event, String query) {
        if (query.length()<=1) {
            return false;
        }

        final boolean upperCase = isUpperCase(query);

        List<AutoComplete> list;
        synchronized (autoCompleteList) {
            list = autoCompleteList.stream()
                    .filter(word -> word.getWord().toLowerCase().startsWith(query.toLowerCase()))
                    .map(word -> {
                        word.setWord(upperCase ? word.getWord().toUpperCase() : word.getWord().toLowerCase());
                        return word;
                    })
                    .collect(Collectors.toList());
        }

        if (list.isEmpty()) {
            return false;
        }

        ObservableList<AutoComplete> items = autoCompleteListView.getItems();
        items.clear();
        items.addAll(list);
        autoCompleteListView.getSelectionModel().select(0);
        return true;
    }

    /**
     * 最後に入力した文字が大文字か判定する
     * @param query 入力済みSQL
     * @return 大文字の場合 true、それ以外は false
     */
    private boolean isUpperCase(String query) {
        StringBuilder queryBuffer = (new StringBuilder(query)).reverse();

        for (int loop=0; loop<queryBuffer.length(); loop++) {
            if (!isCharacter(queryBuffer.charAt(loop))) {
                continue;
            }
            if (isUpperCharacter(queryBuffer.charAt(loop))) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 入力文字が文字なのか判定
     * @param ch 入力文字
     * @return 文字の場合 true、それ以外は false
     */
    private boolean isCharacter(char ch) {
        return Arrays.stream(ALPHABETS_ALL).anyMatch(c -> c == ch);
    }
    // isCharacterメソッドで使用する「大文字小文字アルファベット」
    private static final Character[] ALPHABETS_ALL = new Character[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    };

    /**
     * 入力文字が大文字なのか判定
     * @param ch 入力文字
     * @return 大文字の場合 true、それ以外は false
     */
    private boolean isUpperCharacter(char ch) {
        return Arrays.stream(ALPHABETS_UPPERCASE).anyMatch(c -> c == ch);
    }
    // isUpperCharacterメソッドで使用する「大文字アルファベット」
    private static final Character[] ALPHABETS_UPPERCASE = new Character[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    /**
     * コントローラのルート要素が完全に処理された後に、コントローラを初期化するためにコールされます。<br>
     * @param location ルート・オブジェクトの相対パスの解決に使用される場所、または場所が不明の場合は、null
     * @param resources ート・オブジェクトのローカライズに使用されるリソース、
     *                  またはルート・オブジェクトがローカライズされていない場合は、null。
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // 選択した入力補完をメイン画面に通知する
    private void selected() {
        int index = autoCompleteListView.getSelectionModel().getSelectedIndex();
        if (index==-1) {
            return;
        }

        ObservableList<AutoComplete> items = autoCompleteListView.getItems();
        mainControllerInterface.selectAutoComplete(items.get(index).getWord());
    }

    /***************************************************************************
     *                                                                         *
     * Event handler                                                           *
     *                                                                         *
     **************************************************************************/

    // 入力補完表示用ListViewのキープレスイベントハンドラ
    // キーの種類によって以下のように処理する。
    //   ENTER -> ListViewで現在選択中の入力補完をメイン画面に通知して自画面を閉じる
    //   TAB   -> メイン画面にフォーカスを戻す
    //   ESC   -> メイン画面にフォーカスを戻し、自画面を閉じる
    @FXML
    private void onKeyPressed(KeyEvent event){
        switch (event.getCode()) {
            case ENTER:
                mainControllerInterface.hideAutoCompleteStage();
                selected();
                break;

            case TAB:
                mainControllerInterface.requestFocusToMainController();
                break;

            case ESCAPE:
                mainControllerInterface.hideAutoCompleteStage();
                mainControllerInterface.requestFocusToMainController();
                break;
        }
    }

    // 入力補完表示用ListViewのマウスイベントハンドラ
    // プライマリボタン（左ボタン）のダブルクリック時に選択した予約語をメイン画面に通知して自画面を閉じる
    @FXML
    private void onMouseClicked(MouseEvent event){
        if (event.getClickCount()>=2 && event.getButton()== MouseButton.PRIMARY) {
            mainControllerInterface.hideAutoCompleteStage();
            selected();
        }
    }
}


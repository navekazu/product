package tools.dbconnector6.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 入力補完用のエンティティクラス。
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class AutoComplete {
    /**
     * 入力補完の種類
     */
    public enum AutoCompleteType{
        /**
         * SQL予約語
         */
        SQL,

        /**
         * テーブル名（テーブル、ビュー、シノニムなど）
         */
        TABLE,

        /**
         * カラム名
         */
        COLUMN,
    }

    private AutoCompleteType type;  // 入力補完の種類
    private String word;            // 入力補完

    /**
     * 入力補完の文字列表現。<br>
     * 入力補完フィールドの値を返す
     * @return 入力補完フィールドの値
     */
    @Override
    public String toString() {
        return word;
    }
}

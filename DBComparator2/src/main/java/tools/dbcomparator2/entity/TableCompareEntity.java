package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 比較対象のテーブル情報
 */
@Data
@Builder
public class TableCompareEntity {
    // テーブル名
    private String tableName;

    // レコード数
    private int recordCount;

    // プライマリキーのカラム
    private List<String> primaryKeyColumnList;

    // プライマリキーの値のハッシュ値をキーにしたRecordHashEntityのMap
    private Map<String, RecordHashEntity> recordHashEntityMap;

    public void addRecordHashEntity(RecordHashEntity entity) {
        if (recordHashEntityMap==null) {
            recordHashEntityMap = Collections.synchronizedMap(new HashMap<>());
        }
        recordHashEntityMap.put(entity.getPrimaryKeyHashValue(), entity);
    }
}

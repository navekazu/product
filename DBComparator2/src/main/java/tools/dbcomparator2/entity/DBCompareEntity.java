package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;
import tools.dbcomparator2.enums.DBCompareStatus;
import tools.dbcomparator2.service.DBParseService;

import java.util.*;

@Data
@Builder
public class DBCompareEntity {
    private ConnectEntity connectEntity;
    private DBParseService dbParseService;
    private List<String> tableList;

    // テーブル名をキーに、そのテーブルのレコード数を格納する
    private Map<String, Integer> tableRecordCount;

    // テーブル名をキーに、そのテーブルのプライマリーキーの列名を格納する
    private Map<String, List<String>> primaryKeyColumnList;

    // テーブル名をキーに、そのテーブルのレコード情報を格納する
    // レコード情報はプライマリキーの値のハッシュ値をキーに格納する
    private Map<String, Map<String, RecordHashEntity>> tableRecordEntity;

    public void putTableRecordCount(String tableName, int recordCount) {
        if (tableRecordCount==null) {
            tableRecordCount = new HashMap<>();
        }
        tableRecordCount.put(tableName, recordCount);
    }

    public void putPrimaryKeyColumnList(String tableName, List<String> list) {
        if (primaryKeyColumnList==null) {
            primaryKeyColumnList = new HashMap<>();
        }
        primaryKeyColumnList.put(tableName, list);
    }

    public void putTableRecordEntity(String tableName, RecordHashEntity entity) {
        if (tableRecordEntity==null) {
            tableRecordEntity = new HashMap<>();
        }
        if (!tableRecordEntity.containsKey(tableName)) {
            tableRecordEntity.put(tableName, Collections.synchronizedMap(new HashMap<>()));
        }
        tableRecordEntity.get(tableName).put(entity.getPrimaryKeyHashValue(), entity);
    }
    public int countTableRecordEntity(String tableName, RecordHashEntity entity) {
        if (tableRecordEntity==null) {
            tableRecordEntity = new HashMap<>();
        }
        if (!tableRecordEntity.containsKey(tableName)) {
            tableRecordEntity.put(tableName, Collections.synchronizedMap(new HashMap<>()));
        }
        return tableRecordEntity.get(tableName).size();
    }

    private Map<String, List<PrimaryKeyValue>> tableValues;
    private DBCompareStatus status;
}

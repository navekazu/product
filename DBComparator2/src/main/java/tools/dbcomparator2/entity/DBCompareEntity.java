package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;
import tools.dbcomparator2.enums.DBCompareStatus;
import tools.dbcomparator2.service.DBParseService;

import java.sql.Connection;
import java.util.*;

@Data
@Builder
public class DBCompareEntity {
    private ConnectEntity connectEntity;
    private DBParseService dbParseService;
    private List<String> tableList;
    private Map<String, Integer> tableRecordCount = new HashMap<>();
    private Map<String, List<TableRecordEntity>> tableRecordEntity = new HashMap<>();

    public void putTableRecordCount(String tableName, int recordCount) {
        tableRecordCount.put(tableName, recordCount);
    }

    public void putTableRecordEntity(String tableName, TableRecordEntity entity) {
        if (!tableRecordEntity.containsKey(tableName)) {
            tableRecordEntity.put(tableName, Collections.synchronizedList(new ArrayList<>()));
        }
        tableRecordEntity.get(tableName).add(entity);
    }
    public int countTableRecordEntity(String tableName, TableRecordEntity entity) {
        if (!tableRecordEntity.containsKey(tableName)) {
            tableRecordEntity.put(tableName, Collections.synchronizedList(new ArrayList<>()));
        }
        return tableRecordEntity.size();
    }

    private Map<String, List<PrimaryKeyValue>> tableValues;
    private DBCompareStatus status;
}

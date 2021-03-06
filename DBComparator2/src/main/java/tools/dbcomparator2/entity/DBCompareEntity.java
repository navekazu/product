package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;
import tools.dbcomparator2.enums.DBParseStatus;
import tools.dbcomparator2.service.DBParseService;

import java.util.*;

/**
 * 比較対象のデータベース情報
 */
@Data
@Builder
public class DBCompareEntity {
    private ConnectEntity connectEntity;
    private DBParseStatus status;
    private DBParseService dbParseService;
    private Map<String, TableCompareEntity> tableCompareEntityMap;

    public void addTableCompareEntity(TableCompareEntity entity) {
        if (tableCompareEntityMap==null) {
            tableCompareEntityMap = Collections.synchronizedMap(new HashMap<>());
        }
        tableCompareEntityMap.put(entity.getTableName(), entity);
    }

    public TableCompareEntity getTableCompareEntity(String tableName) {
        return tableCompareEntityMap.get(tableName);
    }
}

package tools.dbcomparator2.service;

import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;

import java.util.List;

/**
 * DB解析の通知イベント
 */
public interface DBParseNotification {
    public void parsedTableList(ConnectEntity connectEntity, List<String> tableList);
    public void countedTableRecord(ConnectEntity connectEntity, String tableName, int recordCount);
    public void parsedPrimaryKey(ConnectEntity connectEntity, String tableName, List<String> primaryKeyColumnList);
    public void parsedTableRecord(ConnectEntity connectEntity, String tableName, int rowNumber, RecordHashEntity tableRecordEntity);

    public void fatal(ConnectEntity connectEntity, Exception exception);
    public void error(ConnectEntity connectEntity, Exception exception);
}

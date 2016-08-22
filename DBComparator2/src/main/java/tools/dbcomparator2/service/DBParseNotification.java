package tools.dbcomparator2.service;

import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.TableRecordEntity;

import java.sql.Connection;
import java.util.List;

/**
 * DB解析の通知イベント
 */
public interface DBParseNotification {
    public void parsedTableList(ConnectEntity connectEntity, List<String> tableList);
    public void countedTableRecord(ConnectEntity connectEntity, String tableName, int recordCount);
    public void parsedTableRecord(ConnectEntity connectEntity, String tableName, TableRecordEntity tableRecordEntity);

    public void fatal(ConnectEntity connectEntity, Exception exception);
    public void error(ConnectEntity connectEntity, Exception exception);
}

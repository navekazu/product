package tools.dbcomparator2.service;

import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * テストコードを短くするためだけのDBParseNotification実装クラス
 */
public class DBParseNotificationAdapter implements DBParseNotification{
    @Override
    public void parsedTableList(ConnectEntity connectEntity, List<String> tableList) {

    }

    @Override
    public void countedTableRecord(ConnectEntity connectEntity, String tableName, int recordCount) {

    }

    @Override
    public void parsedPrimaryKey(ConnectEntity connectEntity, String tableName, List<String> primaryKeyColumnList) {

    }

    @Override
    public void parsedTableRecord(ConnectEntity connectEntity, String tableName, int rowNumber, RecordHashEntity tableRecordEntity) {

    }

    @Override
    public void fatal(ConnectEntity connectEntity, Exception exception) {
        fail();
    }

    @Override
    public void error(ConnectEntity connectEntity, Exception exception) {
        fail();
    }
}

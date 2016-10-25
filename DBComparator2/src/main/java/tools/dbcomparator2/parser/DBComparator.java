package tools.dbcomparator2.parser;

import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.entity.TableCompareEntity;

import java.util.List;

public class DBComparator implements DBParseNotification {

    public void startCompare(List<ConnectEntity> connectEntityList) {

    }

    @Override
    public void parsedTableList(ConnectEntity connectEntity, List<TableCompareEntity> tableCompareEntityList) throws Exception {

    }

    @Override
    public void parsedPrimaryKey(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity) throws Exception {

    }

    @Override
    public void countedTableRecord(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity) throws Exception {

    }

    @Override
    public void parsedTableRecord(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity, int rowNumber, RecordHashEntity tableRecordEntity) throws Exception {

    }
}

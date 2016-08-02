package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;
import tools.dbcomparator2.enums.DBCompareStatus;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DBCompareEntity {
    private ConnectEntity connectEntity;
    private Map<String, List<PrimaryKeyValue>> tableValues;
    private DBCompareStatus status;
}

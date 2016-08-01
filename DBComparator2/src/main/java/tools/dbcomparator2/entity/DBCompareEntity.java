package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;
import tools.dbcomparator2.enums.DBCompareStatus;

@Data
@Builder
public class DBCompareEntity {
    private ConnectEntity connectEntity;
    private DBCompareStatus status;
}

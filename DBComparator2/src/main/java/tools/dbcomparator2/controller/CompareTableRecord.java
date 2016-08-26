package tools.dbcomparator2.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompareTableRecord {
    private String tableName;
    private Double progress;
    private String memo;
    private int rowCount;
    private int count;
}

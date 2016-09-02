package tools.dbcomparator2.entity;

import javafx.beans.property.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

public class CompareTableRecord {
    private SimpleStringProperty tableName;
    private SimpleDoubleProperty progress;
    private SimpleStringProperty memo;
    private SimpleIntegerProperty rowCount;
    private SimpleIntegerProperty count;

    public CompareTableRecord() {
        tableName = new SimpleStringProperty();
        progress = new SimpleDoubleProperty();
        memo = new SimpleStringProperty();
        rowCount = new SimpleIntegerProperty();
        count = new SimpleIntegerProperty();
    }

    public String getTableName() {
        return tableName.get();
    }

    public void setTableName(String tableName) {
        this.tableName.set(tableName);
    }

    public Double getProgress() {
        return progress.get();
    }

    public void setProgress(Double progress) {
        this.progress.set(progress);
    }

    public String getMemo() {
        return memo.get();
    }

    public void setMemo(String memo) {
        this.memo.set(memo);
    }

    public int getRowCount() {
        return rowCount.get();
    }

    public void setRowCount(int rowCount) {
        this.rowCount.set(rowCount);
    }

    public int getCount() {
        return count.get();
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public StringProperty tableNameProperty() {
        return tableName;
    }
    public DoubleProperty progressProperty() {
        return progress;
    }
    public StringProperty memoProperty() {
        return memo;
    }

}

package tools.dbcomparator2.controller;

public interface MainControllerNotification {
    public void addRow(String tableName);
    public void initProgress(String tableName, int rowCount);
    public void updateProgress(String tableName, int count);
}

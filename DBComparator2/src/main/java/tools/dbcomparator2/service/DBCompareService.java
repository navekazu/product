package tools.dbcomparator2.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import tools.dbcomparator2.task.SchemaParser;

public class DBCompareService extends Service<Void> {
    @Override
    protected Task<Void> createTask() {
        return new SchemaParser();
    }
}

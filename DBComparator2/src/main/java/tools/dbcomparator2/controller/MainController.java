package tools.dbcomparator2.controller;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.dbcomparator2.entity.CompareTableRecord;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.RecordHashEntity;
import tools.dbcomparator2.entity.TableCompareEntity;
import tools.dbcomparator2.enums.CompareType;
import tools.dbcomparator2.enums.RecordCompareStatus;
import tools.dbcomparator2.parser.DBComparator;
import tools.dbcomparator2.parser.DBParseNotification;
import tools.dbcomparator2.service.DBCompareService;

import java.net.URL;
import java.util.*;

public class MainController extends Application implements Initializable, MainControllerNotification, DBParseNotification {
    private Logger logger = LoggerFactory.getLogger(MainController.class);
    private DBCompareService dbCompareService;

    @FXML
    private TitledPane generalOperationsTitledPane;

    @FXML
    private Accordion generalOperationsAccordion;


    @FXML
    private ConnectController primaryController;

    @FXML
    private ConnectController secondaryController;

    @FXML
    private ChoiceBox compareType;

    @FXML
    private Button startCompareButton;

    @FXML
    private Button restartCompareButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView compareTable;

    @FXML
    private TableColumn<CompareTableRecord, String> tableColumn;

    @FXML
    private TableColumn<CompareTableRecord, Double> progressColumn;

    @FXML
    private TableColumn<CompareTableRecord, String> memoColumn;

    @FXML
    private Label statusLabel;

    private Map<String, CompareTableRecord> compareTableRecordMap;

    private Service compareBackgroundService;
    private DBComparator dbComparator;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("DBComparator2");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Callback callback = ProgressBarTableCell.<CompareTableRecord>forTableColumn();

        tableColumn.setCellValueFactory(new PropertyValueFactory<CompareTableRecord, String>("tableName"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<CompareTableRecord, Double>("progress"));
        progressColumn.setCellFactory(ProgressBarTableCell.<CompareTableRecord>forTableColumn());
        memoColumn.setCellValueFactory(new PropertyValueFactory<CompareTableRecord, String>("memo"));

        dbCompareService = new DBCompareService();
        dbCompareService.setMainControllerNotification(this);
        dbCompareService.setCompareTableRecordList(compareTable.getItems());

        dbComparator = new DBComparator(this);

        compareTableRecordMap = Collections.synchronizedMap(new HashMap<>());

        compareType.getItems().add(CompareType.IMMEDIATE);
        compareType.getItems().add(CompareType.PIVOT);
        compareType.getItems().add(CompareType.ROTATION);
        compareType.getSelectionModel().select(0);
        compareType.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<CompareType>() {
                    @Override
                    public void changed(ObservableValue<? extends CompareType> observable, CompareType oldValue, CompareType newValue) {
                        updateStatus(false);
                    }
                });

        generalOperationsAccordion.setExpandedPane(generalOperationsTitledPane);

        restartCompareButton.setDisable(true);
        cancelButton.setDisable(true);

        compareBackgroundService = new Service() {
            @Override
            protected Task createTask() {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        dbCompareService.startCompare();
                        return null;
                    }

                    @Override
                    protected void 	scheduled() {
                        updateStatus(isRunning());
                    }

                    @Override
                    protected void 	cancelled() {
                        updateStatus(isRunning());
                    }

                    @Override
                    protected void 	failed() {
                        updateStatus(isRunning());
                    }

                    @Override
                    protected void 	succeeded() {
                        updateStatus(isRunning());
                    }
                };
                return task;
            }
        };
        updateStatus(false);

        // テストコード
        primaryController.setLibraryPath("h2-1.3.176.jar");
        primaryController.setDriver("org.h2.Driver");
        primaryController.setUrl("jdbc:h2:file:./testdb1/testdb");
        primaryController.setUser("sa");
        primaryController.setPassword("");
        primaryController.setSchema("PUBLIC");
        secondaryController.setLibraryPath("h2-1.3.176.jar");
        secondaryController.setDriver("org.h2.Driver");
        secondaryController.setUrl("jdbc:h2:file:./testdb2/testdb");
        secondaryController.setUser("sa");
        secondaryController.setPassword("");
        secondaryController.setSchema("PUBLIC");
    }

    private void updateStatus(boolean isRunning) {
        compareType.setDisable(isRunning);
        startCompareButton.setDisable(isRunning);
        restartCompareButton.setDisable(!(!isRunning && dbCompareService.canRestartable() && compareType.getSelectionModel().getSelectedItem()!=CompareType.IMMEDIATE));
        cancelButton.setDisable(!isRunning);
    }

    @FXML
    private void handleStartCompareButton(ActionEvent event) {
        generalOperationsTitledPane.setExpanded(false);
        statusLabel.setText("Compare start.");

        CompareType type = (CompareType)compareType.getSelectionModel().getSelectedItem();
        List<ConnectEntity> entityList = new ArrayList<>();

        entityList.add(ConnectEntity.builder()
                .library(primaryController.getLibraryPath())
                .driver(primaryController.getDriver())
                .url(primaryController.getUrl())
                .user(primaryController.getUser())
                .password(primaryController.getPassword())
                .schema(primaryController.getSchema())
                .build());

        if (type==CompareType.IMMEDIATE) {
            entityList.add(ConnectEntity.builder()
                    .library(secondaryController.getLibraryPath())
                    .driver(secondaryController.getDriver())
                    .url(secondaryController.getUrl())
                    .user(secondaryController.getUser())
                    .password(secondaryController.getPassword())
                    .schema(secondaryController.getSchema())
                    .build());
        }

        dbCompareService.setCompareType(type);
        dbCompareService.updateConnectEntity(entityList);
//        compareBackgroundService.restart();

        try {
            dbComparator.startCompare(entityList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRestartCompareButton(ActionEvent event) {
        generalOperationsTitledPane.setExpanded(false);
        statusLabel.setText("Compare start.");

        ConnectEntity secondaryEntity = ConnectEntity.builder()
                .library(secondaryController.getLibraryPath())
                .driver(secondaryController.getDriver())
                .url(secondaryController.getUrl())
                .user(secondaryController.getUser())
                .password(secondaryController.getPassword())
                .schema(secondaryController.getSchema())
                .build();

        dbCompareService.setCompareType((CompareType) compareType.getSelectionModel().getSelectedItem());
        dbCompareService.addConnectEntity(secondaryEntity);
        compareBackgroundService.restart();

    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        statusLabel.setText("Compare cancel.");
        compareBackgroundService.cancel();
    }

    @Override
    public void addRow(String tableName) {
        synchronized (compareTableRecordMap) {
            CompareTableRecord record = new CompareTableRecord();
            record.setTableName(tableName);
            compareTableRecordMap.put(tableName, record);
            compareTable.getItems().add(record);
        }
    }

    @Override
    public void initProgress(String tableName, int rowCount) {
        if (!compareTableRecordMap.containsKey(tableName)) {
            return;
        }

        CompareTableRecord record = compareTableRecordMap.get(tableName);
        record.setRowCount(rowCount);
    }

    @Override
    public void updateProgress(String tableName, final int count) {
        if (!compareTableRecordMap.containsKey(tableName)) {
            return;
        }

//        Platform.runLater(() -> {
            List<CompareTableRecord> list = compareTable.getItems();
            list.stream()
                    .filter(record -> tableName.equals(record.getTableName()))
                    .forEach(record -> {
                        double d = ((double) (count + 1) / (double) record.getRowCount());
                        record.setProgress(d);
                        record.setMemo(String.format("%,d/%,d", (count + 1), record.getRowCount()));
                    });

//            CompareTableRecord record = compareTableRecordMap.get(tableName);
//            double d = ((double) (count + 1) / (double) record.getRowCount());
//            logger.debug("d:"+d);
//            record.setProgress(d);
//            record.setMemo(String.format("%,d/%,d", (count + 1), record.getRowCount()));
//        });
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void parsedTableList(ConnectEntity connectEntity, List<TableCompareEntity> tableCompareEntityList) throws Exception {
        synchronized (compareTableRecordMap) {
            tableCompareEntityList.stream()
                    .filter(tableCompareEntity -> !compareTableRecordMap.containsKey(tableCompareEntity.getTableName()))
                    .forEach(tableCompareEntity -> {
                        CompareTableRecord record = new CompareTableRecord();
                        record.setTableName(tableCompareEntity.getTableName());
                        compareTableRecordMap.put(tableCompareEntity.getTableName(), record);
                        compareTable.getItems().add(record);
                    });
        }

    }

    @Override
    public void parsedPrimaryKey(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity) throws Exception {
    }

    @Override
    public void countedTableRecord(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity) throws Exception {
        synchronized (compareTableRecordMap) {
            if (!compareTableRecordMap.containsKey(tableCompareEntity.getTableName())) {
                return;
            }
            CompareTableRecord record = compareTableRecordMap.get(tableCompareEntity.getTableName());
            record.setRowCount(record.getRowCount()<tableCompareEntity.getRecordCount()? tableCompareEntity.getRecordCount(): record.getRowCount());
            record.setMemo(String.format("Row counts: %,3d", record.getRowCount()));
        }
    }

    @Override
    public void parsedTableRecord(ConnectEntity connectEntity, TableCompareEntity tableCompareEntity, int rowNumber, RecordHashEntity tableRecordEntity) throws Exception {
        synchronized (compareTableRecordMap) {
            if (!compareTableRecordMap.containsKey(tableCompareEntity.getTableName())) {
                return;
            }
            CompareTableRecord record = compareTableRecordMap.get(tableCompareEntity.getTableName());
            record.setCount(record.getCount()<rowNumber? rowNumber: record.getCount());

            double rowCount = record.getRowCount();
            double count = record.getCount()+1;
            record.setProgress(count/rowCount);
            record.setMemo(String.format("Compare: %,3d", record.getCount()+1));

            if (!record.getPrimaryKeyHashValueMap().containsKey(tableRecordEntity.getPrimaryKeyHashValue())) {
                record.getPrimaryKeyHashValueMap().put(tableRecordEntity.getPrimaryKeyHashValue(), RecordCompareStatus.ONE_SIDE_ONLY);
                return ;
            }

            List<RecordHashEntity> reverseSideRecordHashEntityList = dbComparator.getReverseSideRecordHashEntity(connectEntity, tableCompareEntity, tableRecordEntity);
            record.getPrimaryKeyHashValueMap().put(tableRecordEntity.getPrimaryKeyHashValue(),
                reverseSideRecordHashEntityList.stream().allMatch(recordHashEntity -> recordHashEntity.getAllColumnHashValue().equals(tableRecordEntity.getAllColumnHashValue()))?
                    RecordCompareStatus.EQUALITY: RecordCompareStatus.INEQUALITY);

        }

    }
}

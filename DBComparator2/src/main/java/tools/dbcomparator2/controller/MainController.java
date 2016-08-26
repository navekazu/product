package tools.dbcomparator2.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.service.DBCompareService;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController extends Application implements Initializable, MainControllerNotification {
    private DBCompareService dbCompareService;

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

        compareTableRecordMap = Collections.synchronizedMap(new HashMap<>());

    }

    @FXML
    private void handleConnectButton(ActionEvent event) {

        Thread t = new Thread() {

            @Override
            public void run() {
                ConnectEntity connectEntity1 = ConnectEntity.builder()
                        .library("h2-1.3.176.jar")
                        .driver("org.h2.Driver")
                        .url("jdbc:h2:file:./testdb1/testdb")
                        .user("sa")
                        .password(null)
                        .schema("PUBLIC")
                        .build();
                ConnectEntity connectEntity2 = ConnectEntity.builder()
                        .library("h2-1.3.176.jar")
                        .driver("org.h2.Driver")
                        .url("jdbc:h2:file:./testdb2/testdb")
                        .user("sa")
                        .password(null)
                        .schema("PUBLIC")
                        .build();
                dbCompareService.startCompare(connectEntity1, connectEntity2);
            }
        };
        t.start();

    }

    @FXML
    private void handleRestartButton(ActionEvent event) {

    }

    @Override
    public void addRow(String tableName) {
        synchronized (compareTableRecordMap) {
            if (compareTableRecordMap.containsKey(tableName)) {
                return;
            }
            CompareTableRecord record = CompareTableRecord.builder().tableName(tableName).build();
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
    public void updateProgress(String tableName, int count) {
        if (!compareTableRecordMap.containsKey(tableName)) {
            return;
        }

        CompareTableRecord record = compareTableRecordMap.get(tableName);
        record.setProgress(((double) (count + 1) / (double) record.getRowCount()));
        record.setMemo(String.format("%,d/%,d", (count + 1), record.getRowCount()));
    }
}

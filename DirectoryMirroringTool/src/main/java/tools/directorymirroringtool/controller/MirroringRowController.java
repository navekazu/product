package tools.directorymirroringtool.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tools.directorymirroringtool.process.MirroringNotify;
import tools.directorymirroringtool.process.MirroringParameter;
import tools.directorymirroringtool.process.MirroringProcess;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MirroringRowController implements Initializable, MirroringNotify {
    public static final String FXML_PATH = "/fxml/MirroringRow.fxml";
    public static final String WINDOW_TITLE = "";

    @FXML Label nameLabel;
    @FXML TextField targetPathField;
    @FXML TextField backupPathField;
    @FXML Label statusLabel;
    @FXML Label backupIntervalLabel;
    @FXML Label lastBackupDateLabel;
    @FXML Label nextBackupDateLabel;

    MirroringProcess mirroringProcess;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMirroringProcess(MirroringProcess mirroringProcess) {
        if (this.mirroringProcess!=null) {
            this.mirroringProcess.setMirroringNotify(null);
        }
        this.mirroringProcess = mirroringProcess;
        this.mirroringProcess.setMirroringNotify(this);
    }

    @Override
    public void requestUpdateUi() {
        Platform.runLater(() -> updateUi());
    }
    public void updateUi() {
        MirroringParameter param = mirroringProcess.getParam();
        nameLabel.setText(param.getName());
        targetPathField.setText(param.getSourcePath().toString());
        backupPathField.setText(param.getSinkPath().toString());
        statusLabel.setText(param.getStatus().getLabel());

        backupIntervalLabel.setText(getIntervalString(param.getSyncInterval()));
        lastBackupDateLabel.setText(getDateString(param.getLastBackupDate()));
        nextBackupDateLabel.setText(getDateString(param.getNextBackupDate()));
    }
    static String getIntervalString(int syncInterval) {
        String hour = (syncInterval/60>=1? String.format("%d hour%s", syncInterval/60, (syncInterval/60!=1? "s": "")): "");
        String minute = (syncInterval%60!=0? String.format("%d minute%s", syncInterval%60, (syncInterval%60!=1? "s": "")): "");
        String hourAndMinute = hour+" "+minute;
        return hourAndMinute.trim()+".";
    }
    static String getDateString(Date date) {
        return String.format("%tY/%tm/%td %tH:%tM", date, date, date, date, date);
    }

    @FXML
    public void handleChangeRegularlyExecute(ActionEvent event) {
        mirroringProcess.changeRegularlyExecute();
        updateUi();
    }

    @FXML
    public void handleExecuteImmediately(ActionEvent event) {
        mirroringProcess.immediateExecute();
    }

    @FXML
    public void handleStop(ActionEvent event) {
        mirroringProcess.emergencyStop();
    }

}

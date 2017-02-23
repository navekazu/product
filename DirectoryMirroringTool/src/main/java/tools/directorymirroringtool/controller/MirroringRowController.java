package tools.directorymirroringtool.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tools.directorymirroringtool.process.MirroringProcess;

import java.net.URL;
import java.util.ResourceBundle;

public class MirroringRowController implements Initializable {
    public static final String FXML_PATH = "/fxml/MirroringRow.fxml";
    public static final String WINDOW_TITLE = "";

    @FXML Label nameLabel;
    @FXML TextField targetPathField;
    @FXML TextField backupPathField;
    @FXML Label statusLabel;

    MirroringProcess mirroringProcess;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMirroringProcess(MirroringProcess mirroringProcess) {
        this.mirroringProcess = mirroringProcess;
    }

    public void updateUi() {
        nameLabel.setText(mirroringProcess.getSourcePath().getFileName().toString());
        targetPathField.setText(mirroringProcess.getSourcePath().toString());
        backupPathField.setText(mirroringProcess.getSinkPath().toString());
    }
}

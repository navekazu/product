package tools.directorymirroringtool.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import tools.directorymirroringtool.App;
import tools.directorymirroringtool.process.MirroringManager;
import tools.directorymirroringtool.process.MirroringProcess;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public static final String FXML_PATH = "/fxml/Main.fxml";
    public static final String WINDOW_TITLE = "Mirroring tool";

    @FXML
    ListView mirroringList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MirroringManager mirroringManager = App.getMirroringManager();
        mirroringManager.getMirroringProcessList().stream().forEach(this::addMirroringRow);
    }

    void addMirroringRow(MirroringProcess mirroringProcess) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MirroringRowController.FXML_PATH));

        try {
            Parent node = fxmlLoader.load();
            MirroringRowController mirroringRowController = fxmlLoader.getController();
            mirroringRowController.setMirroringProcess(mirroringProcess);
            mirroringRowController.updateUi();

            mirroringList.getItems().add(node);
            //mirroringList.requestLayout();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

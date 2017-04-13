package tools.filer.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextField addressField;

    @FXML
    private TextField filterField;

    @FXML
    private TextField commandField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}

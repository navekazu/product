package tools.dbcomparator2.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectController implements Initializable {

    @FXML private TextField libraryPathField;
    @FXML private TextField driverField;
    @FXML private TextField urlField;
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;
    @FXML private TextField schemaField;

    private static File fileChooserInitialDirectory = new File(System.getProperty("user.dir"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleReferenceButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));
        fileChooser.setInitialDirectory(fileChooserInitialDirectory);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile!=null) {
            libraryPathField.setText(selectedFile.getPath());
            fileChooserInitialDirectory = selectedFile.getParentFile();
        }
    }

    @FXML
    private void handleBackslashToSlashButton(ActionEvent event) {
        String url = urlField.getText();
        urlField.setText(ConnectController.backslashToSlash(url));
    }

    static String backslashToSlash(String value) {
        return value.replaceAll("\\\\", "/");
    }


    public String getLibraryPath() {
        return libraryPathField.getText();
    }

    public String getDriver() {
        return driverField.getText();
    }

    public String getUrl() {
        return urlField.getText();
    }

    public String getUser() {
        return userField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getSchema() {
        return schemaField.getText();
    }








    public void setLibraryPath(String value) {
        libraryPathField.setText(value);
    }

    public void setDriver(String value) {
        driverField.setText(value);
    }

    public void setUrl(String value) {
        urlField.setText(value);
    }

    public void setUser(String value) {
        userField.setText(value);
    }

    public void setPassword(String value) {
        passwordField.setText(value);
    }

    public void setSchema(String value) {
        schemaField.setText(value);
    }

}

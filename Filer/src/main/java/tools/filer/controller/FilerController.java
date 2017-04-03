package tools.filer.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import tools.filer.FilerInterface;
import tools.filer.command.CommandHandler;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class FilerController implements FilerInterface, Initializable {
    @FXML
    private TreeView directoryTree;

    @FXML
    private TableView fileListTable;

    private CommandHandler commandHandler;

    public FilerController() {
        this.commandHandler = new CommandHandler(this);
    }

    @Override
    public Path getCurrentDirectory() {
        return null;
    }

    @Override
    public void setCurrentDirectory(Path path) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

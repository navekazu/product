package tools.filer.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import tools.filer.FilerInterface;

import java.nio.file.Path;

public class FilerController implements FilerInterface {
    @FXML
    private TreeView directoryTree;

    @FXML
    private TableView fileListTable;

    @Override
    public Path getCurrentDirectory() {
        return null;
    }

    @Override
    public void setCurrentDirectory(Path path) {

    }
}

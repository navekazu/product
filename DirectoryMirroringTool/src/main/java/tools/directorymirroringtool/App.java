package tools.directorymirroringtool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.directorymirroringtool.controller.MainController;
import tools.directorymirroringtool.controller.SystemTrayController;
import tools.directorymirroringtool.process.MirroringManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class App extends Application implements SystemTrayEvent {
    static MirroringManager mirroringManager = new MirroringManager();
    static SystemTrayController systemTrayController = new SystemTrayController();

    public static MirroringManager getMirroringManager() {
        return mirroringManager;
    }

    public App() {
    }

    public void launchApp(String[] args) {
        getMirroringManager().createMirroringProcess(Paths.get("D:\\work\\test01\\source"), Paths.get("D:\\work\\test01\\sink"));
        getMirroringManager().createMirroringProcess(Paths.get("D:\\work\\test02\\source"), Paths.get("D:\\work\\test02\\sink"));
        getMirroringManager().createMirroringProcess(Paths.get("D:\\work\\test03\\source"), Paths.get("D:\\work\\test03\\sink"));
//        getMirroringManager().createMirroringProcess(Paths.get("D:\\work\\test04\\source"), Paths.get("D:\\work\\test04\\sink"));
//        getMirroringManager().createMirroringProcess(Paths.get("D:\\work\\test05\\source"), Paths.get("D:\\work\\test05\\sink"));
//        getMirroringManager().createMirroringProcess(Paths.get("D:\\work\\test06\\source"), Paths.get("D:\\work\\test06\\sink"));

        systemTrayController.startOnTray(this);
        Application.launch(App.class, args);
    }


    public static void main(String[] args) {
        App app = new App();
        app.launchApp(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MainController.FXML_PATH));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle(MainController.WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                exit();
            }
        });
    }

    @Override
    public void showWindow() {

    }

    @Override
    public void exit() {
        systemTrayController.shutdown();
        mirroringManager.shutdown();
        Platform.exit();
    }
}

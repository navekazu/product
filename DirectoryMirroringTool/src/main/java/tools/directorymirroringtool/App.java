package tools.directorymirroringtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tools.directorymirroringtool.controller.MainController;
import tools.directorymirroringtool.process.MirroringManager;

import java.nio.file.Paths;

public class App extends Application {
    static MirroringManager mirroringManager = new MirroringManager();

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
    }
}

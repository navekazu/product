package tools.directorymirroringtool;

import javafx.application.Application;
import javafx.stage.Stage;
import tools.directorymirroringtool.process.MirroringManager;

import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        MirroringManager mirroringManager = new MirroringManager();
        mirroringManager.createMirroringProcess(Paths.get("D:\\work\\test01\\source"), Paths.get("D:\\work\\test01\\sink"));
//        mirroringManager.createMirroringProcess(Paths.get("D:\\work\\test02\\source"), Paths.get("D:\\work\\test02\\sink"));
//        mirroringManager.createMirroringProcess(Paths.get("D:\\work\\test03\\source"), Paths.get("D:\\work\\test03\\sink"));

    }

    class FxLauncher extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {

        }
    }
}

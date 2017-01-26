package tools.mailer.processor;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.mailer.di.anntation.Plugin;
import tools.mailer.di.anntation.Process;
import tools.mailer.di.anntation.ProcessType;
import tools.mailer.di.container.DIContainer;

@Plugin
public class ApplicationProcessor extends Application {

    @Process(processType= ProcessType.BOOT_APPLICATION)
    public void boot() {
        Application.launch(new String[]{});
    }

    @Process(processType= ProcessType.STARTED_APPLICATION)
    public void started() {
        System.out.println("started");
        DIContainer.getInstance().fireEvent(ProcessType.RECV_MAIL);
    }

    @Process(processType= ProcessType.TERM_APPLICATION)
    public void term() {
        System.out.println("term");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Mailer");
        primaryStage.setScene(scene);

        primaryStage.setOnShown(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event) {
                DIContainer.getInstance().fireEvent(ProcessType.STARTED_APPLICATION);
            }
        });
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event) {
                DIContainer.getInstance().fireEvent(ProcessType.TERM_APPLICATION);
            }
        });

        primaryStage.show();
    }
}

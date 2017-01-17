package tools.addressprinter;


import javafx.application.Application;
import javafx.scene.Scene;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        Print print = new Print();
        print.showAvailablePrinters();

        Application.launch(Print.class, args);
    }

}

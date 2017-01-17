package tools.addressprinter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.print.JobSettings;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.stage.Stage;
import tools.addressprinter.controller.JapanesePostcardVController;
import tools.addressprinter.entity.AddressPoint;
import tools.addressprinter.entity.Data;
import tools.addressprinter.entity.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Print extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Print print = new Print();
        Data data = new Data();
        data.destinationPoint = new AddressPoint();
        data.destinationPoint.zipNo = new Point();
        data.destinationPoint.zipNo.x = 0.4;
        data.destinationPoint.zipNo.y = 0.1;
        print.print(data);

    }

    public List<Printer> getAvailablePrinters() {
        List<Printer> list = new ArrayList<>();

//        Printer defaultPrinter = Printer.getDefaultPrinter();
//        outputPrinterInformation(defaultPrinter);

//        Collection<Printer> collection = Printer.getAllPrinters();
//        collection.stream().forEach(this::outputPrinterInformation);

        return list;
    }

    private boolean isAvailablePrinterForAddressPrinter(Printer printer) {
        return printer.getPrinterAttributes().getSupportedPapers().stream()
                .filter(paper -> Paper.JAPANESE_POSTCARD==paper)
//                .filter(paper -> Paper.A0==paper)
                .count()!=0;
    }

    public void showAvailablePrinters() {
        Printer defaultPrinter = Printer.getDefaultPrinter();

        List<Printer> availablePrinterList = Printer.getAllPrinters().stream()
                .filter(this::isAvailablePrinterForAddressPrinter)
                .collect(toList());

        IntStream.range(0, availablePrinterList.size())
                .forEach(i -> {
                    System.out.println(String.format("%2d%s %s", i+1, (availablePrinterList.get(i).equals(defaultPrinter)? "*": " "), availablePrinterList.get(i).getName()));
                });
    }

    public void print(Data data) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/JapanesePostcardV.fxml"));
        Node root = loader.load();
        root.autosize();
        JapanesePostcardVController controller = loader.getController();
        controller.setData(data);

        PrinterJob job = PrinterJob.createPrinterJob();
        job.printPage(root);
        job.endJob();
    }

}

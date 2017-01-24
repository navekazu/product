package tools.addressprinter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import tools.addressprinter.controller.JapanesePostcardVController;
import tools.addressprinter.entity.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Print extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Print print = new Print();

        Data data = createSampleData();
        print.print(data);

        Platform.exit();

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
        JapanesePostcardVController controller = loader.getController();

        PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = job.getPrinter();
        JobSettings jobSettings = job.getJobSettings();
        PageLayout pageLayout = printer.createPageLayout(Paper.JAPANESE_POSTCARD, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        jobSettings.setPageLayout(pageLayout);
//        pageLayout.
//        jobSettings.set

//        double h1 = pageLayout.getPrintableHeight();
//        double h2 = root. .maxHeight();

        root.resize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
//        root.maxHeight(pageLayout.getPrintableHeight());
//        root.prefHeight(pageLayout.getPrintableHeight());
//        root.maxWidth(pageLayout.getPrintableWidth());
//        root.prefWidth(pageLayout.getPrintableWidth());
//        root.autosize();
        controller.setData(data, 0);

        job.printPage(root);
        job.endJob();
    }

    Data createSampleData() {
        Data data = new Data();
        data.destinationPoint = new AddressPoint();
        data.destinationPoint.zipNo = new Point();
        data.destinationPoint.zipNo.x = 0.4;
        data.destinationPoint.zipNo.y = 0.05;
        data.destinationPoint.addressUpper = new Point();
        data.destinationPoint.addressUpper.x = 0.3;
        data.destinationPoint.addressUpper.y = 0.3;
        data.destinationPoint.addressLower = new Point();
        data.destinationPoint.addressLower.x = 0.3;
        data.destinationPoint.addressLower.y = 0.4;
        data.destinationPoint.person = new Point();
        data.destinationPoint.person.x = 0.3;
        data.destinationPoint.person.y = 0.5;

        data.senderPoint = new AddressPoint();
        data.senderPoint.zipNo = new Point();
        data.senderPoint.zipNo.x = 0.1;
        data.senderPoint.zipNo.y = 0.85;
        data.senderPoint.zipNo.font = new Font();
        data.senderPoint.zipNo.font.size = 8;
        data.senderPoint.addressUpper = new Point();
        data.senderPoint.addressUpper.x = 0.1;
        data.senderPoint.addressUpper.y = 0.7;
        data.senderPoint.addressUpper.font = new Font();
        data.senderPoint.addressUpper.font.size = 8;
        data.senderPoint.addressLower = new Point();
        data.senderPoint.addressLower.x = 0.1;
        data.senderPoint.addressLower.y = 0.73;
        data.senderPoint.addressLower.font = new Font();
        data.senderPoint.addressLower.font.size = 8;
        data.senderPoint.person = new Point();
        data.senderPoint.person.x = 0.1;
        data.senderPoint.person.y = 0.76;
        data.senderPoint.person.font = new Font();
        data.senderPoint.person.font.size = 8;

        Address address = new Address();
        address.zipNo = new ZipNo();
        address.zipNo.sectionNo = "100";
        address.zipNo.cityNo = "0001";
        address.address1 = "東京都";
        address.address2 = "千代田区1丁目";
        address.address3 = "コープ千代千代101";
        address.useFamilyNameForEveryone = false;

        address.personList = new ArrayList<>();

        Person person;
        person = new Person();
        person.familyName = "山田";
        person.name = "太郎";
        address.personList.add(person);

        person = new Person();
        person.familyName = "山田";
        person.name = "花子";
        address.personList.add(person);

        data.addressList = new ArrayList<>();
        data.addressList.add(address);

        data.font = new Font();

        address = new Address();
        address.zipNo = new ZipNo();
        address.zipNo.sectionNo = "100";
        address.zipNo.cityNo = "0101";
        address.address1 = "東京都";
        address.address2 = "大島町元町1丁目";
        address.address3 = "コープ元町201";
        address.useFamilyNameForEveryone = false;

        address.personList = new ArrayList<>();

        person = new Person();
        person.familyName = "宮川";
        person.name = "大助";
        address.personList.add(person);

        person = new Person();
        person.familyName = "宮川";
        person.name = "花子";
        address.personList.add(person);

        data.sender = address;
        data.printSender=true;

        return data;
    }
}

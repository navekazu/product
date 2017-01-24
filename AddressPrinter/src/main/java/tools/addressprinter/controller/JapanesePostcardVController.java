package tools.addressprinter.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import tools.addressprinter.entity.Address;
import tools.addressprinter.entity.Data;
import tools.addressprinter.entity.Font;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class JapanesePostcardVController implements Initializable {
    @FXML private Label zipNo;
    @FXML private Label address1;
    @FXML private Label address2;
    @FXML private Label address3;
    @FXML private Label familyName1;
    @FXML private Label name1;
    @FXML private Label familyName2;
    @FXML private Label name2;
    @FXML private Label familyName3;
    @FXML private Label name3;
    @FXML private Label familyName4;
    @FXML private Label name4;
    @FXML private Label familyName5;
    @FXML private Label name5;
    @FXML private Label senderZipNo;
    @FXML private Label senderAddress1;
    @FXML private Label senderAddress2;
    @FXML private Label senderAddress3;
    @FXML private Label senderFamilyName1;
    @FXML private Label senderName1;
    @FXML private Label senderFamilyName2;
    @FXML private Label senderName2;
    @FXML private Label senderFamilyName3;
    @FXML private Label senderName3;

    private javafx.scene.text.Font baseFont;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        baseFont = javafx.scene.text.Font.font(zipNo.getFont().getName(), zipNo.getFont().getSize());
        zipNo.getParent().getChildrenUnmodifiable().stream()
                .filter(child -> (child instanceof Label))
                .forEach(child -> ((Label)child).setText(""));
    }

    private javafx.scene.text.Font createFont(Data data, Font font) {
        Font f = (font==null? (data.font==null? null: data.font): font);
        if (f==null) {
            return baseFont;
        }
        return javafx.scene.text.Font.font(f.name, f.size);
    }

    public void setData(Data data, int index) {
        Region parent = (Region)zipNo.getParent();
        double width = parent.getWidth();
        double height = parent.getHeight();

        Address address = data.addressList.get(index);
        zipNo.setText(address.zipNo.sectionNo+" "+address.zipNo.cityNo);
        zipNo.setLayoutX(width*data.destinationPoint.zipNo.x);
        zipNo.setLayoutY(height*data.destinationPoint.zipNo.y);
        zipNo.setFont(createFont(data, data.destinationPoint.zipNo.font));
        address1.setText(address.address1+address.address2);
        address1.setLayoutX(width*data.destinationPoint.addressUpper.x);
        address1.setLayoutY(width*data.destinationPoint.addressUpper.y);
        address1.setFont(createFont(data, data.destinationPoint.addressUpper.font));
//        address2.setText(address.address2);
        address2.setText("");
        address2.setLayoutX(width*data.destinationPoint.addressUpper.x);
        address2.setLayoutY(width*data.destinationPoint.addressUpper.y);
        address2.setFont(createFont(data, data.destinationPoint.addressUpper.font));
        address3.setText(address.address3);
        address3.setLayoutX(width*data.destinationPoint.addressLower.x);
        address3.setLayoutY(width*data.destinationPoint.addressLower.y);
        address3.setFont(createFont(data, data.destinationPoint.addressLower.font));

        final Label[] destinationFamilyNames = new Label[]{familyName1, familyName2, familyName3, familyName4, familyName5};
        final Label[] destinationNames = new Label[]{name1, name2, name3, name4, name5};
        IntStream.range(0, address.personList.size()).forEach(i -> {
            destinationFamilyNames[i].setText((i==0 || address.useFamilyNameForEveryone)? address.personList.get(i).familyName: "");
            destinationFamilyNames[i].setLayoutX(width * data.destinationPoint.person.x);
            destinationFamilyNames[i].setLayoutY(width * (data.destinationPoint.person.y + (i * 0.05)));
            destinationFamilyNames[i].setFont(createFont(data, data.destinationPoint.person.font));

            destinationNames[i].setText(address.personList.get(i).name);
            destinationNames[i].setLayoutX(width*(data.destinationPoint.person.x+0.2));
            destinationNames[i].setLayoutY(width*(data.destinationPoint.person.y+(i*0.05)));
            destinationNames[i].setFont(createFont(data, data.destinationPoint.person.font));
        });

        senderZipNo.setText(data.printSender? data.sender.zipNo.sectionNo+" "+data.sender.zipNo.cityNo: "");
        senderZipNo.setLayoutX(width*data.senderPoint.zipNo.x);
        senderZipNo.setLayoutY(height*data.senderPoint.zipNo.y);
        senderZipNo.setFont(createFont(data, data.senderPoint.zipNo.font));
        senderAddress1.setText(data.printSender? data.sender.address1+data.sender.address2: "");
        senderAddress1.setLayoutX(width*data.senderPoint.addressUpper.x);
        senderAddress1.setLayoutY(height*data.senderPoint.addressUpper.y);
        senderAddress1.setFont(createFont(data, data.senderPoint.addressUpper.font));
        senderAddress2.setText("");
        senderAddress2.setLayoutX(width*data.senderPoint.addressUpper.x);
        senderAddress2.setLayoutY(height*data.senderPoint.addressUpper.y);
        senderAddress2.setFont(createFont(data, data.senderPoint.addressUpper.font));
        senderAddress3.setText(data.printSender? data.sender.address3: "");
        senderAddress3.setLayoutX(width*data.senderPoint.addressLower.x);
        senderAddress3.setLayoutY(height*data.senderPoint.addressLower.y);
        senderAddress3.setFont(createFont(data, data.senderPoint.addressLower.font));

        final Label[] senderFamilyNames = new Label[]{senderFamilyName1, senderFamilyName2, senderFamilyName3};
        final Label[] senderNames = new Label[]{senderName1, senderName2, senderName3};
        IntStream.range(0, data.sender.personList.size()).forEach(i -> {
            senderFamilyNames[i].setText((data.printSender && (i==0 || data.sender.useFamilyNameForEveryone))? data.sender.personList.get(i).familyName: "");
            senderFamilyNames[i].setLayoutX(width * data.senderPoint.person.x);
            senderFamilyNames[i].setLayoutY(height * (data.senderPoint.person.y + (i * 0.02)));
            senderFamilyNames[i].setFont(createFont(data, data.senderPoint.person.font));

            senderNames[i].setText(data.printSender? data.sender.personList.get(i).name: "");
            senderNames[i].setLayoutX(width*(data.senderPoint.person.x+0.1));
            senderNames[i].setLayoutY(height*(data.senderPoint.person.y+(i*0.02)));
            senderNames[i].setFont(createFont(data, data.senderPoint.person.font));
        });
    }
}

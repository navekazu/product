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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setData(Data data, int index) {
        Region parent = (Region)zipNo.getParent();
        double width = parent.getWidth();
        double height = parent.getHeight();

        Address address = data.addressList.get(index);
        zipNo.setText(address.zipNo.sectionNo+" "+address.zipNo.cityNo);
        zipNo.setLayoutX(width*data.destinationPoint.zipNo.x);
        zipNo.setLayoutY(height*data.destinationPoint.zipNo.y);
        address1.setText(address.address1);
        address1.setLayoutX(width*data.destinationPoint.addressUpper.x);
        address1.setLayoutY(width*data.destinationPoint.addressUpper.y);
        address2.setText(address.address2);
        address2.setLayoutX(width*data.destinationPoint.addressUpper.x);
        address2.setLayoutY(width*data.destinationPoint.addressUpper.y);
        address3.setText(address.address3);
        address3.setLayoutX(width*data.destinationPoint.addressLower.x);
        address3.setLayoutY(width*data.destinationPoint.addressLower.y);

        final Label[] familyNames = new Label[]{familyName1, familyName2, familyName3, familyName4, familyName5};
        final Label[] names = new Label[]{name1, name2, name3, name4, name5};
        IntStream.range(0, address.personList.size()).forEach(i -> {
            if (i==0 || address.useFamilyNameForEveryone) {
                familyNames[i].setText(address.personList.get(i).familyName);
                familyNames[i].setLayoutX(width * data.destinationPoint.person.x);
                familyNames[i].setLayoutY(width * (data.destinationPoint.person.y + (i * 0.05)));
            }
            names[i].setText(address.personList.get(i).name);
            names[i].setLayoutX(width*(data.destinationPoint.person.x+0.2));
            names[i].setLayoutY(width*(data.destinationPoint.person.y+(i*0.05)));
        });

        senderZipNo.setLayoutX(width*data.senderPoint.zipNo.x);
        senderZipNo.setLayoutY(height*data.senderPoint.zipNo.y);
        senderAddress1.setLayoutX(width*data.senderPoint.addressUpper.x);
        senderAddress1.setLayoutY(height*data.senderPoint.addressUpper.y);
        senderAddress2.setLayoutX(width*data.senderPoint.addressUpper.x);
        senderAddress2.setLayoutY(height*data.senderPoint.addressUpper.y);
        senderAddress3.setLayoutX(width*data.senderPoint.addressLower.x);
        senderAddress3.setLayoutY(height*data.senderPoint.addressLower.y);
        senderFamilyName1.setLayoutX(width*data.senderPoint.person.x);
        senderFamilyName1.setLayoutY(height*data.senderPoint.person.y);
        senderName1.setLayoutX(width*data.senderPoint.person.x);
        senderName1.setLayoutY(height*data.senderPoint.person.y);
    }
}

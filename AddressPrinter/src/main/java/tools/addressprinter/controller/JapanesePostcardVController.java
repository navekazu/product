package tools.addressprinter.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import tools.addressprinter.entity.Data;

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setData(Data data) {
        Region parent = (Region)zipNo.getParent();
        double width = parent.getWidth();
        double height = parent.getHeight();

        zipNo.setLayoutX(width*data.destinationPoint.zipNo.x);
        zipNo.setLayoutY(height*data.destinationPoint.zipNo.y);
        address1.setLayoutX(width*data.destinationPoint.addressUpper.x);
        address1.setLayoutX(width*data.destinationPoint.addressUpper.y);
        address2.setLayoutX(width*data.destinationPoint.addressUpper.x);
        address2.setLayoutX(width*data.destinationPoint.addressUpper.y);
        address3.setLayoutX(width*data.destinationPoint.addressLower.x);
        address3.setLayoutX(width*data.destinationPoint.addressLower.y);
        familyName1.setLayoutX(width*data.destinationPoint.person.x);
        familyName1.setLayoutX(width*data.destinationPoint.person.y);
        name1.setLayoutX(width*data.destinationPoint.person.x);
        name1.setLayoutX(width*data.destinationPoint.person.y);

    }
}

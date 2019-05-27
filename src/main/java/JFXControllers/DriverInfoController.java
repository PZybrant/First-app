package JFXControllers;

import SQL.Driver;
import SQL.Forwarder;
import SQL.Truck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DriverInfoController {

    private Stage stage;
    private Driver driver;
    private Truck truck;
    private Forwarder forwarder;

    @FXML
    private Button closeButton;

    @FXML
    private Label driverIdLabel, driverFirstNameLabel, driverLastNameLabel, driverAgeLabel,
            driverPhoneNumberLabel, driverIsOnRoadLabel, brandLabel, modelLabel, plateNumberLabel
            , mileageLabel, forwarderIdLabel, forwarderFirstNameLabel, forwarderLastNameLabel;

    public DriverInfoController(Driver driver){
        this.driver = driver;
        truck = driver.getTruck();
        forwarder = driver.getForwarder();

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DriverInfoWindow" +
                    ".fxml"));
            loader.setController(this);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException ioe){
            ioe.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        initializeCloseButton();
        initializeLabels();
    }

    private void initializeCloseButton() {
        closeButton.setOnAction(e -> stage.close());
    }

    private void initializeLabels() {
        driverIdLabel.setText(String.valueOf(driver.getIdNumber()));
        driverFirstNameLabel.setText(driver.getFirstName());
        driverLastNameLabel.setText(driver.getLastName());
        driverAgeLabel.setText(String.valueOf(driver.getAge()));
        driverPhoneNumberLabel.setText(driver.getPhoneNumber());
        if(driver.isOnRoad()){
            driverIsOnRoadLabel.setText("Yes");
        } else {
            driverIsOnRoadLabel.setText("No");
        }

        if(truck != null){
            brandLabel.setText(truck.getBrand());
            modelLabel.setText(truck.getModel());
            plateNumberLabel.setText(truck.getPlateNumber());
            mileageLabel.setText(String.valueOf(truck.getMileage()));
        }

        forwarderIdLabel.setText(String.valueOf(forwarder.getIdNumber()));
        forwarderFirstNameLabel.setText(forwarder.getFirstName());
        forwarderLastNameLabel.setText(forwarder.getLastName());
    }
}

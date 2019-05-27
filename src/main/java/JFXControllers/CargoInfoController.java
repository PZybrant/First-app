package JFXControllers;

import SQL.Cargo;
import SQL.Company;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CargoInfoController {

    private Cargo cargo;
    private Company sender, customer;
    private Stage stage;

    @FXML
    private Button closeButton;

    @FXML
    private TextArea cargoDescriptionTextArea;

    @FXML
    private Label cargoTypeLabel, cargoDistanceLabel, cargoOrderDateLabel, cargoDeliveryDateLabel,
            senderNameLabel, senderCountryLabel, senderCityLabel, senderStreetLabel,
            senderZipCodeLabel, customerNameLabel, customerCountryLabel, customerCityLabel,
            customerStreetLabel, customerZipCodeLabel;

    public CargoInfoController(Cargo cargo){
        this.cargo = cargo;
        sender = cargo.getSender();
        customer = cargo.getCustomer();

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CargoInfoWindow" +
                    ".fxml"));
            loader.setController(this);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
        } catch(IOException ioe){
            ioe.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        initializeCloseButton();
        initializeLabels();
    }

    private void initializeCloseButton() {
        closeButton.setOnAction(e -> stage.close());
    }

    private void initializeLabels() {
        cargoTypeLabel.setText(cargo.getType());
        cargoDistanceLabel.setText(String.valueOf(cargo.getDistance()));
        cargoOrderDateLabel.setText(cargo.getDateOfOrder().toString());
        if(cargo.getDeliveryDate() != null){
            cargoDeliveryDateLabel.setText(cargo.getDeliveryDate().toString());
        } else{
            cargoDeliveryDateLabel.setText("Cargo is still on road");
        }
        cargoDescriptionTextArea.setText(cargo.getDescription());

        senderNameLabel.setText(sender.getCompanyName());
        senderCountryLabel.setText(sender.getCountry());
        senderCityLabel.setText(sender.getCity());
        senderStreetLabel.setText(sender.getStreet());
        senderZipCodeLabel.setText(sender.getZipCode());

        customerNameLabel.setText(customer.getCompanyName());
        customerCountryLabel.setText(customer.getCountry());
        customerCityLabel.setText(customer.getCity());
        customerStreetLabel.setText(customer.getStreet());
        customerZipCodeLabel.setText(customer.getZipCode());
    }
}

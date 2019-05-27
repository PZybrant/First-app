package JFXControllers;

import SQL.Cargo;
import SQL.Company;
import SQL.DatabaseConnector;
import SQL.Driver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddCargoController {

    private DatabaseConnector databaseConnector;
    private SelectCompanyController selectCompanyController;
    private Company sender, customer;
    private Driver currentDriver;
    private Cargo newCargo;

    private String description, cargoType;
    private int distance;
    private Scene scene;
    private Stage stage;

    @FXML
    private Label senderLabel, customerLabel, warningMessage;

    @FXML
    private TextField distanceTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button setSenderButton, setCustomerButton, cancelButton, applyButton;

    @FXML
    private ChoiceBox<String> cargoTypeChoiceBox;

    public AddCargoController(Driver currentDriver){
        stage = new Stage();
        databaseConnector = new DatabaseConnector();
        this.currentDriver = currentDriver;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCargoWindow.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        initializeCargoTypeChoiceBox();
        initializeSetSenderButton();
        initializeSetCustomerButton();
        initializeDistanceTextField();
        initializeCancelButton();
        initializeApplyButton();
        initializeOnStageCloseRequest();
    }

    private void initializeOnStageCloseRequest() {
        stage.setOnCloseRequest(e -> {
            e.consume();
            boolean closeStatus = new ConfirmController().display("Closing", "Are you sure you " +
                    "want to close the application?");

            if (closeStatus) {
                stage.close();
            }
        });
    }

    private void initializeCargoTypeChoiceBox() {
        cargoTypeChoiceBox.getItems().addAll("High_valued_cargo", "Highly_perishable _argo",
                "Powders", "Oversize", "Dangerous", "Liquids", "Groupage");
    }

    private void initializeSetSenderButton() {
        setSenderButton.setOnAction(e -> {
            selectCompanyController = new SelectCompanyController(scene, this, "sender");
        });
    }

    private void initializeSetCustomerButton() {
        setCustomerButton.setOnAction(e -> {
            selectCompanyController = new SelectCompanyController(scene, this, "customer");
        });
    }

    private void initializeDistanceTextField() {
        distanceTextField.setOnKeyReleased(keyEvent -> {
            if(!keyEvent.getCode().isDigitKey() && keyEvent.getCode().getCode() != 8){
                distanceTextField.deletePreviousChar();
            }
        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(e -> {
            boolean closeStatus = new ConfirmController().display("Closing", "Are you sure you?");
            if(closeStatus){
                stage.close();
            }
        });
    }

    private void initializeApplyButton() {
        applyButton.setOnAction(e -> {

            cargoType = cargoTypeChoiceBox.getValue().toUpperCase();
            distance = Integer.parseInt(distanceTextField.getText());
            description = descriptionTextArea.getText().toUpperCase();

            if(!cargoType.isEmpty() && distance > 0 && sender != null && customer != null && sender != customer && !description.isEmpty()){
                try{
                    databaseConnector.getEntityManager().getTransaction().begin();
                    newCargo = new Cargo(cargoType, distance, sender,
                            customer);
                    databaseConnector.getEntityManager().persist(newCargo);
                    newCargo.setDescription(description);
                    newCargo.setDriver(currentDriver);
                    databaseConnector.getEntityManager().merge(newCargo);
                    currentDriver.addCargo(newCargo);
                    currentDriver.setOnRoad(true);
                    databaseConnector.getEntityManager().merge(currentDriver);
                    databaseConnector.getEntityManager().getTransaction().commit();
                    new AlertController("SUCCESS", "Cargo created successful");
                    stage.close();
                } catch (Exception ex){
                    warningMessage.setText("Cargo can't be created");
                    ex.printStackTrace();
                }
            } else {
                warningMessage.setText("Something goes wrong! Check all fields.");
            }

        });
    }

    public void setSender(Company company){
        this.sender = company;
        senderLabel.setText(sender.getCompanyName());
    }

    public void setCustomer(Company company){
        this.customer = company;
        customerLabel.setText(customer.getCompanyName());
    }

    public Cargo getNewCargo() {
        return newCargo;
    }
}

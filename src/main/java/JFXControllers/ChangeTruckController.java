package JFXControllers;

import SQL.DatabaseConnector;
import SQL.Driver;
import SQL.Truck;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ChangeTruckController {

    private DatabaseConnector databaseConnector;
    private Driver currentDriver;
    private Truck currentTruck, newTruck;

    private List<Truck> truckList;
    private Stage stage;

    @FXML
    private Label currentTruckBrandLabel, currentTruckModelLabel, currentTruckPlateNumberLabel,
    currentTruckMileageLabel, newTruckBrandLabel, newTruckModelLabel,
    newTruckPlateNumberLabel, newTruckMileageLabel;

    @FXML
    private TableView<Truck> truckTableView;
    private ObservableList<Truck> truckObservableList;

    @FXML
    private TableColumn<Truck, String> brandColumn, modelColumn, plateNumberColumn;

    @FXML
    private TableColumn<Truck, Integer> mileageColumn;

    @FXML
    private TableColumn<Truck, Boolean> hasDriverColumn;

    @FXML
    private Button cancelButton, applyButton;

    public ChangeTruckController(Driver currentDriver){
        stage = new Stage();
        databaseConnector = new DatabaseConnector();
        this.currentDriver = currentDriver;
        this.currentTruck = currentDriver.getTruck();
        truckObservableList = FXCollections.observableArrayList();

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChangeTruckWindow" +
                    ".fxml"));
            loader.setController(this);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        initializeCancelButton();
        initializeApplyButton();
        initializeLabels();
        initializeTableView();
        initializeOnStageCloseRequest();
    }

    private void initializeOnStageCloseRequest() {
        stage.setOnCloseRequest(e -> {
            e.consume();
            boolean closeStatus = new ConfirmController().display("Closing",
                    "Are you sure you want to close this window?");

            if(closeStatus){
                stage.close();
            }
        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(e -> {
            boolean closeStatus = new ConfirmController().display("Closing", "Are you sure you " +
                    "want to cancel this operation?");
            if(closeStatus){
                stage.close();
            }
        });
    }

    private void initializeApplyButton() {

        applyButton.setOnAction(e -> {
            if(newTruck != null){
                if(!newTruck.hasDriver()){
                    if(!currentDriver.isOnRoad()){
                            boolean status = new ConfirmController().display("Changing forwarder", "Are you " +
                                    "sure you want to change truck?");
                            if(status) {
                                try {
                                    databaseConnector.getEntityManager().getTransaction().begin();
                                    if (currentTruck != null) {
                                        currentTruck.setCurrentDriver(null);
                                        databaseConnector.getEntityManager().merge(currentTruck);
                                    }
                                    currentDriver.setTruck(newTruck);
                                    newTruck.setCurrentDriver(currentDriver);
                                    databaseConnector.getEntityManager().merge(currentDriver);
                                    databaseConnector.getEntityManager().merge(newTruck);
                                    databaseConnector.getEntityManager().getTransaction().commit();
                                    new AlertController("SUCCESS", "The truck changed " +
                                            "successfully!");
                                    stage.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                    } else {
                        new AlertController("ERROR", "Can't change truck while driver is on road!");
                    }
                } else {
                    new AlertController("ERROR", "Can't change truck that already has a driver!");
                }
            }
        });
    }

    private void initializeLabels() {
        if(currentTruck != null){
            currentTruckBrandLabel.setText(currentTruck.getBrand());
            currentTruckModelLabel.setText(currentTruck.getModel());
            currentTruckPlateNumberLabel.setText(currentTruck.getPlateNumber());
            currentTruckMileageLabel.setText(String.valueOf(currentTruck.getMileage()));
        }

    }

    private void initializeTableView() {
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        plateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        hasDriverColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().hasDriver()));

        fillTableView();

        truckTableView.setOnMouseClicked(e -> {
            if(truckTableView.getSelectionModel().getSelectedItem() != null) {
                newTruck = truckTableView.getSelectionModel().getSelectedItem();
                newTruckBrandLabel.setText(newTruck.getBrand());
                newTruckModelLabel.setText(newTruck.getModel());
                newTruckPlateNumberLabel.setText(newTruck.getPlateNumber());
                newTruckMileageLabel.setText(String.valueOf(newTruck.getMileage()));
            }
        });
    }

    private void fillTableView(){
        try{
            truckList =
                    databaseConnector.getEntityManager().createQuery("from Truck", Truck.class).getResultList();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        if(!truckList.isEmpty()){
            for(Truck truck : truckList){
                if(truck != currentTruck){
                    truckObservableList.add(truck);
                }
            }
            truckTableView.getItems().addAll(truckObservableList);
        }
    }
}

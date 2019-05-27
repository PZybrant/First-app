package JFXControllers;

import SQL.Cargo;
import SQL.DatabaseConnector;
import SQL.Driver;
import SQL.Forwarder;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CargoController {

    private DatabaseConnector databaseConnector;
    private Forwarder currentForwarder;
    private Driver currentDriver;
    private Cargo currentCargo;
    private Stage stage;
    private Scene scene;
    private Parent previousRoot;
    private List<Driver> driverList;

    private int currentDriverIndex, nextIndex, previousIndex;

    @FXML
    private Button infoButton, addCargoButton, updateDateButton, removeCargoButton, backButton,
    previousDriverButton, nextDriverButton, settingButton, logOutButton;

    @FXML
    private TableView<Cargo> cargoesTableView;
    private ObservableList<Cargo> cargoObservableList;

    @FXML
    private TableColumn<Cargo, String> typeColumn, senderColumn, customerColumn, dateOfOrderColumn,
    deliveryDateColumn, descriptionColumn;

    @FXML
    private Label driverLabel, forwarderLabel;


    public CargoController(Forwarder currentForwwader, Driver currentDriver,
                           List<Driver> driverList, Scene scene, Stage stage) {
        this.currentForwarder = currentForwwader;
        this.currentDriver = currentDriver;
        this.driverList = driverList;
        previousRoot = scene.getRoot();
        this.scene = scene;
        this.stage = stage;
        databaseConnector = new DatabaseConnector();

        load();
    }

    public void load(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CargoWindow.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            scene.setRoot(root);

            stage.setTitle("CARGOES");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        forwarderLabel.setText(currentForwarder.getFirstName() + " " + currentForwarder.getLastName());
    }

    @FXML
    private void initialize(){
        initializeCargoTableView();
        initializeInfoBtn();
        initializeAddBtn();
        initializeUpdateDeliveryDateBtn();
        initializeRemoveBtn();
        initializeBackButton();
        initializePreviousBtn();
        initializeNextBtn();
        initializeLogOutButton();

        driverLabel.setText(currentDriver.getFirstName() + " " + currentDriver.getLastName());
    }

    private void initializeInfoBtn() {
        infoButton.setOnAction(e -> {
            if(currentCargo != null){
                new CargoInfoController(currentCargo);
            }
        });
    }

    private void initializeAddBtn() {
        addCargoButton.setOnAction(e -> {
            if(!currentDriver.isOnRoad()) {
                AddCargoController addCargoController = new AddCargoController(currentDriver);

                // Code below is executed after closing AddCargoWindow
                if (addCargoController.getNewCargo() != null) {
                    Cargo newCargo = addCargoController.getNewCargo();
                    databaseConnector.getEntityManager().refresh(newCargo);
                    cargoObservableList.add(newCargo);
                    currentCargo = newCargo;
                    cargoesTableView.getItems().removeAll();
                    cargoesTableView.getItems().clear();
                    cargoesTableView.getItems().addAll(cargoObservableList);
                }
            }
        });
    }

    private void initializeUpdateDeliveryDateBtn() {
        updateDateButton.setOnAction(e -> {
            if(currentCargo != null && currentCargo.getDeliveryDate() == null){
                new UpdateCargoDeliveryDateController(currentCargo);
                cargoesTableView.refresh();
            }
        });
    }

    private void initializeRemoveBtn() {
        removeCargoButton.setOnAction(e -> {
            if(currentCargo != null){
                if(currentCargo.getDeliveryDate() != null){
                    boolean status = new ConfirmController().display("Remove?", "Are you sure you want to" +
                            " remove" +
                            " this cargo?");
                    if(status){
                        try{
                            databaseConnector.getEntityManager().getTransaction().begin();
                            currentDriver.removeCargo(currentCargo);
                            databaseConnector.getEntityManager().merge(currentDriver);
                            databaseConnector.getEntityManager().refresh(currentDriver);
                            databaseConnector.getEntityManager().remove(currentCargo);
                            databaseConnector.getEntityManager().getTransaction().commit();
                            cargoObservableList.remove(currentCargo);
                            currentCargo = null;
                            cargoesTableView.getItems().removeAll();
                            cargoesTableView.getItems().clear();
                            cargoesTableView.getItems().addAll(cargoObservableList);
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                } else {
                    new AlertController("Alert", "Can't delete a cargo that has not been " +
                            "delivered");
                }
            } else{
                new AlertController("Alert", "Select cargo first!");
            }
        });
    }

    private void initializeBackButton() {

        backButton.setOnAction(e -> {
            stage.setTitle("DRIVERS");
            scene.setRoot(previousRoot);
        });
    }

    private void initializePreviousBtn() {
        previousDriverButton.setOnAction(e -> {
            currentDriverIndex = driverList.indexOf(currentDriver);
            previousIndex = currentDriverIndex - 1;
            int lastListIndex = driverList.size() - 1;

            if(driverList.size() > 1){
                if(previousIndex < 0){
                    previousIndex = lastListIndex;
                    currentDriver = driverList.get(previousIndex);
                    currentCargo = null;
                    fillTable(currentDriver);
                    driverLabel.setText(currentDriver.getFirstName()+ " " + currentDriver.getLastName());
                } else {
                    currentDriver = driverList.get(previousIndex);
                    currentCargo = null;
                    fillTable(currentDriver);
                    driverLabel.setText(currentDriver.getFirstName()+ " " + currentDriver.getLastName());
                }
            }
        });
    }

    private void initializeNextBtn() {
        nextDriverButton.setOnAction(e -> {
            currentDriverIndex = driverList.indexOf(currentDriver);
            nextIndex = currentDriverIndex + 1;
            int lastListIndex = driverList.size() -1;

            if(driverList.size() > 1){
                if(nextIndex > lastListIndex){
                    nextIndex = 0;
                    currentDriver = driverList.get(nextIndex);
                    currentCargo = null;
                    fillTable(currentDriver);
                    driverLabel.setText(currentDriver.getFirstName()+ " " + currentDriver.getLastName());
                } else {
                    currentDriver = driverList.get(nextIndex);
                    currentCargo = null;
                    fillTable(currentDriver);
                    driverLabel.setText(currentDriver.getFirstName()+ " " + currentDriver.getLastName());
                }
            }
        });
    }

    private void initializeLogOutButton() {
        logOutButton.setOnAction(e -> {
            LoginController lw = new LoginController();

            boolean closeStatus = new ConfirmController().display("Confirm logout", "Are you " +
                    "sure you want logout?");

            if(closeStatus){
                try {
                    currentForwarder = null;
                    databaseConnector.getEntityManager().clear();
                    stage.close();
                    lw.start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initializeCargoTableView() {
        cargoObservableList = FXCollections.observableArrayList();

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        senderColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getSender().getCompanyName()));
        customerColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getCustomer().getCompanyName()));
        dateOfOrderColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper(p.getValue().getDateOfOrder().toString().substring(0, 19)));
        deliveryDateColumn.setCellValueFactory(p -> {
            if (p.getValue().getDeliveryDate() == null) {
                return new ReadOnlyObjectWrapper("On road");
            } else {
             return new ReadOnlyObjectWrapper(p.getValue().getDeliveryDate().toString().substring(0, 19));
            }
        });
        descriptionColumn.setCellValueFactory(p -> {
            if (!p.getValue().getDescription().isEmpty() && p.getValue().getDescription() != null){
                return new ReadOnlyObjectWrapper<>(p.getValue().getDescription());
            } else{
                return new ReadOnlyObjectWrapper<>("empty");
            }
        });

        fillTable(currentDriver);

        cargoesTableView.setOnMouseClicked(e -> {
            if(cargoesTableView.getSelectionModel().getSelectedItem() != null){
                currentCargo = cargoesTableView.getSelectionModel().getSelectedItem();
            }
            System.out.println(currentCargo);
        });

    }

    private void fillTable(Driver driver){

        cargoesTableView.getItems().clear();
        cargoObservableList.clear();

        try {
            List<Cargo> resultList =
                    databaseConnector.getEntityManager().createQuery("FROM Cargo WHERE driver = " + driver.getRowId(),
                            Cargo.class).getResultList();
            cargoObservableList.addAll(resultList);
            cargoesTableView.getItems().addAll(cargoObservableList);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}

































































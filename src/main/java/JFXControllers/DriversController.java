package JFXControllers;

import SQL.DatabaseConnector;
import SQL.Driver;
import SQL.Forwarder;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriversController {

    private DatabaseConnector databaseConnector;
    private Driver currentDriver;
    private Forwarder currentForwarder;
    private List<Driver> drivers;

    private String sortValue = "default";
    private String searchValue = "";

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button switchTableButton, infoButton, changeTruckButton, removeTruckButton,
            changeForwarderButton, logOutButton;

    @FXML
    private ComboBox<String> sortingBox;

    @FXML
    private  MenuButton sortButton;

    @FXML
    private MenuItem menuItemDefault, menuItemIdAsc, menuItemIdDesc, menuItemFNameAsc,
            menuItemFNameDesc, menuItemLNameAsc, menuItemLNameDesc;

    @FXML
    private TableView<Driver> driversTableView;
    private ObservableList<Driver> driverObservableList, currentForwarderDriversObservableList;
    // currentForwarderDriversObservableList = 0, driverObservableList = 1
    private int listNumber;

    @FXML
    private TableColumn<Driver, Integer> rowIdColumn , driverIdNumberColumn;

    @FXML
    private TableColumn<Driver, String> firstNameColumn, lastNameColumn, onRoadColumn;

    @FXML
    private Label totalCargoesLabel, forwarderLabel;

    public DriversController(Forwarder forwarder){
        this.currentForwarder = forwarder;
        databaseConnector = new DatabaseConnector();

        listNumber = 0;
        stage = new Stage();
        load();
    }

    public void load(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DriversWindow.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("DRIVERS");
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        forwarderLabel.setText(currentForwarder.getFirstName() + " " + currentForwarder.getLastName());
    }


    @FXML
    public void initialize() {
        initializeTableView();
        initializeSearchTextField();
        initializeSortButton();
        initializeSwitchTableButton();
        initializeInfoButton();
        initializeChangeTruckButton();
        initializeRemoveTruckButton();
        initializeChangeForwarderButton();
        initializeLogOutButton();
        initializeOnStageCloseRequest();
    }

    private void initializeOnStageCloseRequest() {
        stage.setOnCloseRequest(e -> {
            e.consume();
            boolean closeStatus = new ConfirmController().display("Closing", "Are you sure you " +
                    "want to close the application?");

            if(closeStatus){
                databaseConnector.getEntityManager().close();
                stage.close();
            }
        });
    }

    private void initializeTableView() {
        driverObservableList = FXCollections.observableArrayList();
        currentForwarderDriversObservableList = FXCollections.observableArrayList();
        rowIdColumn.setCellValueFactory(new PropertyValueFactory<>("rowId"));
        driverIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        onRoadColumn.setCellValueFactory(p -> {
            if(p.getValue().isOnRoad()){
                return new ReadOnlyObjectWrapper<>("Yes");
            } else {
                return new ReadOnlyObjectWrapper<>("No");
            }
        });

        fillTable();

        driversTableView.setOnMouseClicked(this::onTableClick);

    }

    private void initializeSearchTextField() {
        searchTextField.setOnKeyReleased(e -> {

            if(e.getCode().isDigitKey()){
                if(sortValue.equals("default") || sortValue.equals("id")){
                    searchValue = "id";
                    searchBy(searchValue);
                }
            } else if(e.getCode().isLetterKey()) {
                if(sortValue.equals("default")  || sortValue.equals("lastName")){
                    searchValue = "lastName";
                    searchBy(searchValue);
                } else {
                    searchValue = "firstName";
                    searchBy(searchValue);
                }
            } else if(e.getCode().getCode() == 8){
                if(searchValue.length() > 0){
                    searchBy(searchValue);
                }
            } else {
                searchTextField.deletePreviousChar();
            }
        });
    }

    private void initializeSortButton() {
        menuItemDefault.setOnAction(e -> {
            searchTextField.clear();
            searchTextField.setPromptText("Type last name/id number");
            sort(rowIdColumn, true);
            sortValue = "default";
        });

        menuItemIdAsc.setOnAction(e -> {
            searchTextField.clear();
            searchTextField.setPromptText("Type id number");
            sort(driverIdNumberColumn, true);
            sortValue = "id";
        });

        menuItemIdDesc.setOnAction(e -> {
            searchTextField.clear();
            searchTextField.setPromptText("Type id number");
            sort(driverIdNumberColumn, false);
            sortValue = "id";
        });

        menuItemFNameAsc.setOnAction(e -> {
            searchTextField.clear();
            searchTextField.setPromptText("Type first name");
            sort(firstNameColumn, true);
            sortValue = "first name";
        });

        menuItemFNameDesc.setOnAction(e -> {
            searchTextField.clear();
            searchTextField.setPromptText("Type first name");
            sort(firstNameColumn, false);
            sortValue = "first name";
        });

        menuItemLNameAsc.setOnAction(e -> {
            searchTextField.clear();
            searchTextField.setPromptText("Type last name");
            sort(lastNameColumn, true);
            sortValue = "last name";
        });

        menuItemLNameDesc.setOnAction(e -> {
            searchTextField.clear();
            searchTextField.setPromptText("Type last name");
            sort(lastNameColumn, false);
            sortValue = "last name";
        });
    }

    private void initializeSwitchTableButton() {
        switchTableButton.setOnAction(e -> {
            driversTableView.getItems().clear();
            driversTableView.getItems().removeAll();

            if(listNumber == 0){
                listNumber = 1;
                driversTableView.getItems().addAll(driverObservableList);
            }else if(listNumber == 1){
                listNumber = 0;
                driversTableView.getItems().addAll(currentForwarderDriversObservableList);
            }
            currentDriver = null;
            totalCargoesLabel.setText("0");
        });
    }

    private void initializeInfoButton() {
        infoButton.setOnAction(e -> {
            if (currentDriver != null){
                new DriverInfoController(currentDriver);
            } else {
                new AlertController("ERROR", "CHOOSE A DRIVER");
            }
        });
    }

    private void initializeChangeTruckButton() {
        changeTruckButton.setOnAction(e -> {
            if(currentDriver != null){
               new ChangeTruckController(currentDriver);
            } else {
                new AlertController("ERROR", "CHOOSE A DRIVER");
            }
        });
    }

    private void initializeRemoveTruckButton() {
        removeTruckButton.setOnAction(e -> {
            if(currentDriver != null){
                if (currentDriver.getTruck() != null && !currentDriver.isOnRoad()){
                    databaseConnector.getEntityManager().getTransaction().begin();
                    Truck currentTruck = currentDriver.getTruck();
                    currentDriver.setTruck(null);
                    currentTruck.setCurrentDriver(null);
                    databaseConnector.getEntityManager().merge(currentDriver);
                    databaseConnector.getEntityManager().merge(currentTruck);
                    databaseConnector.getEntityManager().getTransaction().commit();
                    new AlertController("SUCCESS", "Truck successfully removed");
                } else {
                    new AlertController("ERROR", "Truck can't be removed");
                }
            } else {
                new AlertController("ERROR", "CHOOSE A DRIVER");
            }
        });
    }

    private void initializeChangeForwarderButton(){
            changeForwarderButton.setOnAction(e -> {
                if(currentDriver != null) {
                    new ChangeForwarderController(currentDriver);

                    if(currentDriver.getForwarder() == currentForwarder){
                        if(!currentForwarderDriversObservableList.contains(currentDriver)){
                            currentForwarderDriversObservableList.add(currentDriver);
                        }
                    } else if(currentDriver.getForwarder() != currentForwarder){
                        if(currentForwarderDriversObservableList.contains(currentDriver)){
                            currentForwarderDriversObservableList.remove(currentDriver);
                        }
                    }
                   if(listNumber == 0){
                       fillTable();
                   }
                }
            });
    }

    private void initializeLogOutButton(){
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

    private void fillTable(){
        driverObservableList.clear();
        currentForwarderDriversObservableList.clear();
        driversTableView.getItems().removeAll();
        driversTableView.getItems().clear();
        try {
            drivers = databaseConnector.getEntityManager().createQuery("FROM Driver",
                    Driver.class).getResultList();

            drivers.forEach(System.out::println);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        for(Driver driver : drivers){
            if(driver.getForwarder() == currentForwarder){
                currentForwarderDriversObservableList.add(driver);
            }
        }
        driverObservableList.addAll(drivers);

        if(listNumber == 0){
            driversTableView.getItems().addAll(currentForwarderDriversObservableList);
        } else if(listNumber == 1){
            driversTableView.getItems().addAll(driverObservableList);
        }
    }

    private void onTableClick(MouseEvent e) {

        if (driversTableView.getSelectionModel().getSelectedItem() != null) {
            if (e.getClickCount() == 1) {
                currentDriver = driversTableView.getSelectionModel().getSelectedItem();
                String totalCargoes = String.valueOf(currentDriver.getCargoes().size());
                totalCargoesLabel.setText(totalCargoes);
            } else if (e.getClickCount() > 1) {
                List<Driver> driverList = driversTableView.getItems();

                new CargoController(currentForwarder, currentDriver, driverList, scene, stage);
            }
        }
    }

    private void searchBy(String value){
        ObservableList<Driver> tempObservableList = FXCollections.observableArrayList();
        List<Driver> tempList = new ArrayList<>();
        if(listNumber == 0){
            tempList = currentForwarderDriversObservableList;
        } else if(listNumber == 1){
            tempList = driverObservableList;
        }
        driversTableView.getItems().removeAll();
        driversTableView.getItems().clear();

        String str1;
        String str2 = searchTextField.getText();

        if(value.equals("id")){
            for(Driver driver : tempList) {
                long idNumber = driver.getIdNumber();
                str1 = String.valueOf(idNumber);
                if (compare(str1, str2)) {
                    tempObservableList.add(driver);
                }
            }
        } else if(value.equals("firstName")){
            for(Driver driver : tempList){
                str1 = driver.getFirstName();
                if(compare(str1, str2)){
                    tempObservableList.add(driver);
                }
            }
        } else if(value.equals("lastName")) {
            for (Driver driver : tempList) {
                str1 = driver.getLastName();
                if (compare(str1, str2)) {
                    tempObservableList.add(driver);
                }
            }
        }
        driversTableView.getItems().addAll(tempObservableList);
    }

    private void sort(TableColumn column, boolean ascendingSortType){
        if(ascendingSortType){
            column.setSortType(TableColumn.SortType.ASCENDING);
        } else {
            column.setSortType(TableColumn.SortType.DESCENDING);
        }
        column.setSortable(true);
        driversTableView.getSortOrder().add(column);
        driversTableView.sort();
        column.setSortable(false);
    }


    private boolean compare(String str1, String str2) {
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

        for(int i = 0; i < str2.length(); i++){
            char a = str1.charAt(i);
            char b = str2.charAt(i);
            if(a != b){
                return false;
            }
        }
        return true;
    }
}

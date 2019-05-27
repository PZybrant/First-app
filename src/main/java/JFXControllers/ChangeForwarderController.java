package JFXControllers;

import SQL.DatabaseConnector;
import SQL.Driver;
import SQL.Forwarder;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ChangeForwarderController {

    private DatabaseConnector databaseConnector;
    private Driver currentDriver;
    private Forwarder currentForwarder, newForwarder;

    private List<Forwarder> forwarderList;
    private Stage stage;

    @FXML
    private Label currentForwarderIdLabel, currentForwarderFirstNameLabel,
            currentForwarderLastNameLabel, newForwarderIdLabel, newForwarderFirstNameLabel,
            newForwarderLastNameLabel;

    @FXML
    private TableView<Forwarder> forwarderTableView;
    private ObservableList<Forwarder> forwarderObservableList;

    @FXML
    private TableColumn<Forwarder, Long> idColumn;

    @FXML
    private TableColumn<Forwarder, String> firstNameColumn, lastNameColumn;

    @FXML
    private Button cancelButton, applyButton;

    public ChangeForwarderController(Driver currentDriver){
        stage = new Stage();
        databaseConnector = new DatabaseConnector();
        this.currentDriver = currentDriver;
        this.currentForwarder = currentDriver.getForwarder();
        forwarderObservableList = FXCollections.observableArrayList();

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml" +
                    "/ChangeForwarderWindow" +
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
            if(newForwarder != null){
                boolean status = new ConfirmController().display("Changing forwarder", "Are you " +
                        "sure you want to change forwarder?");
                if(status){
                    try{
                        databaseConnector.getEntityManager().getTransaction().begin();
                        currentForwarder.removeDriver(currentDriver);
                        newForwarder.addDriver(currentDriver);
                        currentDriver.setForwarder(newForwarder);
                        databaseConnector.getEntityManager().merge(currentForwarder);
                        databaseConnector.getEntityManager().merge(newForwarder);
                        databaseConnector.getEntityManager().merge(currentDriver);
                        databaseConnector.getEntityManager().getTransaction().commit();
                        new AlertController("SUCCESS", "Forwarder changed successfully");
                        stage.close();
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            } else {
                new AlertController("ERROR", "Choose forwarder first!");
            }
        });
    }

    private void initializeLabels() {
        currentForwarderIdLabel.setText(String.valueOf(currentForwarder.getIdNumber()));
        currentForwarderFirstNameLabel.setText(currentForwarder.getFirstName());
        currentForwarderLastNameLabel.setText(currentForwarder.getLastName());

    }

    private void initializeTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        fillTableView();

        forwarderTableView.setOnMouseClicked(e -> {
            if(forwarderTableView.getSelectionModel().getSelectedItem() != null) {
                newForwarder = forwarderTableView.getSelectionModel().getSelectedItem();
                newForwarderIdLabel.setText(String.valueOf(newForwarder.getIdNumber()));
                newForwarderFirstNameLabel.setText(newForwarder.getFirstName());
                newForwarderLastNameLabel.setText(newForwarder.getLastName());
            }
        });
    }

    private void fillTableView(){
        try{
            forwarderList =
                    databaseConnector.getEntityManager().createQuery("from Forwarder",
                            Forwarder.class).getResultList();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        if(!forwarderList.isEmpty()){
            for(Forwarder forwarder : forwarderList){
                if(forwarder != currentForwarder){
                    forwarderObservableList.add(forwarder);
                }
            }
            forwarderTableView.getItems().addAll(forwarderObservableList);
        }
    }
}

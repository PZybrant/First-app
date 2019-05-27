package JFXControllers;

import SQL.Cargo;
import SQL.DatabaseConnector;
import SQL.Driver;
import SQL.Truck;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Patryk on 2019-03-31
 */
public class UpdateCargoDeliveryDateController {

    private DatabaseConnector databaseConnector;
    private Cargo currentCargo;
    private Driver driver;
    private Truck truck;
    private int actualMileage, newMileage;
    private int year, month, day, hour, minute;
    private Stage stage;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> hoursComboBox, minutesComboBox;

    @FXML
    private Button cancelButton, applyButton;

    @FXML
    private Label warningLabel;

    public UpdateCargoDeliveryDateController(Cargo currentCargo){
        stage = new Stage();
        this.currentCargo = currentCargo;
        databaseConnector = new DatabaseConnector();
        driver = currentCargo.getDriver();
        truck = driver.getTruck();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml" +
                    "/UpdateCargoDeliveryDateWindow" +
                    ".fxml"));
            loader.setController(this);
            Parent root = loader.load();

            Scene scene = new Scene(root);
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
        initializeHoursComboBox();
        initializeMinutesComboBox();
        initializeCancelButton();
        initializeApplyButton();
        initializeOnStageCloseRequest();
        datePicker.setOnAction(a -> warningLabel.setText(""));
    }

    private void initializeOnStageCloseRequest() {
        stage.setOnCloseRequest(e -> {
            e.consume();
            boolean closeStatus = new ConfirmController().display("Closing", "Are you sure you?");

            if(closeStatus){
                stage.close();
            }
        });
    }

    private void initializeHoursComboBox() {
        String str;
        for(int i = 0; i < 24; i++){
            StringBuilder sb = new StringBuilder();
            if(i <= 9){
                sb.append("0" + i);
                str = sb.toString();
                hoursComboBox.getItems().add(str);
            } else {
                str = String.valueOf(i);
                hoursComboBox.getItems().add(str);
            }
        }
        hoursComboBox.setValue("00");
        hoursComboBox.setOnAction(e -> {
            warningLabel.setText("");
            hour = Integer.parseInt(hoursComboBox.getValue());
        });
    }

    private void initializeMinutesComboBox() {
        String str;
        for(int i = 0; i < 60; i++){
            StringBuilder sb = new StringBuilder();
            if(i <= 9){
                sb.append("0" + i);
                str = sb.toString();
                minutesComboBox.getItems().add(str);
            } else {
                str = String.valueOf(i);
                minutesComboBox.getItems().add(str);
            }
        }
        minutesComboBox.setValue("00");
        minutesComboBox.setOnAction(e -> {
            warningLabel.setText("");
            minute = Integer.parseInt(minutesComboBox.getValue());
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

            actualMileage = truck.getMileage();
            newMileage = actualMileage + currentCargo.getDistance();
            Timestamp timestamp = null;

            if(datePicker.getValue() != null) {
                year = datePicker.getValue().getYear();
                month = datePicker.getValue().getMonth().getValue() -1;
                day = datePicker.getValue().getDayOfMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute);
                timestamp = new Timestamp(calendar.getTimeInMillis());
            }


            if(timestamp != null) {
                if (timestamp.after(currentCargo.getDateOfOrder())) {
                    try {
                        databaseConnector.getEntityManager().getTransaction().begin();
                        currentCargo.setDeliveryDate(timestamp);
                        truck.setMileage(newMileage);
                        driver.setOnRoad(false);
                        databaseConnector.getEntityManager().merge(currentCargo);
                        databaseConnector.getEntityManager().merge(driver);
                        databaseConnector.getEntityManager().merge(truck);
                        databaseConnector.getEntityManager().getTransaction().commit();
                        stage.close();
                    } catch (Exception ex) {
                        System.out.println(ex.toString());
                    }
                } else {
                    warningLabel.setText("Delivery date must be after order date!");
                }
            } else {
                warningLabel.setText("Select date first!");
            }


        });
    }

}

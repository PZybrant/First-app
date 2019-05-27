package JFXControllers;

import SQL.Company;
import SQL.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddNewCompanyController {

    private DatabaseConnector databaseConnector;
    private SelectCompanyController selectCompanyController;
    private Company newCompany;
    private String companyName, companyCounty, companyCity, companyStreet, companyZipcode;

    private Parent previousRoot, currentRoot;
    private Scene scene;

    @FXML
    private TextField nameTextField, countryTextField, cityTextField, streetTextField,
            zipCodeTextField;

    @FXML
    private Label warningLabel;

    @FXML
    private Button cancelButton, applyButton;

    public AddNewCompanyController(Scene scene, SelectCompanyController selectCompanyController){
        databaseConnector = new DatabaseConnector();
        previousRoot = scene.getRoot();
        this.scene = scene;
        this.selectCompanyController = selectCompanyController;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddNewCompanyWindow" +
                    ".fxml"));
            loader.setController(this);
            currentRoot = loader.load();
            scene.setRoot(currentRoot);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        initializeTextFields();
        initializeCancelButton();
        initializeApplyButton();
    }

    private void initializeTextFields() {
        nameTextField.setOnKeyReleased(e -> {
            companyName = nameTextField.getText();
        });

        countryTextField.setOnKeyReleased(e -> {
            companyCounty = countryTextField.getText();
            if(!isLetterOnly(companyCounty)){
                warningLabel.setText("Only letters allowed for country.");
            }
        });

        cityTextField.setOnKeyReleased(e -> {
            companyCity = cityTextField.getText();
            if(!isLetterOnly(companyCity)){
                warningLabel.setText("Only letters allowed for city.");
            }
        });

        streetTextField.setOnKeyReleased(e -> {
            companyStreet = streetTextField.getText();
        });

        zipCodeTextField.setOnKeyReleased(e -> {
            int max = 5;
            int size = zipCodeTextField.getText().length();

            if(e.getCode().isDigitKey()){
                if (size > max) {
                    zipCodeTextField.deletePreviousChar();
                }
            } else if(e.getCode().getCode() != 8) {
                zipCodeTextField.deletePreviousChar();
            }
        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(e -> {
            boolean closeStatus = new ConfirmController().display("Closing", "Are you sure you?");
            if(closeStatus){
                scene.setRoot(previousRoot);
            }
        });
    }

    private void initializeApplyButton() {
        applyButton.setOnAction(e -> {

            companyName = nameTextField.getText().toUpperCase();
            companyCounty = countryTextField.getText().toUpperCase();
            companyCity = cityTextField.getText().toUpperCase();
            companyStreet = streetTextField.getText().toUpperCase();
            companyZipcode = zipCodeTextField.getText().toUpperCase();

            if(isValidate()) {
                try {
                    databaseConnector.getEntityManager().getTransaction().begin();
                    newCompany = new Company(companyName, companyCounty, companyCity, companyStreet,
                            companyZipcode);
                    databaseConnector.getEntityManager().persist(newCompany);
                    databaseConnector.getEntityManager().getTransaction().commit();
                    selectCompanyController.addNewCompanyIntoTable(newCompany);
                    scene.setRoot(previousRoot);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else{
                warningLabel.setText("There are some missing fields");
            }
        });
    }

    private boolean isValidate() {
        return companyName != null && !companyName.isEmpty() && companyCounty != null && !companyCounty.isEmpty()
                && companyCity != null && !companyCity.isEmpty() && companyStreet != null && !companyStreet.isEmpty()
                && companyZipcode != null && companyZipcode.length() == 5;
    }

    private boolean isLetterOnly(String str){
        char[] chars = str.toCharArray();
        for(char c : chars){
            if(!Character.isLetter(c)){
                return false;
            }
        }
        return true;
    }

    public Company getNewCompany() {
        return newCompany;
    }
}

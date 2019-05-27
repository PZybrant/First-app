package JFXControllers;

import SQL.Company;
import SQL.DatabaseConnector;
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

public class SelectCompanyController {

    private DatabaseConnector databaseConnector;
    private Company selectedCompany;
    private AddCargoController addCargoController;

    private List<Company> companies;
    private Parent previousRoot, currentRoot;
    private String who;
    private Scene scene;
    private Stage stage;


    @FXML
    private Label selectedCompanyLabel;

    @FXML
    private Button addNewCompanyButton, cancelButton, applyButton;

    @FXML
    private TableView<Company> companyTableView;
    private ObservableList<Company> companyObservableList;

    @FXML
    private TableColumn<Company, String> nameColumn, countryColumn, cityColumn, streetColumn,
            zipCodeColumn;

    public SelectCompanyController(Scene scene, AddCargoController addCargoController, String who){
        databaseConnector = new DatabaseConnector();
        companyObservableList = FXCollections.observableArrayList();
        previousRoot = scene.getRoot();
        this.scene = scene;
        this.addCargoController = addCargoController;
        this.who = who;

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectCompanyWindow" +
                    ".fxml"));
            loader.setController(this);
            currentRoot = loader.load();

            scene.setRoot(currentRoot);
        }catch (IOException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        initializeAddNewCompanyButton();
        initializeCancelButton();
        initializeApplyButton();
        initializeTableView();
    }

    private void initializeAddNewCompanyButton() {
        addNewCompanyButton.setOnAction(e ->{
            AddNewCompanyController newCompanyController = new AddNewCompanyController(scene, this);
        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(e -> scene.setRoot(previousRoot));
    }

    private void initializeApplyButton() {
        applyButton.setOnAction(e -> {
            if(selectedCompany != null){
                switch (who){
                    case ("sender"):
                        addCargoController.setSender(selectedCompany);
                        break;
                    case("customer"):
                        addCargoController.setCustomer(selectedCompany);
                        break;
                }
                scene.setRoot(previousRoot);
            }
        });
    }

    private void initializeTableView() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));

        fillTableView();

        companyTableView.setOnMouseClicked(e -> {
            if(companyTableView.getSelectionModel().getSelectedItem() != null){
                selectedCompany = companyTableView.getSelectionModel().getSelectedItem();
                selectedCompanyLabel.setText(selectedCompany.getCompanyName());
            }
        });
    }

    private void fillTableView() {
        companyObservableList.clear();
        companyTableView.getItems().clear();
        companyTableView.getItems().removeAll();

        try{
            companies = databaseConnector.getEntityManager().createQuery("from Company", Company.class).getResultList();
        } catch (NullPointerException ex){
            System.out.println(ex);
        } catch (Exception ex){
            System.out.println(ex);
        }

        if(companies != null){
            companyObservableList.addAll(companies);
        }
        companyTableView.getItems().addAll(companyObservableList);
    }

    public void addNewCompanyIntoTable(Company company){
        Company newCompany = company;
        databaseConnector.getEntityManager().refresh(newCompany);
        companyObservableList.add(newCompany);
        companyTableView.getItems().removeAll();
        companyTableView.getItems().clear();
        companyTableView.getItems().addAll(companyObservableList);
    }
}

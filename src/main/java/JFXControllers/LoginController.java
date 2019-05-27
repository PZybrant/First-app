package JFXControllers;

import SQL.DatabaseConnector;
import SQL.Forwarder;
import SQL.LoginData;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class LoginController extends Application {

    private DatabaseConnector databaseConnector;
    private Forwarder currentForwarder;
    private List<Forwarder> forwarders;
    private Button userButton;
    private Rectangle2D visualBounds;
    private Scene scene;
    private Stage stage;

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox usersVBox;

    @FXML
    private VBox imageVBox;

    @FXML
    private Label warningLabel, userLabel;

    @FXML
    private Button loginButton;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        databaseConnector = new DatabaseConnector();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        visualBounds = Screen.getPrimary().getVisualBounds();

        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void initialize(){
        initializeUserButtons();
        initializeLoginButton();
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

    private void initializeUserButtons() {
        try{
            forwarders = databaseConnector.getEntityManager().createQuery("FROM Forwarder", Forwarder.class).getResultList();
        } catch (Exception ex){
            ex.toString();
        }

        for(Forwarder f : forwarders){
            userButton = new Button();
            userButton.setText(f.getFirstName() + " " + f.getLastName());
            userButton.getStyleClass().add("user-button");
            ImageView userAvatar = new ImageView();
            userAvatar.setFitHeight(40);
            userAvatar.setFitWidth(40);
            try{
                Image image = new Image("/images/baseline_account_circle_white_48dp.png");
                userAvatar.setImage(image);
            } catch (NullPointerException ex){
                ex.printStackTrace();
            } catch (IllegalArgumentException ex){
                ex.printStackTrace();
            } catch (Exception ex){
                ex.printStackTrace();
            }
            userButton.setGraphic(userAvatar);
            userButton.setOnAction(e -> onUserButtonClicked(e));
            usersVBox.getChildren().add(userButton);
        }
    }

    private void onUserButtonClicked(ActionEvent e) {
        loginTextField.setText("");
        passwordField.setText("");
        for(int i = 0; i < usersVBox.getChildren().size(); i++){
                Object obj = usersVBox.getChildren().get(i);
            if(obj == e.getSource()){
                currentForwarder = forwarders.get(i);
                userLabel.setText(currentForwarder.getFirstName() + " " + currentForwarder.getLastName());
            }
        }
        imageVBox.setVisible(false);
        loginButton.setVisible(true);
    }


    private void initializeLoginButton() {
        loginButton.setVisible(false);
        
        loginButton.setOnAction(e -> {
            LoginData loginData = currentForwarder.getLoginData();

            String login = loginTextField.getText();
            String password = passwordField.getText();

            if (!login.isEmpty() && !password.isEmpty()) {
                if (loginData.getLogin().equals(login) && loginData.getPassword().equals(password)) {
                    currentForwarder = loginData.getUser();
                    stage.close();
                    new DriversController(currentForwarder);

                } else {
                    warningLabel.setText("Invalid login or password");
                    loginTextField.setText("");
                    passwordField.setText("");
                }

            } else {
                warningLabel.setText("Missing fields");
            }
        });
    }

    public void begin(String[] strings){
        launch(strings);
    }


}

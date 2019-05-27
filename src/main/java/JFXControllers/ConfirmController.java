package JFXControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.internal.CoreLogging;

import java.io.IOException;

public class ConfirmController {

    private String message;
    private Stage stage;
    private static boolean status;

    @FXML
    private Label label;

    @FXML
    private Button cancelButton, acceptButton;

    public boolean display(String title, String message){
        this.message = message;
        stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConfirmWindow.fxml"));
        loader.setController(this);

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();

        return status;
    }

    @FXML
    private void initialize(){
        initializeLabel();
        initializeCancelButton();
        initializeAcceptButton();
    }

    private void initializeLabel() {
        label.setText(message);
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(e -> {
            status = false;
            stage.close();
        });

    }

    private void initializeAcceptButton() {
        acceptButton.setOnAction(e -> {
            status = true;
            stage.close();
        });

    }
}

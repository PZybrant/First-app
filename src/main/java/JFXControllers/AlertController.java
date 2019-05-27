package JFXControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertController {

    private String text;
    private Stage stage;

    @FXML
    private Label label;
    @FXML
    private Button acceptButton;

    public AlertController(String title, String text){
        this.text = text;
        stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AlertWindow.fxml"));
        loader.setController(this);
        try{
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException ioe){
            ioe.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        stage.setResizable(false);
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void initialize(){
        initializeLabel();
        initializeAcceptButton();
    }

    private void initializeLabel() {
        label.setText(text);
    }

    private void initializeAcceptButton() {
        acceptButton.setOnAction(e -> stage.close());
    }

    //----------------------------------------------------------------------------------------------------------------------
    public static void close(Stage stage){
        stage.close();
    }
//----------------------------------------------------------------------------------------------------------------------
}

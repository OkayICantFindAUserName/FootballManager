package Client.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class TextController {

    @FXML
    private TextField NachrichtTXT;

    @FXML
    public JFXButton closeButton;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public static StringProperty friendnameproperty = new SimpleStringProperty();
    public static String friend;

    @FXML
    void sendMessage(ActionEvent event) {
        if(!NachrichtTXT.getText().isEmpty()){
            String alle = NachrichtTXT.getText();
            if(alle.length()>250){
                alert.setTitle("Nachricht");
                alert.setContentText("Nachricht konnte nicht versendet werden, da sie mehr als 250 Zeichen beinhaltet!");
                alert.show();
            }else {
                ClientConnection.sendMessages(friend, alle);
                alert.setTitle("Nachricht");
                alert.setContentText("Nachricht wurde gesendet");
                alert.show();
                closeButtonAction();
                System.out.println(alle.length());
            }
        }
        System.out.println("keine Nachricht");
    }
    private void closeButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    }

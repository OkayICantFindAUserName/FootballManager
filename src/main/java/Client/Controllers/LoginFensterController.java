package Client.Controllers;


import Client.Main;
import Client.ShakeIt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginFensterController implements Initializable {

    @FXML
    private TextField nameTXT;

    @FXML
    private PasswordField passwortTXT;

    public static int ID;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    //beendet das System
    @FXML
    void exit(ActionEvent event) {
        System.exit(1);
    }
    // ermöglicht das Login
    @FXML
    void login(ActionEvent event) throws IOException {

        ShakeIt voidName = new ShakeIt(nameTXT);  //Falls keine eingaben gemacht wurden erscheint eine kleine animation
        ShakeIt voidPassword = new ShakeIt(passwortTXT);//Falls keine eingaben gemacht wurden erscheint eine kleine animation

        if(!nameTXT.getText().isEmpty() && !passwortTXT.getText().isEmpty()){
            String s = ClientConnection.checkLogin(nameTXT.getText(), passwortTXT.getText());
            if(s == "ez"){
                Main.setRoot("Hauptmenue");
            }else if(s == null){
                alert.setTitle("Registrierung");
                alert.setContentText("Accountname oder Passwort sind falsch");
                alert.show();
            }
        } else{

            voidName.playAnim();
            voidPassword.playAnim();
        }
    }
    //navigation
    @FXML
    void register(ActionEvent event) throws IOException {

        Main.setRoot("RegestrierFenster");

    }
    //navigation
    @FXML
    void serverip(ActionEvent event)  throws IOException {
    	 Main.setRoot("Serverip");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert.getDialogPane().getStylesheets().add(
                LoginFensterController.class.getClassLoader().getResource("stylesheet.css").toExternalForm());
        alert.initStyle(StageStyle.UNDECORATED);

    }


}

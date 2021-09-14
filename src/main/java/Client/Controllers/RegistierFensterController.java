package Client.Controllers;

import Client.Main;
import Client.ShakeIt;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistierFensterController  implements Initializable {
    @FXML
    private TextField emailTXT;

    @FXML
    private TextField nameTXT;

    @FXML
    private JFXPasswordField passwortTXT;

    @FXML
    private JFXPasswordField passwort2TXT;

    private Alert alert = new Alert(AlertType.INFORMATION);

    @FXML
    void register(ActionEvent event) throws IOException {

        ShakeIt voidName = new ShakeIt(nameTXT);
        ShakeIt voidPassword = new ShakeIt(passwortTXT);
        ShakeIt voidPassword2 = new ShakeIt(passwort2TXT);
        ShakeIt voidEMail = new ShakeIt(emailTXT);
        if (validateEmail()) {
            if (validatePassword()) {
            	if (passwortTXT.getText().equals(passwort2TXT.getText())) {
            		if(!nameTXT.getText().isEmpty() && !passwortTXT.getText().isEmpty() && !emailTXT.getText().isEmpty()){

            			String information = ClientConnection.postUSER(nameTXT.getText(),passwortTXT.getText(),emailTXT.getText());
            			alert.setTitle("Registrierung");
            			alert.setContentText(information);
            			alert.show();
            			if(information.contains("erfolgreich")){
						Main.setRoot("LoginFenster");}
            		} else{

            			voidName.playAnim();
            			voidPassword.playAnim();
            			voidPassword2.playAnim();
            			voidEMail.playAnim();
            		}

            	}else {
            		alert.setTitle("Registrierung");
            		alert.setContentText("Passwoerter muessen uebereinstimmen");
            		alert.show();
            		voidPassword.playAnim();
            		voidPassword2.playAnim();
            	}
            }
        }
    }
    
    private boolean validateEmail() {
    	Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._-]*@[a-zA-Z0-9._-]+([.][a-zA-Z]+)+");
    	Matcher m = p.matcher(emailTXT.getText());
    	if(m.find() && m.group().equals(emailTXT.getText())) {
    		return true;
    	}else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Ung�ltige Email-Adresse");
    		alert.setHeaderText(null);
    		alert.setContentText("Bitte geben Sie eine g�ltige Email-Adresse ein!");
    		alert.showAndWait();
    		
    		return false;
    	}
    }
    
    private boolean validatePassword() {
    	Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,15})");
    	Matcher m = p.matcher(passwortTXT.getText());
    	if(m.matches()) {
    		return true;
    	}else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Passwort ung�ltig");
    		alert.setHeaderText(null);
    		alert.setContentText("Bitte geben Sie eine g�ltiges Passwort ein!");
    		alert.showAndWait();
    		
    		return false;
    	}
    }
    
    @FXML
    void switchToLogin(ActionEvent event) throws IOException {

        Main.setRoot("LoginFenster");

    }

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		alert.getDialogPane().getStylesheets().add(
				LoginFensterController.class.getClassLoader().getResource("stylesheet.css").toExternalForm());
		alert.initStyle(StageStyle.UNDECORATED);
	}
}


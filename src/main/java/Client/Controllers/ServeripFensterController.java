package Client.Controllers;

import Client.Main;
import Client.ShakeIt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ServeripFensterController implements Initializable {
	private Alert alert = new Alert(Alert.AlertType.INFORMATION);
	@FXML
	private TextField serverTXT;
	@FXML
	private TextField portTXT;

	
    @FXML
    void serv(ActionEvent event) throws IOException {
        ShakeIt voidserver = new ShakeIt(serverTXT);
        ShakeIt voidport = new ShakeIt(portTXT);

    	if(serverTXT.getText().isEmpty() && portTXT.getText().isEmpty()) {
    		voidserver.playAnim();
    		voidport.playAnim();
    		return;
    	}
    	
    	if(serverTXT.getText().isEmpty() && !portTXT.getText().isEmpty()) {
    		voidserver.playAnim();
    		return;
    		
    	}
    	
    	if(!serverTXT.getText().isEmpty() && portTXT.getText().isEmpty()) {
    		voidport.playAnim();
    		return;
    		
    	}
    	
    	ClientConnection.setip(serverTXT.getText());
		ClientConnection.setport(Integer.parseInt(portTXT.getText()));
    	
        alert.setTitle("IP und Port geandert");
        alert.setContentText("IP: "+serverTXT.getText() + "  Port: "+portTXT.getText());
        alert.show();
		
    	System.out.println("Server ip geandert in: "+ serverTXT.getText());
   		System.out.println("Port geandert auf: "+Integer.parseInt(portTXT.getText()));
    		
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

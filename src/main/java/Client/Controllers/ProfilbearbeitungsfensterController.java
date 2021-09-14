package Client.Controllers;

import Client.Models.Assets;
import Client.Main;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfilbearbeitungsfensterController implements Initializable {

    @FXML
    private TextField nameTXT;

    @FXML
    private TextField emailTXT;

    @FXML
    private TextField passwortTXT;

    @FXML
    private TextField teamNameTXT;

    @FXML
    private TextField StadionNameTXT;

    @FXML
    private JFXButton stadionBTN;

    @FXML
    private JFXButton imbissBTN;

    @FXML
    private JFXButton parkplatzBTN;

    private Alert alert = new Alert(AlertType.INFORMATION);

    private int costImbiss;
    private int costParkplatz;
    private int costStadion;

    @FXML
    void Spielverlassen(ActionEvent event) throws IOException {

        Main.setRoot("HauptmenueFenster");
    }

    @FXML
    void speichern(ActionEvent event) {

        if(!StadionNameTXT.getText().isEmpty()){
            String s = ClientConnection.updateAssetName(StadionNameTXT.getText());
            HauptmenueController.stadionName.set(StadionNameTXT.getText());
            alert.setTitle("Stadionname");
            alert.setContentText(s);
            alert.show();
        }
        String name;
        String passwort;
        String email;
        String line_up;

        if(teamNameTXT.getText().isEmpty()){
            line_up = null;
        }else{
            line_up = teamNameTXT.getText();
        }

        if(nameTXT.getText().isEmpty()){
            name = null;
        }else{
            name = nameTXT.getText();
        }

        if(passwortTXT.getText().isEmpty()){
            passwort = null;
        }else{
        	if(validatePassword()) {
            passwort = passwortTXT.getText();
        	}
        	else {
        		passwort = null;
        		return;
        	}
        }
        if(emailTXT.getText().isEmpty()){
            email = null;
       }else{
        	if (validateEmail()) {
            	email = emailTXT.getText();
            }else {
            	email = null;
            	return;
            	}
        }
        if(emailTXT.getText().isEmpty() && nameTXT.getText().isEmpty() && passwortTXT.getText().isEmpty() && teamNameTXT.getText().isEmpty() && StadionNameTXT.getText().isEmpty()) {
            alert.setTitle("Keine Aenderungen");
            alert.setContentText("Keine Aenderungen festgestellt");
            alert.show();
            return;
        }
        String info = ClientConnection.updateUSER(name,passwort,email, line_up);
        alert.setTitle("Änderung");
        alert.setContentText(info);
        alert.show();
    }

    // Quelle des Codes: https://www.youtube.com/watch?v=q5nJktU42q4
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

    public void upgradeStadion(ActionEvent event) {

        String s = ClientConnection.upgradeAsset(2,costStadion + "");
        if(s.equals("Erfolgreich verbessert.")){
            HauptmenueController.stadtionLevel.set(HauptmenueController.stadtionLevel.get() + 1);
            HauptmenueController.seps.set( ClientConnection.getSEPS());
            HauptmenueController.plaetze.set(HauptmenueController.stadtionLevel.getValue()*5000+5000);
        }
        alert.setTitle("Upgrade");
        alert.setContentText(s);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
        update();
    }

    public void upgradeImbiss(ActionEvent event) {

        String s = ClientConnection.upgradeAsset(0,costImbiss+ "");
        if(s.equals("Erfolgreich verbessert.")){
            HauptmenueController.imbissLevel.set(HauptmenueController.imbissLevel.get() + 1);
            HauptmenueController.seps.set(ClientConnection.getSEPS());
        }
        alert.setTitle("Upgrade");
        alert.setContentText(s);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
        update();
    }

    public void upgradeParkplatz(ActionEvent event) {

        String s = ClientConnection.upgradeAsset(1,costParkplatz + "");
        if(s.equals("Erfolgreich verbessert.")){
            HauptmenueController.parkplatzLevel.set(HauptmenueController.parkplatzLevel.get() + 1);
            HauptmenueController.seps.set(ClientConnection.getSEPS());
        }
        alert.setTitle("Upgrade");
        alert.setContentText(s);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
        update();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stadionBTN.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                upgradeStadion(null);
            }
        });

        imbissBTN.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                upgradeImbiss(null);
            }
        });

        parkplatzBTN.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                upgradeParkplatz(null);
            }
        });
        update();
    }

    private void update(){

        Assets assets = ClientConnection.getAssets();

        int [] costs = getCostFromAsset(assets);

        costImbiss = costs[0];
        costParkplatz = costs[1];
        costStadion = costs[2];

        imbissBTN.setText(costImbiss + "");
        parkplatzBTN.setText(costParkplatz + "");
        stadionBTN.setText(costStadion + "");
    }

    private int[] getCostFromAsset(Assets assets) {

        int[] costs = new int[3];

        switch (assets.getImbiss()){

            case 0:
                costs[0] = 500;
                break;
            case 1:
                costs[0] = 1000;
                break;
            case 2:
                costs[0] = 1500;
                break;
            case 3:
                costs[0] = 0;
                break;
        }

        switch (assets.getParkplatz()){

            case 0:
                costs[1] = 500;
                break;
            case 1:
                costs[1] = 1000;
                break;
            case 2:
                costs[1] = 1500;
                break;
            case 3:
                costs[1] = 0;
                break;
        }

        switch (assets.getKapazitaet()){

            case 0:
                costs[2] = 500;
                break;
            case 1:
                costs[2] = 1000;
                break;
            case 2:
                costs[2] = 1500;
                break;
            case 3:
                costs[2] = 0;
                break;
        }
        return costs;
    }
}


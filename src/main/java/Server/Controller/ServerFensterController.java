package Server.Controller;


import Server.Scrapper;
import Server.Server_Socket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

import java.io.IOException;


public class ServerFensterController {

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private int portnummer = 8000;
    private Server_Socket test = new Server_Socket(portnummer);
    @FXML
    private Text scrapped;

    @FXML
    private Button scrappenBTN;

    @FXML
    private TextField serverportTXT;

    @FXML
    void exit(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    void serverport(ActionEvent event) throws IOException {
        if (serverportTXT.getText().isEmpty()) {
            alert.setTitle("Achtung!");
            alert.setContentText("Es wurde keine Port angegeben, Standartport 8000 wird benutzt");
            alert.show();
            return;

        }
        this.test.setPort(Integer.parseInt(serverportTXT.getText()));
        alert.setTitle("Port geaendert");
        alert.setContentText("Port wurde erfolgreich geandert");
        alert.show();
    }

    @FXML
    void startserver(ActionEvent event) throws IOException {
        if (serverportTXT.getText().isEmpty()) {
            alert.setTitle("Achtung!");
            alert.setContentText("Server wird mit dem Port 8000 gestartet");
            alert.show();

        }
        if (!serverportTXT.getText().isEmpty()) {
            alert.setTitle("");
            alert.setContentText("Server wird mit dem Port " + serverportTXT.getText() + " gestartet");
            alert.show();

        }
        Thread t = new Thread() {
            public void run() {
                test.run();
            }
        };
        t.start();
    }

    @FXML
    void stopserver(ActionEvent event) throws IOException {
        //this.test.stop();
    }

    @FXML
    void getsamtstaeke(ActionEvent event){
        Scrapper.stopScrapping();
    }

    @FXML
    void startScrappen(ActionEvent event) {
        Thread scrapper = new Thread() {
            public void run() {
                Scrapper.getNames();
            }
        };
        scrapper.start();
        scrappenBTN.setDisable(true);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        scrapped.setText("Spieler in Datenbank: " + Scrapper.scrapped);
                    }
                },
                5000,5000
        );
    }

    @FXML
    void stopScrappen(ActionEvent event) throws InterruptedException {
        Scrapper.stopScrapping();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        scrappenBTN.setDisable(false);

                    }
                },
                7500
        );
    }


}

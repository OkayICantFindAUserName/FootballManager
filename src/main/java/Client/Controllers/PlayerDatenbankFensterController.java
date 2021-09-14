package Client.Controllers;

import Client.Models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;


import java.net.URL;
import java.util.*;

public class PlayerDatenbankFensterController implements Initializable {
    @FXML
    TableView<Player> tableView;
    @FXML
    TableColumn<Player, Integer> idCLN;
    @FXML
    TableColumn<Player, String> nameCLN;
    @FXML
    TableColumn<Player, Integer> alterCLN;
    @FXML
    TableColumn<Player, String> nationCLN;
    @FXML
    TableColumn<Player, String> positionCLN;
    @FXML
    TableColumn<Player, String> vereinCLN;
    @FXML
    TableColumn<Player, Integer> strengthCLN;

    @FXML
    private TextField idTXT;

    @FXML
    private TextField nameTXT;

    @FXML
    private TextField alterTXT;

    @FXML
    private TextField nationTXT;

    @FXML
    private TextField posTXT;

    @FXML
    private TextField vereinTXT;

    @FXML
    private TextField staerkeTXT;

    private static List<Player> playerList;
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    void filtern(ActionEvent event) {
        List<Player> listOfPlayers = new ArrayList<>();
        listOfPlayers.addAll(playerList);
        if (!idTXT.getText().isEmpty()) {
            try {
                int id = Integer.parseInt(idTXT.getText());
                listOfPlayers.removeIf(player -> !(player.getId() == id));
            } catch (Exception e) {
                alert.setTitle("ID muss eine Zahl sein");
                alert.setContentText("Bitte geben Sie bei ID eine Zahl ein.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        }
        if (!nameTXT.getText().isEmpty()) {
            listOfPlayers.removeIf(player -> !player.getName().toLowerCase().matches("(.*)" + nameTXT.getText().toLowerCase() + "(.*)"));
        }
        if (!alterTXT.getText().isEmpty()) {
            String[] agesString = alterTXT.getText().split(",");
            try {
                int[] ages = new int[agesString.length];
                for (int i = 0; i < agesString.length; i++) {
                    ages[i] = Integer.parseInt(agesString[i]);
                }
                listOfPlayers.removeIf(player -> !checkAge(player.getDateOfBirth(), ages));

            } catch (Exception e) {
                alert.setTitle("Das Alter muss ein Integer sein");
                alert.setContentText("Bitte geben Sie das Alter eine Zahl ein. Mehre werte geben Sie bitte so ein : 20,21,22");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        }
        if (!nationTXT.getText().isEmpty()) {
            listOfPlayers.removeIf(player -> !player.getNation().toLowerCase().matches("(.*)" + nationTXT.getText().toLowerCase() + "(.*)"));
        }
        if (!vereinTXT.getText().isEmpty()) {
            listOfPlayers.removeIf(player -> !player.getClub().toLowerCase().matches("(.*)" + vereinTXT.getText().toLowerCase() + "(.*)"));
        }
        if (!posTXT.getText().isEmpty()) {
            listOfPlayers.removeIf(player -> !player.getPos().toLowerCase().matches("(.*)" + posTXT.getText().toLowerCase() + "(.*)"));
        }
        if (!staerkeTXT.getText().isEmpty()) {
            try{
                double strength = Double.parseDouble(staerkeTXT.getText());
                listOfPlayers.removeIf(player -> player.getGesamtStaerke() != strength);

            }catch(Exception e){
                alert.setTitle("Die Staerke muss ein Integer sein");
                alert.setContentText("Bitte geben Sie eine Zahl ein.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        }
        tableView.getItems().clear();
        tableView.getItems().addAll(listOfPlayers);
        tableView.refresh();
    }
//

    private boolean checkAge(int dateOfBirth, int[] toCheck) {
        int age = dateOfBirth;

        for (int i = 0; i < toCheck.length; i++) {
            if (toCheck[i] == age) {
                return true;
            }

        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert.getDialogPane().getStylesheets().add(
                LoginFensterController.class.getClassLoader().getResource("stylesheet.css").toExternalForm());
        alert.initStyle(StageStyle.UNDECORATED);
        Thread t = new Thread(() -> {
            idCLN.setCellValueFactory(new PropertyValueFactory<Player, Integer>("id"));
            nameCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
            alterCLN.setCellValueFactory(new PropertyValueFactory<Player, Integer>("dateOfBirth"));
            nationCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("nation"));
            positionCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("pos"));
            vereinCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("club"));
            strengthCLN.setCellValueFactory(new PropertyValueFactory<Player, Integer>("strength"));

            if(playerList == null){
                System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
                playerList = parseUserList();
            }
            tableView.getItems().addAll(playerList);

        });
        t.start();

    }

    //Network methode um user vom server zu bekommen
    private List<Player> parseUserList() {
        return ClientConnection.getAllPlayerFromServer();
    }

}


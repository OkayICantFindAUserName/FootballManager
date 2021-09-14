package Client.Controllers;

import Client.Models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
public class LootboxenOeffnenController implements Initializable{

    @FXML
    private TableView<Player> PlayerTBL;

    @FXML
    private TableColumn<Player, String> nameCLN;

    @FXML
    private TableColumn<Player, Integer> strengthCLN;

    @FXML
    private TableColumn<Player, String> posCLN;

    @FXML
    private TableColumn<Player, String> nationCLN;

    @FXML
    private Text sepsTXT;

    @FXML
    private TextField sizeTXT;


    private static List<Player> playerList;
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    void open(ActionEvent event) {
        if(!sizeTXT.getText().isEmpty()){
            try{
                int size = Integer.parseInt(sizeTXT.getText());
                if(size  == 0){
                    throw new Exception();
                }
                playerList = ClientConnection.openLootBox(size);
                update();
            }catch (Exception e){
                alert.setTitle("Ungueltige Eingabe");
                alert.setContentText("Bitte geben sie die gewuenschte Anzahl der Spieler" +
                        "\nals numerische Zahl ein");
                alert.show();
            }
        }
        else{
            alert.setTitle("Kein Wert");
            alert.setContentText("Bitte geben Sie zuerst die gewuenschte Anzahl " +
                    "\nder Spieler ihrer Lootbox ein");
            alert.show();
        }

    }


    private void update() {
        int seps = ClientConnection.getSEPS();
        sepsTXT.setText(String.valueOf(seps));
        HauptmenueController.seps.set(seps);
        PlayerTBL.getItems().clear();
        Thread t = new Thread(() -> {
            nameCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
            strengthCLN.setCellValueFactory(new PropertyValueFactory<Player, Integer>("strength"));
            nationCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("nation"));
            posCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("pos"));

            PlayerTBL.getItems().addAll(playerList);

        });
        t.start();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert.getDialogPane().getStylesheets().add(
                LoginFensterController.class.getClassLoader().getResource("stylesheet.css").toExternalForm());
        alert.initStyle(StageStyle.UNDECORATED);
        int seps = ClientConnection.getSEPS();
        sepsTXT.setText(String.valueOf(seps));
    }
}

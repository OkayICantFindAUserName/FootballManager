package Client.Controllers;


import Client.Controllers.ClientConnection;
import Client.Controllers.HauptmenueController;
import Client.Controllers.LoginFensterController;
import Client.Models.Player;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
public class TeamViewController implements Initializable {

    @FXML
    private TableView<Player> StartelfTBL;

    @FXML
    private TableColumn<Player, String> startelfNameCLN;

    @FXML
    private TableColumn<Player, Integer> startelfstrengthCLN;

    @FXML
    private TableColumn<Player, String> startElfPosCLN;

    @FXML
    private ChoiceBox<String> lineUpCBOX;

    @FXML
    private TableView<Player> teamTBL;

    @FXML
    private TableColumn<Player, String> teamNameCLN;

    @FXML
    private TableColumn<Player, Integer> teamstrengthCLN;

    @FXML
    private TableColumn<Player, String> teamPosCLN;

    @FXML
    private Text TeamNameTXT;

    private static List<Player> teamPlayerList;
    private static List<Player> startelfPlayerList;
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private Player selectedPlayer;

    @FXML
    void add(ActionEvent event) {
        if (selectedPlayer != null && !startelfPlayerList.contains(selectedPlayer)) {
            startelfPlayerList.add(selectedPlayer);
            teamPlayerList.remove(selectedPlayer);
            update();
        }
    }

    @FXML
    void remove(ActionEvent event) {
        if (selectedPlayer != null && !teamPlayerList.contains(selectedPlayer)) {
            startelfPlayerList.remove(selectedPlayer);
            teamPlayerList.add(selectedPlayer);
            update();
        }
    }

    @FXML
    void sell(ActionEvent event) {
        if (selectedPlayer != null) {
            if(teamPlayerList.contains(selectedPlayer)) {
                teamPlayerList.remove(selectedPlayer);  }
            else {
                startelfPlayerList.remove(selectedPlayer);
            }
            ClientConnection.sellPlayer(selectedPlayer.getId());
            int i = (int) (HauptmenueController.seps.getValue() + selectedPlayer.getGesamtStaerke());
            HauptmenueController.seps.set(i);

            alert.setTitle("Spieler verkauft");
            alert.setContentText("Spieler erfolgreich verkauft!");
            alert.show();
            update();
        }
    }

    @FXML
    void save(ActionEvent event) {
        if (startelfPlayerList.size() != 11) {
            alert.setTitle("Startelf");
            alert.setContentText("Es müssen sich 11 Spieler in der Startelf befinden.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        if (lineUpCBOX.getValue().isEmpty()) {
            alert.setTitle("Line-Up");
            alert.setContentText("Bitte wählen Sie ihre Aufstellung aus.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        String[] amount = lineUpCBOX.getValue().split("-");
        Integer[] anzahlDerSpieler = new Integer[amount.length];

        for (int i = 0; i < amount.length; i++) {
            anzahlDerSpieler[i] = Integer.parseInt(amount[i]);
        }
        int tw = 0;
        for (Player p : startelfPlayerList) {
            if (p.getPos().equals("DF")) {
                anzahlDerSpieler[0]--;
            }
            if (p.getPos().equals("MF")) {
                anzahlDerSpieler[1]--;
            }
            if (p.getPos().equals("FW")) {
                anzahlDerSpieler[2]--;
            }
            if (p.getPos().equals("TW")) {
                tw++;
            }
        }
        if(anzahlDerSpieler[0] > 0 ){
            alert.setTitle("Line-Up");
            alert.setContentText("Bitte setzen Sie genug Verteidiger ein, die stehen ganz unten auf der Gehaltsliste");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        if(anzahlDerSpieler[1] > 0 ){
            alert.setTitle("Line-Up");
            alert.setContentText("Bitte setzen Sie genug Mittelfeldspieler ein, deren Ego waechst nicht von allein.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        if(anzahlDerSpieler[2] > 0 ){
            alert.setTitle("Line-Up");
            alert.setContentText("Bitte setzen Sie genug Stürmer ein, wer putzt uns sonst den Pfosten sauber?");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        if(tw != 1 ){
            alert.setTitle("Line-Up");
            alert.setContentText("Bitte setzen Sie einen Torwart ein, sonst war die Ausruestung umsonst.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        String s = ClientConnection.addPlayerToStartelf(startelfPlayerList,lineUpCBOX.getValue());
        if(s.equals("-1")){
            alert.setTitle("Fehler");
            alert.setContentText("Bitte setzen Sie sich mit ihren Admin in Kontakt.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();

        }else{
            alert.setTitle("Erfolg");
            alert.setContentText("Erfoglreich angelegt.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert.getDialogPane().getStylesheets().add(
                LoginFensterController.class.getClassLoader().getResource("stylesheet.css").toExternalForm());
        alert.initStyle(StageStyle.UNDECORATED);
        String teamName = ClientConnection.getTeamName();
        TeamNameTXT.setText(teamName);
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "4-4-2",
                        "5-4-1",
                        "3-4-3",
                        "4-3-3",
                        "3-5-2"
                );
        lineUpCBOX.getItems().addAll(options);
        lineUpCBOX.setValue("4-4-2");

        teamTBL.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells = teamTBL.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                selectedPlayer = teamTBL.getSelectionModel().getSelectedItem();

            }
        });

        StartelfTBL.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells2 = StartelfTBL.getSelectionModel().getSelectedCells();

        selectedCells2.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                selectedPlayer = StartelfTBL.getSelectionModel().getSelectedItem();

            }
        });


        Thread t = new Thread(() -> {

            startelfNameCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
            startElfPosCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("pos"));
            startelfstrengthCLN.setCellValueFactory(new PropertyValueFactory<Player, Integer>("strength"));
            startelfPlayerList = parsestartelfPlayerList();
            StartelfTBL.getItems().addAll(startelfPlayerList);

            teamNameCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
            teamPosCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("pos"));
            teamstrengthCLN.setCellValueFactory(new PropertyValueFactory<Player, Integer>("strength"));
            teamPlayerList = parsePlayerList();

            for (Player p :
                    startelfPlayerList) {
                teamPlayerList.removeIf(player -> player.getId() == p.getId());
            }
            teamTBL.getItems().addAll(teamPlayerList);

        });
        t.start();


    }


    private void update() {
        teamTBL.getItems().clear();
        StartelfTBL.getItems().clear();
        Thread t = new Thread(() -> {
            teamNameCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
            teamPosCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("pos"));
            teamstrengthCLN.setCellValueFactory(new PropertyValueFactory<Player, Integer>("strength"));
            teamTBL.getItems().addAll(teamPlayerList);

            startelfNameCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
            startElfPosCLN.setCellValueFactory(new PropertyValueFactory<Player, String>("pos"));
            startelfstrengthCLN.setCellValueFactory(new PropertyValueFactory<Player, Integer>("strength"));
            StartelfTBL.getItems().addAll(startelfPlayerList);

        });
        t.start();
    }

    private List<Player> parsestartelfPlayerList() {
        return ClientConnection.getStartElf();
    }

    private List<Player> parsePlayerList() {
        return ClientConnection.getPlayersFromTeam();
    }

    public void removeAll(ActionEvent event) {

        teamPlayerList.addAll(startelfPlayerList);
        startelfPlayerList.clear();
        update();

    }
}

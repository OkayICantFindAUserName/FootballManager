package Client.Controllers;

import Client.Models.MatchWrapper;
import Client.Models.User;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
public class MatchFensterController implements Initializable {

    @FXML
    private TableView<User> FriendsTBL;

    @FXML
    private TableColumn<User, String> friendsNameCLN;

    @FXML
    private TableColumn<User, String> firendsEmailCLN;

    @FXML
    private TableView<User> herrausgefordertTBL;

    @FXML
    private TableColumn<User, String> herrausgefordertNameCLN;

    @FXML
    private TableColumn<User, String> herrausgefordertEmailCLN;

    @FXML
    private TableView<User> herrausforderungTBL;

    @FXML
    private TableColumn<User, String> herrausforderungNameCLN;

    @FXML
    private TableColumn<User, String> herrausforderungEmailCLN;

    @FXML
    private TableView<MatchWrapper> historyTBL;

    @FXML
    private TableColumn<MatchWrapper, String> historyNameCLN;

    @FXML
    private TableColumn<MatchWrapper, String> historyTeamName;

    @FXML
    private TableColumn<MatchWrapper, String> historyResultCLN;

    private static List<MatchWrapper> matchHistory;
    private static List<User> friends;
    private static List<User> requested;
    private static List<User> requests;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);


    User selectedFriend = null;
    User selectedRequestFriend = null;

    @FXML
    void ablehnen(ActionEvent event) {
        if (selectedRequestFriend != null) {
            if (!selectedRequestFriend.getUserName().isEmpty()) {
                String s = ClientConnection.declineMatchRequest(selectedRequestFriend.getUserName());
                update("decline");
            }
        }
    }

    @FXML
    void annhemen(ActionEvent event) {
        if (selectedRequestFriend != null) {
            if (!selectedRequestFriend.getUserName().isEmpty()) {
                String s = ClientConnection.acceptMatch(selectedRequestFriend.getUserName());
                alert.setTitle("Ergebniss");
                alert.setContentText(s);
                alert.show();
                update("accept");
            }
        }
    }

    @FXML
    void herrausfordern(ActionEvent event) {
        if (selectedFriend != null) {
            if (!selectedFriend.getUserName().isEmpty()) {
                String s = ClientConnection.requestMatch(selectedFriend.getUserName());
                alert.setTitle("Herrausgefordert");
                alert.setContentText("Sie haben ihren Freund herrausgefordert");
                alert.show();
                update("challenge");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        herrausforderungTBL.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells = herrausforderungTBL.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                selectedRequestFriend = herrausforderungTBL.getSelectionModel().getSelectedItem();
                System.out.println(selectedRequestFriend);
            }
        });

        FriendsTBL.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells2 = FriendsTBL.getSelectionModel().getSelectedCells();

        selectedCells2.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                selectedFriend = FriendsTBL.getSelectionModel().getSelectedItem();
                System.out.println(selectedFriend);
            }
        });

        Thread t = new Thread(() -> {

            matchHistory = null;
            friends = null;
            requested = null;
            requests = null;

            FriendsTBL.getItems().clear();
            herrausforderungTBL.getItems().clear();
            historyTBL.getItems().clear();
            herrausgefordertTBL.getItems().clear();

            historyNameCLN.setCellValueFactory(new PropertyValueFactory<MatchWrapper, String>("Name"));
            historyResultCLN.setCellValueFactory(new PropertyValueFactory<MatchWrapper, String>("goals"));
            historyTeamName.setCellValueFactory(new PropertyValueFactory<MatchWrapper, String>("TeamName"));
            matchHistory = getMatchHistory();
            historyTBL.getItems().addAll(matchHistory);

            herrausgefordertNameCLN.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
            herrausgefordertEmailCLN.setCellValueFactory(new PropertyValueFactory<User, String>("eMail"));
            requested = getRequested();
            herrausgefordertTBL.getItems().addAll(requested);

            friendsNameCLN.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
            firendsEmailCLN.setCellValueFactory(new PropertyValueFactory<User, String>("eMail"));
            friends = getFriends();
            FriendsTBL.getItems().addAll(friends);


            herrausforderungNameCLN.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
            herrausforderungEmailCLN.setCellValueFactory(new PropertyValueFactory<User, String>("eMail"));
            requests = getMatchRequests();
            herrausforderungTBL.getItems().addAll(requests);

        });
        t.start();

    }

    private void update(String s) {
        Thread t = new Thread(() -> {
            if (s.equals("decline")) {
                requests = getMatchRequests();
                herrausforderungTBL.getItems().clear();
                herrausforderungTBL.getItems().addAll(requests);
            }

            if (s.equals("accept")) {
                requests = getMatchRequests();
                herrausforderungTBL.getItems().clear();
                herrausforderungTBL.getItems().addAll(requests);

                matchHistory = getMatchHistory();
                historyTBL.getItems().clear();
                historyTBL.getItems().addAll(matchHistory);
            }

            if (s.equals("challenge")) {
                requested = getRequested();
                herrausforderungTBL.getItems().clear();
                herrausgefordertTBL.getItems().addAll(requested);
            }

        });

        Platform.runLater(t);
    }

    private List<User> getRequested() {
        return ClientConnection.getRequestedMatches();
    }

    private List<User> getMatchRequests() {
        return ClientConnection.getMatchRequests();
    }

    private List<MatchWrapper> getMatchHistory() {
        return ClientConnection.getMatchHistory();
    }

    private List<User> getFriends() {
        return ClientConnection.checkForNonPendingFriends();
    }
}

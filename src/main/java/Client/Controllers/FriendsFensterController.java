package Client.Controllers;

import Client.Main;
import Client.Models.User;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FriendsFensterController implements Initializable {

    @FXML
    VBox root;

    //pending
    @FXML
    private TableView<User> pendingFriendsTBL;

    @FXML
    private TableColumn<User, String> pendingNameCLN;

    @FXML
    private TableColumn<User, String> pendingEmailCLN;

    @FXML
    private TextField requestTXT;

    //friends
    @FXML
    private TableView<User> FriendstableTBL;

    @FXML
    private TableColumn<User, String> friendNameCLN;

    @FXML
    private TableColumn<User, String> friendEmailCLN;

    //requests
    @FXML
    private TableView<User> requestTBL;

    @FXML
    private TableColumn<User, String> requestNameCLN;

    @FXML
    private TableColumn<User, String> requestEmailCLN;
//
    @FXML
    private TextField requestNameTXT;
    @FXML
    private TextField requestEmailTXT;
    @FXML
    private TextField pendingNameTXT;
    @FXML
    private TextField pendingEmailTXT;
    @FXML
    private TextField vNameTXT;
    @FXML
    private TextField vEmailTXT;

    private static List<User> pendingUsers;

    private static List<User> friends;

    private static List<User> requests;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    boolean init = true;
    User selectedFriend = null;
    User selectedRequestFriend = null;

    private static Parent loadFXML(String fxml) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @FXML
    void suchenR(ActionEvent event){
        List<User> reFriend = new ArrayList<>();
        reFriend.addAll(pendingUsers);

        if (!requestNameTXT.getText().isEmpty()) {
            reFriend.removeIf(user -> !user.getUserName().toLowerCase().matches("(.*)" + requestNameTXT.getText().toLowerCase() + "(.*)"));
        }
        if (!requestEmailTXT.getText().isEmpty()) {
            reFriend.removeIf(user -> !user.geteMail().toLowerCase().matches("(.*)" + requestEmailTXT.getText().toLowerCase() + "(.*)"));
        }

        pendingFriendsTBL.getItems().clear();
        pendingFriendsTBL.getItems().addAll(reFriend);
        pendingFriendsTBL.refresh();
    }

    @FXML
    void showprofil(ActionEvent event){
        if (selectedFriend != null) {
            if (!selectedFriend.getUserName().isEmpty()) {
                FreundeProfilController.friendnameproperty.set(selectedFriend.getUserName());
                try {
                    root.getChildren().clear();
                    root.getChildren().add(loadFXML("FreundeProfil"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @FXML
    void suchenP(ActionEvent event){
        List<User> peFriend = new ArrayList<>();
        peFriend.addAll(requests);

        if (!pendingNameTXT.getText().isEmpty()) {
            peFriend.removeIf(user -> !user.getUserName().toLowerCase().matches("(.*)" + pendingNameTXT.getText().toLowerCase() + "(.*)"));
        }
        if (!pendingEmailTXT.getText().isEmpty()) {
            peFriend.removeIf(user -> !user.geteMail().toLowerCase().matches("(.*)" + pendingEmailTXT.getText().toLowerCase() + "(.*)"));
        }

        requestTBL.getItems().clear();
        requestTBL.getItems().addAll(peFriend);
        requestTBL.refresh();
    }
    @FXML
    void suchenF(ActionEvent event){
        List<User> vFriend = new ArrayList<>();
        vFriend.addAll(friends);

        if (!vNameTXT.getText().isEmpty()) {
            vFriend.removeIf(user -> !user.getUserName().toLowerCase().matches("(.*)" + vNameTXT.getText().toLowerCase() + "(.*)"));
        }
        if (!vEmailTXT.getText().isEmpty()) {
            vFriend.removeIf(user -> !user.geteMail().toLowerCase().matches("(.*)" + vEmailTXT.getText().toLowerCase() + "(.*)"));
        }

        FriendstableTBL.getItems().clear();
        FriendstableTBL.getItems().addAll(vFriend);
        FriendstableTBL.refresh();
    }


    @FXML
    void remove(ActionEvent event) {

        if (selectedFriend != null) {
            if (!selectedFriend.getUserName().isEmpty()) {
                String s = ClientConnection.removeFriend(selectedFriend.getUserName());
                System.out.println(s);
                update("remove");
            }
        }

    }

    @FXML
    void sentRequest(ActionEvent event) {
        if (!requestTXT.getText().isEmpty()) {
            String alle = ClientConnection.sendFriendInvite(requestTXT.getText());
            alert.setTitle("Freunschaftsanfrage");
            alert.setContentText(alle);
            alert.show();
        }
        update("request");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestTBL.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells = requestTBL.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                selectedRequestFriend = requestTBL.getSelectionModel().getSelectedItem();

            }
        });

        FriendstableTBL.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells2 = FriendstableTBL.getSelectionModel().getSelectedCells();

        selectedCells2.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                selectedFriend = FriendstableTBL.getSelectionModel().getSelectedItem();

            }
        });
        pendingUsers = null;
        friends = null;
        requests = null;

        Thread t = new Thread(() -> {
            pendingNameCLN.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
            pendingEmailCLN.setCellValueFactory(new PropertyValueFactory<User, String>("eMail"));
            if(pendingUsers == null){
                pendingUsers = getPendingFriends();
            }
            pendingFriendsTBL.getItems().addAll(pendingUsers);


            friendNameCLN.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
            friendEmailCLN.setCellValueFactory(new PropertyValueFactory<User, String>("eMail"));
            if(friends == null){
                friends = getFriends();
            }
            FriendstableTBL.getItems().addAll(friends);


            requestNameCLN.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
            requestEmailCLN.setCellValueFactory(new PropertyValueFactory<User, String>("eMail"));
            if(requests == null){
                requests = getFriendRequest();
                System.out.println(requests);
            }
            requestTBL.getItems().addAll(requests);

        });
        t.start();


    }

    private void update(String s) {
        Thread t = new Thread(() -> {
            if (s.equals("decline")) {
                requests = getFriendRequest();
                requestTBL.getItems().clear();
                requestTBL.getItems().addAll(requests);
                return;
            }

            if (s.equals("remove")) {
                friends = getFriends();
                FriendstableTBL.getItems().clear();
                FriendstableTBL.getItems().addAll(friends);
                return;
            }

            if (s.equals("request")) {
                pendingUsers = getPendingFriends();
                pendingFriendsTBL.getItems().clear();
                pendingFriendsTBL.getItems().addAll(pendingUsers);
                return;
            }

            if (s.equals("accept")) {
                requests = getFriendRequest();
                requestTBL.getItems().clear();
                requestTBL.getItems().addAll(requests);

                friends = getFriends();
                FriendstableTBL.getItems().clear();
                FriendstableTBL.getItems().addAll(friends);
                return;
            }
        });

        Platform.runLater(t);
    }

    @FXML
    void decline(ActionEvent event) {
        if (selectedRequestFriend != null) {
            if (!selectedRequestFriend.getUserName().isEmpty()) {
                String s = ClientConnection.declineFriend(selectedRequestFriend.getUserName());
                System.out.println(s);
                update("decline");
            }
        }
    }

    @FXML
    void accept(ActionEvent event) {
        if (selectedRequestFriend != null) {
            if (!selectedRequestFriend.getUserName().isEmpty()) {
                String s = ClientConnection.acceptFriend(selectedRequestFriend.getUserName());
                System.out.println(s);
                update("accept");
            }
        }
    }

    private List<User> getPendingFriends() {
        return ClientConnection.getRequestedFriends();
    }

    private List<User> getFriends() {
        return ClientConnection.checkForNonPendingFriends();
    }

    private List<User> getFriendRequest() {
        return ClientConnection.checkForPendingFriends();
    }

}

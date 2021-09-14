package Client.Controllers;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import Client.Models.Nachricht;
import Client.Models.User;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ChatController implements Initializable{

	private static User theOne;
	@FXML
	private TableColumn<User, String> friendNameCLN;

	@FXML
	private TableColumn<Nachricht, String> SenderNameCLN;

	@FXML
	private TableColumn<Nachricht, Date> DateCLN;

	@FXML
	private TableColumn<Nachricht, String> NachrichtCLN;

	@FXML
	private TableColumn<Nachricht, String> ReceiverCLN;

	@FXML
	private TableColumn<Nachricht, Date> ReceiveDateCLN;

	@FXML
	private TableColumn<Nachricht, String> GesendeteNachrichtCLN;

	@FXML
	private TableView<User> FriendstableTBL;

	@FXML
	private TableView<Nachricht> MessagetableTBL;

	@FXML
	private TableView<Nachricht> SenttableTBL;

	private static List<Nachricht> receivedMessages;

	private static List<Nachricht> sentMessages;

	private static List<User> friends;

	private Alert alert = new Alert(Alert.AlertType.INFORMATION);


	User selectedFriend = null;
	Nachricht selectedMessage = null;


	@FXML
	void openNewWindow(ActionEvent event) {
		try{
			if(selectedFriend!=null) {
				TextController.friendnameproperty.set(selectedFriend.getUserName());
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/text.fxml"));
				Scene root = new Scene(fxmlLoader.load());
				Stage stage = new Stage();
				stage.setTitle("Nachricht schreiben");
				stage.setScene((root));
				stage.show();
			}
			else{
				alert.setTitle("ERROR");
				alert.setContentText("Bitte suche einen Freund aus!");
				alert.show();
				theOne = selectedFriend;

			}
		}catch (Exception e){
			System.out.println("Cant load new window");
			e.printStackTrace();
		}
	}

	@FXML
	private void refresh(){
		receivedMessages = getReceivedMessages();
		MessagetableTBL.getItems().clear();
		MessagetableTBL.getItems().addAll(receivedMessages);

	}

	@FXML
	private void refreshSent(){
		sentMessages = getSentMessages();
		SenttableTBL.getItems().clear();
		SenttableTBL.getItems().addAll(sentMessages);
	}


	private List<User> getFriends() {
		return ClientConnection.checkForNonPendingFriends();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		MessagetableTBL.getSelectionModel().setCellSelectionEnabled(true);
		ObservableList selectedCells = MessagetableTBL.getSelectionModel().getSelectedCells();

		selectedCells.addListener(new ListChangeListener() {
			@Override
			public void onChanged(Change c) {
				 selectedMessage = MessagetableTBL.getSelectionModel().getSelectedItem();

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

		SenttableTBL.getSelectionModel().setCellSelectionEnabled(true);
		ObservableList selectedCells3 = SenttableTBL.getSelectionModel().getSelectedCells();

		selectedCells.addListener(new ListChangeListener() {
			@Override
			public void onChanged(Change c) {
				selectedMessage = SenttableTBL.getSelectionModel().getSelectedItem();

			}
		});

		friends = null;

		Thread t = new Thread(() -> {
			friendNameCLN.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
			if(friends == null){
				friends = getFriends();
			}
			FriendstableTBL.getItems().addAll(friends);

			SenderNameCLN.setCellValueFactory(new PropertyValueFactory<Nachricht, String>("senderName"));
			DateCLN.setCellValueFactory(new PropertyValueFactory<Nachricht, Date>("date"));
			NachrichtCLN.setCellValueFactory(new PropertyValueFactory<Nachricht, String>("nachricht"));

			MessagetableTBL.getItems().clear();

			if(receivedMessages == null){
				receivedMessages = getReceivedMessages();
			}

			ReceiverCLN.setCellValueFactory(new PropertyValueFactory<Nachricht, String>("receiverName"));
			ReceiveDateCLN.setCellValueFactory(new PropertyValueFactory<Nachricht, Date>("date"));
			GesendeteNachrichtCLN.setCellValueFactory(new PropertyValueFactory<Nachricht, String>("nachricht"));

			if(sentMessages == null){
				sentMessages = getSentMessages();
			}
			SenttableTBL.getItems().addAll(sentMessages);
		});

		t.start();

	}

	private List<Nachricht> getSentMessages() { return ClientConnection.getSentMessages(); }

	private List<Nachricht> getReceivedMessages() { return ClientConnection.getMessages(); }

}
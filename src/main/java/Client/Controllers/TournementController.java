package Client.Controllers;

import Client.Main;
import Client.Models.Tournement;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TournementController implements Initializable {

    public static Tournement selectedDetailTournement;

    @FXML
    private HBox root;

    @FXML
    private TableView<Tournement> allTournementsTBL;

    @FXML
    private TableColumn<Tournement, String> allTournementsName;

    @FXML
    private TableColumn<Tournement, Integer> allTournementsFee;

    @FXML
    private TableColumn<Tournement, String> allTournementsType;

    @FXML
    private TableColumn<Tournement, Integer> allTournementsAmount;

    @FXML
    private TableColumn<Tournement, Integer> allTournementsMaxAmount;

    @FXML
    private TableView<Tournement> pendingTournements;

    @FXML
    private TableColumn<Tournement, String> pendingTournementsName;

    @FXML
    private TableColumn<Tournement, Integer> pendingTournementsFee;

    @FXML
    private TableColumn<Tournement, String> pendingTournementsType;

    @FXML
    private TableColumn<Tournement, Integer> pendingTournementsAmount;

    @FXML
    private TableColumn<Tournement, Integer> pendingTournementsMaxAmount;

    @FXML
    private TableView<Tournement> createdTournement;

    @FXML
    private TableColumn<Tournement, String> createdTournementName;

    @FXML
    private TableColumn<Tournement, Integer> createdTournementFee;

    @FXML
    private TableColumn<Tournement, String> createdTournementType;

    @FXML
    private TableColumn<Tournement, Integer> createdTournementAmount;

    @FXML
    private TableColumn<Tournement, Integer> createdTournementMaxAmount;

    @FXML
    private TextField NameTXT;

    @FXML
    private TextField feeTXT;

    @FXML
    private ChoiceBox<String> typeCBOC;

    @FXML
    private TextField maxAmountTXT;

    @FXML
    private TableView<Tournement> HistoryTournement;

    @FXML
    private TableColumn<Tournement, String> HistoryTournementName;

    @FXML
    private TableColumn<Tournement, String> historyTournementType;

    @FXML
    void startNewTournement(ActionEvent event) {

        int fee;
        int maxAmount;
        if(!NameTXT.getText().isEmpty() && !feeTXT.getText().isEmpty() && !maxAmountTXT.getText().isEmpty() && !typeCBOC.getValue().isEmpty()){
            try{
                fee = Integer.parseInt(feeTXT.getText());
                maxAmount = Integer.parseInt(maxAmountTXT.getText());
                if(fee > 10000 || maxAmount > 32){
                    throw new Exception();
                }
            }catch (Exception e){
                alert.setTitle("Parse Error");
                alert.setContentText("Gebühren und max. Anzahl müssen eine Zahl sein unter 32 sein.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
                return;
            }
            String s;
            String value = typeCBOC.getValue();
            if(value.equals("Knock-Out")){
                System.out.println(maxAmount);
                if(maxAmount != 4 && maxAmount != 8 && maxAmount != 16 && maxAmount != 32){
                    alert.setTitle("Knock-Out error");
                    alert.setContentText("Ein Knock-Out Tunier muss 4,8,16 oder 32 Spieler beinhalten um ein Faires Spiel zu gewährleisten.");
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.show();
                    return;
                }
                Tournement tournement = new Tournement(NameTXT.getText(),fee,maxAmount,true);
                s = ClientConnection.createNewTournement(tournement);
                HauptmenueController.seps.set(ClientConnection.getSEPS());
            }else{
                if(maxAmount < 3){
                    alert.setTitle("Liga error");
                    alert.setContentText("Ein Liga Tunier muss mindestens 3 Spieler beinhalten um ein Faires Spiel zu gewährleisten.");
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.show();
                    return;
                }
                Tournement tournement = new Tournement(NameTXT.getText(),fee,maxAmount,false);
                s = ClientConnection.createNewTournement(tournement);
                HauptmenueController.seps.set(ClientConnection.getSEPS());
            }
            if(s.equals("-2")){
                alert.setTitle("Gleicher Name");
                alert.setContentText("Name gibt es bereits.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }else if(s.equals("-1")){
                alert.setTitle("Kein Teamname");
                alert.setContentText("Bitte wählen sie zuerst einen Teamnamen");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }
            else if(s.equals("-5")){
                alert.setTitle("Zuwenig SEPs");
                alert.setContentText("Sie haben leider zuwenig SEPs für das Tunier");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }
            else if(s.equals("-4")){
                alert.setTitle("Keine Startelf");
                alert.setContentText("Bitte erstellen sie zuerst eine Startelf");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            } else{
                alert.setTitle("Erfolgreich angelegt");
                alert.setContentText("Erfolgreich angelegt");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }
        }else{
            alert.setTitle("Fehlender Wert");
            alert.setContentText("Bitte geben Sie alle Werte an.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        initialize(null,null);
    }

    @FXML
    void teilnehmen(ActionEvent event) {

        if (selectedTournement != null) {
            String s = ClientConnection.addUserToTournement(selectedTournement.getName());
            initialize(null, null);
            if (s.equals("-1")) {
                alert.setTitle("Teamname Fehlt");
                alert.setContentText("Bitte waehlen sie zuerst einen Teamnamen");
                alert.show();
            }

            if (s.equals("-4")) {
                alert.setTitle("Team nicht vollstaending");
                alert.setContentText("Bitte stellen sie zuerst eine Startelf auf");
                alert.show();
            }
            if (s.equals("-5")) {
                alert.setTitle("Zuwenig SEPs");
                alert.setContentText("Sie haben leider zuwenig SEPs für das Tunier");
                alert.show();
            }
            if (s.equals("-3")) {
                alert.setTitle("Tunier nicht mehr Verfügbar");
                alert.setContentText("Das Tunier ist leider nicht mehr Verfügbar");
                alert.show();
            }
            if (s.equals("-6")) {
                alert.setTitle("Bereits im Tunier");
                alert.setContentText("Sie nehmen bereits am Tunier teil");
                alert.show();
            }
            if (s.equals("1")) {
                HauptmenueController.seps.set(HauptmenueController.seps.get() - selectedTournement.getFee());
                alert.setTitle("Tunier teilnahme");
                alert.setContentText("Sie nehmen am Tunier teil");
                alert.show();
            }
            if (s.equals("2")) {
                HauptmenueController.seps.set(HauptmenueController.seps.get() - selectedTournement.getFee());
                HauptmenueController.seps.set(ClientConnection.getSEPS());
                alert.setTitle("Tunier erfolgreich teilgenommen");
                alert.setContentText("Sie haben am Tunier teilgenommen");
                alert.show();
            }
        }
    }

    private static List<Tournement> allTournements;
    private static List<Tournement> allPendingTournements;
    private static List<Tournement> allHistoryTournements;
    private static List<Tournement> allCreatedTournements;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    boolean init = true;

    Tournement selectedTournement = null;
    Tournement selectedOwnTournement = null;

    private static Parent loadFXML(String fxml) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (init) {
            allTournementsTBL.getSelectionModel().setCellSelectionEnabled(true);
            ObservableList selectedCells = allTournementsTBL.getSelectionModel().getSelectedCells();

            selectedCells.addListener(new ListChangeListener() {
                @Override
                public void onChanged(Change c) {
                    selectedTournement = allTournementsTBL.getSelectionModel().getSelectedItem();
                    System.out.println(selectedTournement);
                }
            });
            createdTournement.getSelectionModel().setCellSelectionEnabled(true);
            ObservableList selectedCells2 = createdTournement.getSelectionModel().getSelectedCells();

            selectedCells2.addListener(new ListChangeListener() {
                @Override
                public void onChanged(Change c) {
                    selectedOwnTournement = createdTournement.getSelectionModel().getSelectedItem();
                    System.out.println(selectedOwnTournement);
                }
            });
            HistoryTournement.getSelectionModel().setCellSelectionEnabled(true);
            ObservableList selectedCells3 = HistoryTournement.getSelectionModel().getSelectedCells();

            selectedCells3.addListener(new ListChangeListener() {

                @Override
                public void onChanged(Change c) {
                    selectedDetailTournement = HistoryTournement.getSelectionModel().getSelectedItem();
                    if(selectedDetailTournement.getType().equals("Knock-Out")){
                        try {
                            root.getChildren().clear();
                            root.getChildren().addAll(loadFXML("TournementKODetails"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            root.getChildren().clear();
                            root.getChildren().addAll(loadFXML("TournementLigaDetails"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            ObservableList<String> options =
                    FXCollections.observableArrayList(
                            "Knock-Out",
                            "Liga"
                    );
            typeCBOC.getItems().addAll(options);
            typeCBOC.setValue("Knock-Out");

            init = false;
        }
        Thread t = new Thread(() -> {

            allTournements = null;

            allTournementsTBL.getItems().clear();

            allTournementsName.setCellValueFactory(new PropertyValueFactory<Tournement, String>("Name"));
            allTournementsFee.setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("fee"));
            allTournementsType.setCellValueFactory(new PropertyValueFactory<Tournement, String>("type"));
            allTournementsAmount.setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("anzahl"));
            allTournementsMaxAmount.setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("maxAnzahl"));

            if (allTournements == null) {
                allTournements = getAllTournements();
            }
            allTournementsTBL.getItems().addAll(allTournements);

            allPendingTournements = null;

            pendingTournements.getItems().clear();

            pendingTournementsName.setCellValueFactory(new PropertyValueFactory<Tournement, String>("Name"));
            pendingTournementsFee.setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("fee"));
            pendingTournementsType.setCellValueFactory(new PropertyValueFactory<Tournement, String>("type"));
            pendingTournementsAmount.setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("anzahl"));
            pendingTournementsMaxAmount .setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("maxAnzahl"));

            if (allPendingTournements == null) {
                allPendingTournements = getAllPendingTournements();
            }
            pendingTournements.getItems().addAll(allPendingTournements);

            allHistoryTournements = null;

            HistoryTournement.getItems().clear();

            HistoryTournementName.setCellValueFactory(new PropertyValueFactory<Tournement, String>("Name"));
            historyTournementType.setCellValueFactory(new PropertyValueFactory<Tournement, String>("type"));

            if (allHistoryTournements == null) {
                allHistoryTournements = getNonPendingTournements();
            }
            HistoryTournement.getItems().addAll(allHistoryTournements);

            allCreatedTournements = null;

            createdTournement.getItems().clear();

            createdTournementName.setCellValueFactory(new PropertyValueFactory<Tournement, String>("Name"));
            createdTournementFee.setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("fee"));
            createdTournementType.setCellValueFactory(new PropertyValueFactory<Tournement, String>("type"));
            createdTournementAmount.setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("anzahl"));
            createdTournementMaxAmount .setCellValueFactory(new PropertyValueFactory<Tournement, Integer>("maxAnzahl"));

            if (allCreatedTournements == null) {
                allCreatedTournements = getAllCreatedTournements();
            }
            createdTournement.getItems().addAll(allCreatedTournements);
        });
        t.start();
    }

    private List<Tournement> getAllCreatedTournements() {
        return ClientConnection.getAllCreatedTournements();
    }

    private List<Tournement> getNonPendingTournements() {
        return ClientConnection.getNonPendingTournements();
    }

    private List<Tournement> getAllTournements() {
        return ClientConnection.getAllTournements();
    }

    private List<Tournement> getAllPendingTournements() {
        return ClientConnection.getAllPendingTournements();
    }
}

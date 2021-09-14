package Client.Controllers;

import Client.Models.Assets;
import Client.Main;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HauptmenueController implements Initializable {


    @FXML
    public Text KapazitaetLevelTXT;

    @FXML
    public Text ParkplatzLevelTXT;

    @FXML
    public Text RestaurantLevelTXT;

    @FXML
    public Text plaetzeTXT;

    @FXML
    private VBox vbox;

    @FXML
    private Text sepsTXT;

    @FXML
    private Text StadtionNameTXT;

    public static IntegerProperty seps = new SimpleIntegerProperty();
    public static IntegerProperty stadtionLevel = new SimpleIntegerProperty();
    public static IntegerProperty imbissLevel = new SimpleIntegerProperty();
    public static IntegerProperty parkplatzLevel = new SimpleIntegerProperty();
    public static StringProperty stadionName = new SimpleStringProperty();
    public static IntegerProperty plaetze = new SimpleIntegerProperty();

    private void loader(ActionEvent event, String s) {
        try {
            vbox.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource(s + ".fxml"));
            vbox.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToprofilbearbeiten(ActionEvent event) throws IOException {
        loader(event, "ProfilbearbeitungsFenster");
    }

    @FXML
    private void switchToStatistik(ActionEvent event) throws IOException {
        loader(event, "StatiskFenster");
    }

    @FXML
    private void switchToSpieler(ActionEvent event) throws IOException {
        loader(event, "SpielerDatenbankFenster");
    }

    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        ClientConnection.logOut();
        Main.setRoot("LoginFenster");
    }

    @FXML
    private void exit() {
        ClientConnection.logOut();
        Platform.exit();
    }

    @FXML
    void switchToTeam(ActionEvent event) throws IOException {
        loader(event, "TeamViewFenster");

    }

    @FXML
    void switchToLootboxen(ActionEvent event) throws IOException {
        loader(event, "LootboxenOeffnenFenster");

    }

    @FXML
    void switchToFreunde(ActionEvent event) throws IOException {
        loader(event, "FriendsFenster");
    }

    @FXML
    void switchToNachrichten(ActionEvent event) throws IOException {
        loader(event, "Messages");
    }

    @FXML
    void switchToMatch(ActionEvent event) throws IOException {
        loader(event, "MatchFenster");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FreundeProfilController.friendnameproperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                FreundeProfilController.friend = t1.toString();
            }
        });

        TextController.friendnameproperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                TextController.friend = t1.toString();
            }
        });


            seps.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sepsTXT.setText("SEPS: " + t1.toString());
            }
        });

        stadtionLevel.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                KapazitaetLevelTXT.setText("Stadion Level: " + t1.toString());
            }
        });

        parkplatzLevel.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ParkplatzLevelTXT.setText("Parkplatz Level: " + t1.toString());
            }
        });

        imbissLevel.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                RestaurantLevelTXT.setText("Imbiss Level: " + t1.toString());
            }
        });

        stadionName.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                StadtionNameTXT.setText("Stadion Name: " + t1);
            }
        });

        plaetze.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                plaetzeTXT.setText("Plaetze: " + t1.toString());
            }
        });


        seps.set(ClientConnection.getSEPS());
        sepsTXT.setText("SEPS: " + seps.getValue());

        Assets assets = ClientConnection.getAssets();

        stadtionLevel.set(assets.getKapazitaet());
        KapazitaetLevelTXT.setText("Stadion Level: " + stadtionLevel.getValue());

        parkplatzLevel.set(assets.getParkplatz());
        ParkplatzLevelTXT.setText("Parkplatz Level: " + parkplatzLevel.getValue());

        imbissLevel.set(assets.getImbiss());
        RestaurantLevelTXT.setText("Imbiss Level: " + imbissLevel.getValue());

        stadionName.set(assets.getName());
        StadtionNameTXT.setText("Stadion Name: " + stadionName.getValue());

        plaetze.set(stadtionLevel.getValue()*5000+5000);
        plaetzeTXT.setText("Plaetze: " + plaetze.getValue());

    }

    public void switchToTurnier(ActionEvent event) {

        loader(event, "TournementFenster");
    }


}

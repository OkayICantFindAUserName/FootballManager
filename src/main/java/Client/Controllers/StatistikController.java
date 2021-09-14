package Client.Controllers;

import Client.Models.MatchWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
public class StatistikController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAchse;

    @FXML
    private NumberAxis yAchse;

    @FXML
    private Text tore;

    @FXML
    private Text gegenTore;

    @FXML
    private Text siege;

    @FXML
    private Text niederlagen;

    @FXML
    private Text Spiele;

    @FXML
    private Text winrate;

    @FXML
    private Text unentschieden;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<MatchWrapper> matchHistory = ClientConnection.getMatchHistory();
        int siege = 0;
        int unentschieden = 0;
        int niederlagen = 0;
        int Spiele = 0;

        int tore = 0;
        int gegenTore = 0;
        double winrate = 0.0;
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.CEILING);

        try{

        for (MatchWrapper m:matchHistory) {
            String[] split = m.getGoals().split(" : ");
            int i1 = Integer.parseInt(split[1]);
            int i2 = Integer.parseInt(split[2]);
            switch (split[0]){
                case "Unentschieden":
                    tore += i1;
                    gegenTore += i2;
                    unentschieden++;
                    break;
                case "Verloren":
                    if(i1 > i2){
                        tore += i2;
                        gegenTore += i1;
                    }else{
                        tore += i1;
                        gegenTore += i2;
                    }
                    niederlagen++;
                    break;
                case "Gewonnen":
                    if(i1 > i2){
                        tore += i1;
                        gegenTore += i2;
                    }else{
                        tore += i2;
                        gegenTore += i1;
                    }
                    siege++;
                    break;
            }
        }
            Spiele = siege+niederlagen+unentschieden;
            winrate = (double)siege/Spiele;
            winrate = winrate*100;
        }
        catch (Exception e){
            alert.setTitle("Fehler aufgetreten");
            alert.setContentText("Die Statistik konnte nicht berechnet werden");
            alert.show();
        }





        this.tore.setText("Tore: " + String.valueOf(tore));
        this.gegenTore.setText("Gegentore: " + String.valueOf(gegenTore));
        this.siege.setText("Siege: " + String.valueOf(siege));
        this.niederlagen.setText("Niederlagen: " + String.valueOf(niederlagen));
        this.unentschieden.setText("Unentschieden: "+ String.valueOf(unentschieden));
        this.Spiele.setText("Alle Spiele: "+ String.valueOf(Spiele));
        this.winrate.setText("Winrate: "+ String.valueOf(df.format(winrate)) + "%");

        xAchse.setLabel("Kategorie");
        yAchse.setLabel("Anzahl");

        XYChart.Series<String,Number> dataChart = new XYChart.Series<String, Number>();
        dataChart.getData().add(new XYChart.Data<>("Tore",tore));
        dataChart.getData().add(new XYChart.Data<>("Gegentore",gegenTore));
        dataChart.getData().add(new XYChart.Data<>("Siege",siege));
        dataChart.getData().add(new XYChart.Data<>("Niederlagen",niederlagen));

        barChart.getData().add(dataChart);


    }
}

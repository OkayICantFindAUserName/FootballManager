package Client.Controllers;

import Client.Models.HelperClass;
import Client.Models.MatchWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class LigaDetailsController implements Initializable {

    @FXML
    private TableView<HelperClass> Table;

    @FXML
    private TableColumn<HelperClass, String> Name;

    @FXML
    private TableColumn<HelperClass, String> teamName;

    @FXML
    private TableColumn<HelperClass, Integer> Punkte;

    @FXML
    private TableColumn<HelperClass, Integer> Platz;

    @FXML
    private TableColumn<HelperClass, Integer> Tore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<MatchWrapper> matchList = ClientConnection.getMatchFromT(TournementController.selectedDetailTournement.getName());

        ArrayList<HelperClass> helperClasses = matchListToHelperClass(matchList);

        Name.setCellValueFactory(new PropertyValueFactory<HelperClass, String>("Name"));
        teamName.setCellValueFactory(new PropertyValueFactory<HelperClass, String>("teamName"));
        Punkte.setCellValueFactory(new PropertyValueFactory<HelperClass, Integer>("Punkte"));
        Platz.setCellValueFactory(new PropertyValueFactory<HelperClass, Integer>("Platz"));
        Tore.setCellValueFactory(new PropertyValueFactory<HelperClass, Integer>("tore"));

        Table.getItems().addAll(helperClasses);
    }

    private ArrayList<HelperClass> matchListToHelperClass(List<MatchWrapper> matchList) {

        matchList.sort(new Comparator<MatchWrapper>() {
            @Override
            public int compare(MatchWrapper matchWrapper, MatchWrapper t1) {
                return matchWrapper.getName().compareTo(t1.getName());
            }
        });
        String name = "";
        int i = 0;
        for (MatchWrapper m : matchList) {
            if(!m.getName().equals(name) && !name.equals("")){
                break;
            }
            i++;
            name = m.getName();
        }

        ArrayList<HelperClass> helperClasses = new ArrayList<>();

        int punkte = 0;
        int counter = 0;
        int tore = 0;
        for (MatchWrapper m : matchList) {
            if(m.toString().contains("Unentschieden")){
                tore += Integer.parseInt(m.getGoals().split(" : ")[1]);
                punkte += 1;
            }else if(m.toString().contains("Gewonnen")){
                int i1 = Integer.parseInt(m.getGoals().split(" : ")[1]);
                int i2 = Integer.parseInt(m.getGoals().split(" : ")[2]);
                if(i1 > i2){
                    tore += i1;
                }else{
                    tore += i2;
                }
                punkte += 3;
            }else{
                int i1 = Integer.parseInt(m.getGoals().split(" : ")[1]);
                int i2 = Integer.parseInt(m.getGoals().split(" : ")[2]);
                if(i1 < i2){
                    tore += i1;
                }else{
                    tore += i2;
                }
            }
            counter++;
            if(counter == i){
                helperClasses.add(matchWrapperToHelperClass(m,punkte,tore));
                tore = 0;
                punkte = 0;
                counter = 0;
            }
        }

        helperClasses.sort(new Comparator<HelperClass>() {
            @Override
            public int compare(HelperClass helperClass, HelperClass t1) {
                if(Integer.compare(helperClass.getPunkte(),t1.getPunkte()) == 0){
                    return Integer.compare(helperClass.getTore(),t1.getTore()) * (-1);
                }
                return Integer.compare(helperClass.getPunkte(),t1.getPunkte()) * (-1);
            }
        });
        counter = 1;
        for (HelperClass h : helperClasses) {
            h.setPlatz(counter++);
        }
        return helperClasses;
    }

    private HelperClass matchWrapperToHelperClass(MatchWrapper m, int punkte, int tore) {

        HelperClass helperClass = new HelperClass();
        helperClass.setPunkte(punkte);
        helperClass.setName(m.getName());
        helperClass.setTeamName(m.getTeamName());
        helperClass.setTore(tore);
        return helperClass;
    }
}

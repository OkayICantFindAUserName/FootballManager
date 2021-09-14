package Client.Controllers;

import Client.Models.MatchWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class KODetailController implements Initializable {

    @FXML
    private HBox root;

    @FXML
    private VBox VBox5;

    @FXML
    private VBox VBox4;

    @FXML
    private VBox VBox3;

    List<MatchWrapper> matchList;

    private ArrayList<Text> textArrayList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        matchList = ClientConnection.getMatchFromT(TournementController.selectedDetailTournement.getName());
        trim();
        recursive(root);
        System.out.println(matchList.size());

        int diff = textArrayList.size() - matchList.size();
        for (Text text:textArrayList) {
            text.setText("Jau");
        }
        for (int i = matchList.size() - 1; i > -1; i--) {
            textArrayList.get(i + diff).setText(matchList.get(i).toString());
            System.out.println(diff);
        }


        if(matchList.size() < 9){
            root.getChildren().remove(VBox5);
            root.getChildren().remove(VBox4);
            root.getChildren().remove(VBox3);
        }else if(matchList.size() < 17){
            root.getChildren().remove(VBox5);
            root.getChildren().remove(VBox4);
        }else if(matchList.size() < 33){
            root.getChildren().remove(VBox5);
        }
    }

    private void trim() {

        for (Iterator<MatchWrapper> iterator = matchList.iterator(); iterator.hasNext(); ) {
            MatchWrapper mw = iterator.next();
            String[] split = mw.getResult().split(":");
            if (Integer.valueOf(split[1]) == Integer.valueOf(split[3])) {
                iterator.remove();
            }
        }
    }

    private <T extends Pane> void recursive(T t) {

        if (t == null) {
            return;
        }
        for (Node node : t.getChildren()) {
            if (node instanceof Text) {
                textArrayList.add((Text) node);
                return;
            }
            recursive((T) node);
        }
    }
}



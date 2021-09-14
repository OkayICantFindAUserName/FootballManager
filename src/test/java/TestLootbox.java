import Server.Controller.DatabaseController;
import Server.Model.Player;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TestLootbox {

    @Test
    public void testStartLootbox(){
        //UserID auswählen
        int UserID = 1;
        //Methode um Lootbox zu öffnen
        List<Player> playerList = DatabaseController.openStartLootBox(UserID);
        //Richtige Größe
        assert(playerList.size() == 22);
        double allStringth = 0;
        ArrayList<Player> twList = new ArrayList<>();
        ArrayList<Player> dfList = new ArrayList<>();
        ArrayList<Player> mfList = new ArrayList<>();
        ArrayList<Player> fwList = new ArrayList<>();
        for (Player player:playerList) {
            allStringth += player.getGesamtStaerke();
            switch(player.getPos()){
                case "TW":twList.add(player);
                    break;
                case "DF":dfList.add(player);
                    break;
                case "MF":mfList.add(player);
                    break;
                case "FW":fwList.add(player);
                    break;
            }
        }
        //Richtigen Mittelwert der Stärke
        allStringth /= 22;
        assert(allStringth == 50);

        //Richtige Aufteilung der Positionen
        assert(twList.size() == 2);
        assert(dfList.size() == 7);
        assert(mfList.size() == 10);
        assert(fwList.size() == 3);

        //Methode um das Team zu erhalten
        List<Player> team = null;

        for (Player p : playerList) {
            //Richtige Speicherung in die Datenbank
            assert (team.contains(p));
        }

        for (int j = 0; j < playerList.size(); j++) {
            for (int j2 = 0; j2 < playerList.size(); j2++) {
                if (playerList.get(j).equals(playerList.get(j2)) && j != j2){
                    //Keine Duplikate
                    assert(false);
                }
            }
        }

    }

}

import Server.*;
import Server.Controller.DatabaseController;
import Server.Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Hier kann ein Turnier gemockt werden.
 * Am Ende des Test wird die Oberfläche aufgerufen und man kann sich mit testUserX einloggen um die ergebnisse zu sehen.
 * Nach dem schließen der Oberfläche wird das Turnier und der Benutzer gelöscht.
 */
public class TournementTest {
    //änderen
    final int amountOfUsers = 16;
    final boolean knockout = true;
    //Nicht änderen
    ArrayList<User> userArrayList = new ArrayList<>();
    Tournement tournement = new Tournement();


    @BeforeEach
    public void mockUpUser() throws Exception {

        cleanUp();

        List<Player> testPlayers1 = new ArrayList();

        for (int i = 0; i < amountOfUsers; i++) {
            User user = new User();
            user.setUserName("testUser" + i);
            user.setPasswort("testUser" + i);
            user.seteMail("testUser" + i);
            user.setTeamName("testUser" + i);
            boolean b = DatabaseController.saveUser(user);
            User userWithName = DatabaseController.findUserWithName(user.getUserName());
            DatabaseController.openStartLootBox(userWithName.getId());
            DatabaseController.initAssets(userWithName.getId());
            if (b) {
                userArrayList.add(user);
            } else {
                throw new Exception();
            }
        }

        for (User user : userArrayList) {

            user = DatabaseController.findUserWithName(user.getUserName());
            List<Player> player = DatabaseController.getTeam(user.getId());
            ArrayList<Player> FW = new ArrayList();
            ArrayList<Player> MF = new ArrayList();
            ArrayList<Player> DF = new ArrayList();
            ArrayList<Player> TW = new ArrayList();
            Iterator var6 = player.iterator();

            while (var6.hasNext()) {
                Player p = (Player) var6.next();
                String var8 = p.getPos();
                byte var9 = -1;
                switch (var8.hashCode()) {
                    case 2178:
                        if (var8.equals("DF")) {
                            var9 = 2;
                        }
                        break;
                    case 2257:
                        if (var8.equals("FW")) {
                            var9 = 0;
                        }
                        break;
                    case 2457:
                        if (var8.equals("MF")) {
                            var9 = 1;
                        }
                        break;
                    case 2691:
                        if (var8.equals("TW")) {
                            var9 = 3;
                        }
                }

                switch (var9) {
                    case 0:
                        if (FW.size() < 2) {
                            FW.add(p);
                        }
                        break;
                    case 1:
                        if (MF.size() < 4) {
                            MF.add(p);
                        }
                        break;
                    case 2:
                        if (DF.size() < 4) {
                            DF.add(p);
                        }
                        break;
                    case 3:
                        if (TW.size() < 1) {
                            TW.add(p);
                        }
                }

                if (TW.size() == 1 && DF.size() == 4 && MF.size() == 4 && FW.size() == 2) {
                    break;
                }
            }

            testPlayers1.clear();
            testPlayers1.addAll(TW);
            testPlayers1.addAll(DF);
            testPlayers1.addAll(MF);
            testPlayers1.addAll(FW);


            for (Player p:testPlayers1) {
                DatabaseController.addPlayerToTeam(new Team(user.getId(),p.getId()));
            }

            for (Player p:testPlayers1) {
                DatabaseController.addPlayerToStartElf(user.getId(),p.getId());
            }


        }

    }

    @Test
    public void playTournement() {

        tournement.setCreatorID(userArrayList.get(0).getId());
        tournement.setPending(true);
        tournement.setFee(50);
        tournement.setMaxAnzahl(8);
        tournement.setName("testTournement" + userArrayList.get(0).getId());
        tournement.setKnockOut(knockout);

        int i = DatabaseController.initTournement(userArrayList.get(0).getId(),tournement);

        assert (i == 1);

        for (User user : userArrayList) {
            int i1 = DatabaseController.addUserToTournement(user.getId(), tournement.getName());
            if(i1 != 1 || i1 != 2){
                List<Player> startElf = DatabaseController.getStartElf(user.getId());
                System.out.println("User: " + user.getUserName() + " UserID: " + user.getId() + " Startelf: " + startElf);
            }
            assert (i1 == 1 || i1 == 2);
            if (i1 == 2) {
                break;
            }
        }

        DatabaseController.beginTournement(tournement.getName());

        ArrayList<MatchWrapper> tMatches = DatabaseController.getTMatches(tournement.getName());

        assert (tMatches != null);

    }

    @AfterEach
    public void deleteMockedUsers() {
        try{
            Thread thread = new Thread() {
                public void run() {

                    Server_Socket server_socket = new Server_Socket(8000);
                    server_socket.run();

                }
            };
            thread.start();

            Client.Main.main(null);
        }finally {
            cleanUp();
        }
    }

    public void cleanUp(){
        List<User> user2 = DatabaseController.getUser();
        user2.removeIf(user1 -> !user1.getUserName().contains("testUser"));

        for (User user : user2) {
            System.out.println("Deleting: " + user);
            Assets assetFromUser = DatabaseController.getAssetFromUser(user.getId());
            DatabaseController.deleteAsset(assetFromUser);
            List<Team> teamList = DatabaseController.getThisTeam(user.getId());
            for (Team team : teamList) {
                DatabaseController.deleteTeam(team);
            }
            DatabaseController.deleteUser(user);
        }

        List<Tournement> tournements = DatabaseController.getAllTournements();
        tournements.removeIf(user1 -> !user1.getName().contains("testTournement"));

        for (Tournement user : tournements) {
            DatabaseController.deleteTournement(user);
        }

    }

}

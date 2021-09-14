import Server.Controller.DatabaseController;
import Server.Model.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RotWeissEssenCheat {

    ArrayList<User> userArrayList = new ArrayList<>();

    @BeforeEach
    public void mockUpUser() throws Exception {

        cleanUp();

        List<Player> testPlayers1 = new ArrayList();

        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUserName("testUser" + i);
            user.setPasswort("testUser" + i);
            user.seteMail("testUser" + i);
            if(i == 0){
                user.setTeamName("Rot-Weiss Essen");
            }else{
                user.setTeamName("testUser" + i);
            }
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

    @RepeatedTest(10)
    public void test(){


        Match match = new Match(userArrayList.get(0).getId(),userArrayList.get(1).getId());
        Match matchResult = DatabaseController.acceptMatchRequest(match);

        String[] split = matchResult.getResult().split(":");
        int[] splitInt = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            splitInt[i] = Integer.valueOf(split[i]);
        }

        if(splitInt[0] == userArrayList.get(0).getId()){
            assert(splitInt[1] > splitInt[3]);
        }else{
            assert(splitInt[1] < splitInt[3]);
        }

    }

    @AfterEach
    public void okok(){
        cleanUp();
    }

}

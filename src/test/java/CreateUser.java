import Server.Controller.DatabaseController;
import Server.Model.Assets;
import Server.Model.Team;
import Server.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Diese Klasse kann dafür verwendet werden um neuen Benutzer zu erstellen
 * Diese heißen "testX" wobei X für eine Zahl steht.
 * mit deleteAllTestUser werden alle User die test beinhalten gelöscht.
 */
public class CreateUser {

    private final int Amount = 10;
    private int alreadyThere = 0;

    @BeforeEach
    public void setup() {

        List<User> user = DatabaseController.getUser();
        for (User u : user) {

            if (u.getUserName().contains("test")) {

                alreadyThere++;

            }

        }

    }

    @Test
    public void createUser() {

        for (int i = alreadyThere; i < Amount + alreadyThere; i++) {

            User user = new User();
            user.setUserName("test" + i);
            user.setPasswort("test" + i);
            user.seteMail("test" + i);
            user.setTeamName("default" + i);

            DatabaseController.saveUser(user);
            User userWithName = DatabaseController.findUserWithName(user.getUserName());
            DatabaseController.openStartLootBox(userWithName.getId());
            DatabaseController.initAssets(userWithName.getId());

        }

    }

    @Test
    public void deleteAllTestUser() {

        List<User> user = DatabaseController.getUser();
        for (User u : user) {
            if (u.getUserName().contains("test")) {
                DatabaseController.deleteUser(u);
                Assets assetFromUser = DatabaseController.getAssetFromUser(u.getId());
                DatabaseController.deleteAsset(assetFromUser);
                List<Team> thisTeam = DatabaseController.getThisTeam(u.getId());
                for (Team t : thisTeam) {
                    DatabaseController.deleteTeam(t);

                }
            }
        }

    }

}

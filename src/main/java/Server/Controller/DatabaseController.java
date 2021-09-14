package Server.Controller;

import Client.Models.HelperClass;
import Server.*;
import Server.Model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class DatabaseController {

    public static List<Player> getPlayer() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Player> everyPlayer = session.createQuery("FROM Player").list();
            return everyPlayer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static Player getPlayerFromID(int id) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Player> everyPlayer = session.createQuery("FROM Player where id =" + id).list();
            return everyPlayer.get(0);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static List<Player> getPlayerSorted() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Player> everyPlayer = session.createQuery("FROM Player p ORDER BY p.screachResults ASC").list();
            return everyPlayer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updatePlayers(Player newplayer) {

        Player playerToUpdate;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            playerToUpdate = session.get(Player.class, newplayer.getId());
            playerToUpdate.update(newplayer);
            session.update(playerToUpdate);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUser(User newUser, int id) {

        User userToUpdate;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userToUpdate = session.get(User.class, id);
            userToUpdate.update(newUser, false);
            System.out.println(userToUpdate);
            session.update(userToUpdate);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public static boolean Lootboxen(User newUser, int id) {

        User userToUpdate;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userToUpdate = session.get(User.class, id);
            userToUpdate.update(newUser, true);
            System.out.println(userToUpdate);
            session.update(userToUpdate);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public static List<User> getUser() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> everyUser = session.createQuery("FROM User").list();
            return everyUser;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static boolean saveUser(User user) {
        if (user.getTeamName() == null) {
            user.setTeamName("DefaultName");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user.setsEPS(1000);
            session.save(user);
            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addPlayerTooDatabase(Player player) {

        String[] split = player.getPos().split("-");
        player.setPos(split[0]);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public static int sendFriendRequest(int currentUserID, String friendsName) {

        User userToSendTo = findUserWithName(friendsName);
        if (userToSendTo == null) {
            return -4;
        }
        if (userToSendTo.getId() == currentUserID) {
            return -5;
        }
        List<User> userList = checkForNoNPendingFriends(currentUserID);
        List<User> userList1 = checkForPendingFriends(currentUserID);

        if (userList != null) {
            if (userList.contains(userToSendTo)) {
                return -7;
            }
        }
        if (userList1 != null) {
            if (userList1.contains(userToSendTo)) {
                acceptRequest(currentUserID, friendsName);
                return 2;
            }
        }
        return setUserToPending(checkIfUserIsInDatabase(currentUserID), userToSendTo);
    }

    private static int setUserToPending(User checkIfUserIsInDatabase, User userToSendTo) {

        FriendsList friendsList = new FriendsList(checkIfUserIsInDatabase.getId(), userToSendTo.getId(), true);
        List<FriendsList> friendsList2 = getFriendsList();
        for (FriendsList f : friendsList2) {
            if (f.equals(friendsList)) {
                return -1;
            }
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(friendsList);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -2;
        }
    }

    private static List<FriendsList> getFriendsList() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<FriendsList> everyPlayer = session.createQuery("FROM FriendsList ").list();
            return everyPlayer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    private static FriendsList findFriendsList(int idUser, int idFriend) {

        List<FriendsList> friendsList = getFriendsList();
        FriendsList newf = new FriendsList(idUser, idFriend);
        if (friendsList == null) return null;
        for (FriendsList f : friendsList) {
            if (f.equals(newf)) {
                return f;
            }
        }
        return null;
    }

    public static int acceptRequest(int currentUserID, String friendsName) {

        User friend = findUserWithName(friendsName); // Freund in Datenank finden und in VAriable friend speichern --> Zugriff auf freunds user id (friend.id)
        if (friend == null) { // Sollte Freund nicht existieren, weil hat sich  gel�scht, Fehler meldung zur�ckgeben: "Benutzer nicht gefunden"
            return -2;
        }
        FriendsList friendsList1 = findFriendsList(friend.getId(), currentUserID); // Eintrag in der Freundesliste des Freundes suchen und in friendlist1 abspeichern
        FriendsList friendsList = new FriendsList(currentUserID, friend.getId(), false); // Neues Eintrag generien
        if (addFriend(friendsList)) { // Eintrag in DAtenbank abspeichern
            boolean check = updateFriendsList(friendsList1, false); // Eintrag der Freundeslistes des Feundes wird aktualisert /von true auf false, weil Freundschaftsanfrage angenommen
            if (check) {
                return 1; //Erfolreich aktzeptiert
            } else {
                removeFriendList(friendsList); // Eintrag loeschen, weil z.B.: Eintrag exisiterit schon
                return -1; //Fehlermeldung
            }
        } else {
            return -1; // Fehlermeldung, wenn z.B.: Eintrag schon exisiert 
        }
    }

    public static int declineRequest(int currentUserID, String friendsName) {

        User friend = findUserWithName(friendsName);
        if (friend == null) {
            return -2;
        }
        FriendsList friendsList1 = findFriendsList(friend.getId(), currentUserID);
        boolean b = removeFriendList(friendsList1);
        if (b) {
            return 1;
        } else {
            return -1;
        }
    }

    private static boolean addFriend(FriendsList friendsList) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(friendsList);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    private static boolean updateFriendsList(FriendsList friendsList, boolean b) {

        FriendsList friendsListToUpdate;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            friendsListToUpdate = session.get(FriendsList.class, friendsList.getId());
            friendsListToUpdate.update(b);
            session.update(friendsListToUpdate);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> checkForPendingFriends(int currentUserID) {

        List<FriendsList> allFriends = getFriendsList();
        if (allFriends == null) return null;
        allFriends.removeIf(friendsList -> friendsList.getUserFriendId() != currentUserID);
        allFriends.removeIf(friendsList -> !friendsList.isPending());
        return getCurrentUserFromFriendList(allFriends);
    }

    public static List<User> checkForNoNPendingFriends(int currentUserID) {

        List<FriendsList> allFriends = getFriendsList();
        if (allFriends == null) return null;
        allFriends.removeIf(friendsList -> friendsList.getUserFriendId() != currentUserID);
        allFriends.removeIf(friendsList -> friendsList.isPending());
        return getCurrentUserFromFriendList(allFriends);
    }

    public static List<User> checkForRequestsFriends(int currentUserID) {

        List<FriendsList> allFriends = getFriendsList();
        if (allFriends == null) return null;
        allFriends.removeIf(friendsList -> friendsList.getCurrentUserId() != currentUserID);
        allFriends.removeIf(friendsList -> !friendsList.isPending());
        return getFriendUserFromFriendList(allFriends);
    }

    private static List<User> getCurrentUserFromFriendList(List<FriendsList> friendsList) {

        List<User> userList = new ArrayList<>();
        for (FriendsList list : friendsList) {
            userList.add(checkIfUserIsInDatabase(list.getCurrentUserId()));
        }
        return userList;
    }

    private static List<User> getFriendUserFromFriendList(List<FriendsList> friendsList) {

        List<User> userList = new ArrayList<>();
        for (FriendsList list : friendsList) {
            userList.add(checkIfUserIsInDatabase(list.getUserFriendId()));
        }
        return userList;
    }

    private static User checkIfUserIsInDatabase(int userID) {

        List<User> userList = getUser();
        if (userList == null) return null;
        for (User user : userList) {
            if (user.getId() == userID) {
                return user;
            }
        }
        return null;
    }

    public static User findUserWithName(String name) {

        List<User> userList = getUser();
        if (userList == null) return null;
        for (User user : userList) {
            if (user.getUserName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public static int removeFriend(int currentUserID, String friendsName) {

        User userWithName = findUserWithName(friendsName);
        if (userWithName == null) {
            return -2;
        }
        FriendsList friendsList = findFriendsList(currentUserID, userWithName.getId());
        FriendsList friendsList1 = findFriendsList(userWithName.getId(), currentUserID);
        if (friendsList == null || friendsList1 == null) {
            return -1;
        }
        if (removeFriendList(friendsList) && removeFriendList(friendsList1)) {
            return 1;
        }
        return -1;
    }

    private static boolean removeFriendList(FriendsList friendsList) {

        FriendsList friendsListToDelete;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            friendsListToDelete = session.get(FriendsList.class, friendsList.getId());
            session.remove(friendsListToDelete);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static int getSeps(int userID) {

        User user = checkIfUserIsInDatabase(userID);
        if (user == null) return -1;
        return user.getsEPS();
    }

    private static List<Team> getAllTeams() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Team> everyTeam = session.createQuery("FROM Team").list();
            return everyTeam;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;

    }

    public static List<Player> getTeam(int userID) {

        User user = checkIfUserIsInDatabase(userID);
        List<Player> allPlayerInTeam = new ArrayList<>();
        if (user == null) {
            return null;
        } else {
            List<Team> allTeams = getAllTeams();
            if (allTeams == null) return null;
            allTeams.removeIf(team -> team.getUserID() != userID);
            for (Team team : allTeams) {
                Player playerFromTeam = getPlayerFromTeam(team.getPlayerID());
                if (playerFromTeam != null) {
                    allPlayerInTeam.add(playerFromTeam);
                }
            }
            return allPlayerInTeam;
        }
    }

    private static Player getPlayerFromTeam(int playerID) {

        List<Player> playerList = getPlayer();
        if (playerList == null) return null;
        for (Player p : playerList) {
            if (p.getId() == playerID) {
                return p;
            }
        }
        return null;
    }

    public static boolean addPlayerToTeam(Team newTeam) {

        List<Team> allTeams = getAllTeams();
        if (allTeams == null) return false;
        for (Team t : allTeams) {
            if (t.equals(newTeam)) {
                return false;
            }
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            newTeam.setStartElf(false);
            session.save(newTeam);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }


    public static int sellPlayer(int userID, int playerID) {

        User user = checkIfUserIsInDatabase(userID);
        if (user == null) return 0;
        if (getPlayerFromID(playerID) == null) return 0;

        double playerstaerke = getPlayerFromID(playerID).getGesamtStaerke();
        int newSEPS = user.getsEPS() + (int) playerstaerke;
        if (newSEPS <= 0) return 0;
        user.setsEPS(newSEPS);
        Lootboxen(user, userID);
        deletePlayer(playerID);
        return newSEPS;
    }


    public static List<Player> openLootBox(int userID, int AnzahlBoxen) {

        User user = checkIfUserIsInDatabase(userID);
        if (user == null) return null;
        int newSEPS = user.getsEPS() - (AnzahlBoxen * 100);
        if (newSEPS < 0) return null;

        user.setsEPS(newSEPS);
        Lootboxen(user, userID);
        List<Player> playerList = new ArrayList<>();

        for (int i = 0; i < AnzahlBoxen; i++) {
            Player player = getRandomPlayer();
            while (player == null) player = getRandomPlayer();
            playerList.add(player);
            addPlayerToTeam(new Team(userID, player.getId()));
        }
        for (Player p : playerList) {
            Team team = new Team(userID, p.getId());
            addPlayerToTeam(team);
        }
        return playerList;
    }

    public static List<Player> openStartLootBox(int userID) {

        User user = checkIfUserIsInDatabase(userID);
        if (user == null) return null;

        List<Player> playerList = new ArrayList<>();
        List<Player> TWplayerList = new ArrayList<>();
        List<Player> DEFplayerList = new ArrayList<>();
        List<Player> MFplayerList = new ArrayList<>();
        List<Player> FWplayerList = new ArrayList<>();
        List<Player> player = getPlayer();
        if (player == null) return null;

        for (Player p : player) {
            if (p.getPos().contains("DF")) {
                DEFplayerList.add(p);
            } else if (p.getPos().contains("TW")) {
                TWplayerList.add(p);
            } else if (p.getPos().contains("MF")) {
                MFplayerList.add(p);
            } else if (p.getPos().contains("FW")) {
                FWplayerList.add(p);
            }
        }
        for (int TW = 0; TW < 1; TW++) {
            removeDrafted(TWplayerList, playerList);
        }
        for (int MF = 0; MF < 5; MF++) {
            removeDrafted(MFplayerList, playerList);
        }
        for (int DEF = 0; DEF < 3; DEF++) {
            removeDrafted(DEFplayerList, playerList);
        }
        for (int FW = 0; FW < 1; FW++) {
            removeDrafted(FWplayerList, playerList);
        }
        playerList.addAll(TWORandomPlayer(DEFplayerList, FWplayerList));

        while (playerList.size() < 22) playerList.add(getRandomPlayer());
        if (playerList.size() != 22) return null;

        for (Player p : playerList) addPlayerToTeam(new Team(userID, p.getId()));
        return playerList;
    }

    public static void removeDrafted(List<Player> PlayerList1, List<Player> PlayerList2) {

        List<Player> drafted = TWORandomPlayer(PlayerList1);
        PlayerList1.removeAll(drafted);
        PlayerList2.addAll(drafted);
    }

    public static List<Player> TWORandomPlayer(List<Player> playerlist, List<Player> playerlist21) {

        List<Player> playerList2 = new ArrayList<>();
        List<Player> playerList3 = new ArrayList<>();
        Random random = new Random();
        Player player = playerlist.get(random.nextInt(playerlist.size()));
        int strength = (int) (100 - player.getGesamtStaerke());
        while (true) {
            for (int i = 0; i < 100; i++) {
                for (Player p : playerlist21) {
                    if (p.getGesamtStaerke() == strength) {
                        playerList2.add(p);
                    }
                }
                if (!playerList2.isEmpty()) {
                    break;
                }
            }
            if (playerList2.size() != 0) {
                break;
            } else {
                strength = strength + (random.nextInt(3) - 1);
            }
        }
        System.out.println("playerlistsize " + playerList2.size());
        Player player2 = playerList2.get(random.nextInt(playerList2.size()));
        playerList3.add(player);
        playerList3.add(player2);
        return playerList3;
    }

    public static List<Player> TWORandomPlayer(List<Player> playerlist) {

        int j = 100;
        List<Player> playerList2 = new ArrayList<>();
        List<Player> playerList3 = new ArrayList<>();
        Random random = new Random();
        Player player = playerlist.get(random.nextInt(playerlist.size()));

        for (int i = 0; i < 100; i++) {
            int strength = (int) (j - player.getGesamtStaerke());
            for (Player p : playerlist) {
                if ((int) p.getGesamtStaerke() == strength) {
                    playerList2.add(p);
                }
            }
            if (!playerList2.isEmpty()) {
                break;
            } else {
                if (strength > 50) {
                    j--;
                } else {
                    j++;
                }
            }
        }
        Player player2 = playerList2.get(random.nextInt(playerList2.size()));
        playerList3.add(player);
        playerList3.add(player2);
        return playerList3;
    }

    public static Player getRandomPlayer() {

        List<Player> player = getPlayer();
        Random random = new Random();
        if (player == null) return null;
        return player.get(random.nextInt(player.size()));
    }

    public static boolean addPlayerToStartElf(int userID, int playerID) {

        List<Team> allTeams = getAllTeams();
        if (allTeams == null) return false;
        allTeams.removeIf(team -> team.getUserID() != userID);
        for (Team t : allTeams) {
            if (t.getPlayerID() == playerID) {
                return updateTeam(t, true);
            }
        }
        return false;
    }

    public static boolean deletePlayer(int playerID) {

        Player playerToDelete;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            playerToDelete = session.get(Player.class, playerID);
            session.remove(playerToDelete);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removePlayerToStartElf(int userID) {

        List<Team> allTeams = getAllTeams();
        allTeams.removeIf(team -> team.getUserID() != userID);
        for (Team t : allTeams) {
            updateTeam(t, false);
        }
        return false;
    }

    private static boolean updateTeam(Team t, boolean b) {

        Team TeamToUpdate;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TeamToUpdate = session.get(Team.class, t.getId());
            TeamToUpdate.setStartElf(b);
            session.update(TeamToUpdate);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static List<Player> getStartElf(int userID) {

        List<Team> allTeams = getAllTeams();
        if (allTeams == null) return null;
        List<Player> playerList = new ArrayList<>();
        allTeams.removeIf(team -> team.getUserID() != userID);
        for (Team t : allTeams) {
            if (t.getStartElf()) {
                playerList.add(getPlayerFromTeam(t.getPlayerID()));
            }
        }
        return playerList;
    }

    public static String getTeamNamen(int userID) {

        List<User> user = getUser();
        if (user == null) return null;
        for (User u : user) {
            if (u.getId() == userID) {
                String s = u.getTeamName();
                if (s == null) {
                    return "Noch kein Name Vergeben";
                } else {
                    return s;
                }
            }
        }
        return "";
    }

    public static boolean sendMatchRequest(int userID, String friendName) {

        int friendID = findUserWithName(friendName).getId();
        List<Match> allRequestedMatches = getAllRequestedMatches(userID);
        List<Match> allRequestedMatchesFriend = getAllRequestedMatches(friendID);

        for (Match match : allRequestedMatches) {
            if (match.pending && match.receiverUserID == friendID) {
                System.out.println("Already requested");
                return false;
            }
        }
        for (Match match : allRequestedMatchesFriend) {
            if (match.pending && match.receiverUserID == userID) {
                System.out.print("Already requested, Played match: \t");
                acceptMatchRequest(matchfinder(userID, friendName));
                return true;
            }
        }
        List<User> userList = checkForNoNPendingFriends(userID);
        User user = checkIfUserIsInDatabase(friendID);
        if (user == null) {
            System.out.println("FriendUser Not in Database");
            return false;
        }
        if (userList == null) return false;
        if (userList.contains(user)) {
            Match match = new Match(userID, friendID);
            addMatch(match);
            return true;
        }
        System.out.println("Friend not in FriendsList");
        return false;
    }

    private static void addMatch(Match match) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.save(match);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static List<Match> getAllRequestedMatches(int userID) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Match> everyUser = session.createQuery("FROM Match where senderUserID = " + userID).list();
            return everyUser;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static List<Match> getAllReceivedMatches(int userID) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Match> everyUser = session.createQuery("FROM Match where receiverUserID = " + userID).list();
            return everyUser;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static Match matchfinder(int userID, String friendName) {

        User userWithName = findUserWithName(friendName);
        List<Match> allReceivedMatches = getAllReceivedMatches(userID);
        for (Match match : allReceivedMatches) {
            if (match.getSenderUserID() == userWithName.getId()) {
                if (match.pending) {
                    return match;
                }
            }
        }
        return null;
    }

    public static Match acceptMatchRequest(Match match) {
        int userID = match.getSenderUserID();
        int friendID = match.getReceiverUserID();

        User user = getUserFromID(userID);
        User friend = getUserFromID(friendID);
        if (user == null) {
            System.err.println("User: wurde nicht gefunden: In acceptMatchRequest;DatabaseController");
            return null;
        }
        if (friend == null) {
            System.err.println("Friend: wurde nicht gefunden: In acceptMatchRequest;DatabaseController");
            return null;
        }

        List<Player> startElfUser = getStartElf(userID);
        List<Player> startElfFriend = getStartElf(friendID);
        if (startElfUser.size() != 11) {
            System.err.println("User: Startelf hat nicht exakt 11 Spieler: In acceptMatchRequest;DatabaseController");
            return null;
        }
        if (startElfUser.size() != 11) {
            System.err.println("Friend: Startelf hat nicht exakt 11 Spieler: In acceptMatchRequest;DatabaseController");
            return null;
        }

        String lineup = identLineUp(startElfUser);
        String lineupfriend = identLineUp(startElfFriend);
        if (lineup.equals("null")) {
            System.err.println("User: Aufstellung wurde nicht richtig identifiziert: In acceptMatchRequest;DatabaseController");
            return null;
        }
        if (lineupfriend.equals("null")) {
            System.err.println("Friend: Aufstellung wurde nicht richtig identifiziert: In acceptMatchRequest;DatabaseController");
            return null;
        }

        List<Player> team = getTeam(userID);
        List<Player> team1 = getTeam(friendID);

        team.removeIf(player -> startElfUser.contains(player));
        team1.removeIf(player -> startElfFriend.contains(player));

        if (team.size() < 3) {
            System.err.println("User: Weniger als 3 Spieler auf der Bank: In acceptMatchRequest;DatabaseController");
            return null;
        }
        if (team1.size() < 3) {
            System.err.println("User: Weniger als 3 Spieler auf der Bank: In acceptMatchRequest;DatabaseController");
            return null;
        }

        Random random = new Random();
        int amount = random.nextInt(2);
        if (amount == 0) {
            if (!heimSpiel(userID)) {
                System.out.println("User hat keine Assets");
                return null;
            }
        } else {
            if (!heimSpiel(friendID)) {
                System.out.println("Friend hat keine Assets");
                return null;
            }
        }

        ArrayList<Integer> gameList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            gameList.add(i);
        }

        int helper = 0;
        int resultUser = 0;
        int resultFriend = 0;
        System.out.println();

        amount = random.nextInt(6);
        for (int i = 0; i < amount; i++) {
            int gameID = random.nextInt(gameList.size());

            switch (gameID) {
                case 0:
                    helper = getLineUpBonus(lineup, lineupfriend);
                    System.out.print("Match: getLineUpBonus\t Winner:");
                    if (helper > 0) {
                        System.out.println(user.getUserName());
                        resultUser++;
                    } else if (helper < 0) {
                        System.out.println(friend.getUserName());
                        resultFriend++;
                    } else {
                        System.out.println("Unentschieden");
                        resultFriend++;
                        resultUser++;
                    }
                    System.out.println(user.getUserName() + ": " + resultUser + " - " + friend.getUserName() + ": " + resultFriend);
                    break;
                case 1:
                    helper = getDurchschnittStärkeTeam(startElfUser, startElfFriend, lineup, lineupfriend);
                    System.out.print("Match: getDurchschnittStärkeTeam\t Winner:");
                    if (helper > 0) {
                        System.out.println(user.getUserName());
                        resultUser++;
                    } else if (helper < 0) {
                        System.out.println(friend.getUserName());
                        resultFriend++;
                    } else {
                        System.out.println("Unentschieden");
                        resultFriend++;
                        resultUser++;
                    }
                    System.out.println(user.getUserName() + ": " + resultUser + " - " + friend.getUserName() + ": " + resultFriend);
                    break;
                case 2:
                    helper = getMFDurchschnittStärke(startElfUser, startElfFriend, lineup, lineupfriend);
                    System.out.print("Match: getMFDurchschnittStärke\t Winner:");
                    if (helper > 0) {
                        System.out.println(user.getUserName());
                        resultUser++;
                    } else if (helper < 0) {
                        System.out.println(friend.getUserName());
                        resultFriend++;
                    } else {
                        System.out.println("Unentschieden");
                        resultFriend++;
                        resultUser++;
                    }
                    System.out.println(user.getUserName() + ": " + resultUser + " - " + friend.getUserName() + ": " + resultFriend);
                    break;
                case 3:
                    helper = getChipsaStrength(team, team1);
                    System.out.print("Match: getChipsaStrength\t Winner:");
                    if (helper > 0) {
                        System.out.println(user.getUserName());
                        resultUser++;
                    } else if (helper < 0) {
                        System.out.println(friend.getUserName());
                        resultFriend++;
                    } else {
                        System.out.println("Unentschieden");
                        resultFriend++;
                        resultUser++;
                    }
                    System.out.println(user.getUserName() + ": " + resultUser + " - " + friend.getUserName() + ": " + resultFriend);
                    break;
                case 4:
                    helper = getPairStrength(startElfUser, startElfFriend);
                    System.out.print("Match: getPairStrength\t Winner:");
                    if (helper > 0) {
                        System.out.println(user.getUserName());
                        resultUser++;
                    } else if (helper < 0) {
                        System.out.println(friend.getUserName());
                        resultFriend++;
                    } else {
                        System.out.println("Unentschieden");
                        resultFriend++;
                        resultUser++;
                    }
                    System.out.println(user.getUserName() + ": " + resultUser + " - " + friend.getUserName() + ": " + resultFriend);
                    break;
                case 5:
                    helper = getRandomLottery();
                    System.out.print("Match: getRandomLottery\t Winner:");
                    if (helper > 0) {
                        System.out.println(user.getUserName());
                        resultUser++;
                    } else if (helper < 0) {
                        System.out.println(friend.getUserName());
                        resultFriend++;
                    } else {
                        System.out.println("Unentschieden");
                        resultFriend++;
                        resultUser++;
                    }
                    System.out.println(user.getUserName() + ": " + resultUser + " - " + friend.getUserName() + ": " + resultFriend);
                    break;
            }

            gameList.removeIf(integer -> integer == gameID);

        }
        System.out.println();
        if (user.getTeamName().equals("Rot-Weiss Essen") && (resultUser < resultFriend || resultUser == resultFriend)) {
            resultUser = resultFriend + random.nextInt(3) + 1;
        } else if (friend.getTeamName().equals("Rot-Weiss Essen") && (resultUser > resultFriend || resultUser == resultFriend)) {
            resultFriend = resultUser + random.nextInt(3) + 1;
        }
        if (resultUser > resultFriend) {
            System.out.println(getUserFromID(userID).getTeamName() + " gewinnt:\t" + resultUser + " - " + resultFriend);
            addSEP(userID, 100);
        } else if (resultUser < resultFriend) {
            System.out.println(getUserFromID(friendID).getTeamName() + " gewinnt:\t" + resultUser + " - " + resultFriend);
            addSEP(friendID, 100);
        } else {
            System.out.println("Unentschieden: \t" + resultUser + " - " + resultFriend);
        }
        match.setResult(userID + ":" + resultUser + ":" + friendID + ":" + resultFriend);
        match.setPending(false);
        addMatchToDatabase(match);
        return match;
    }

    private static int addMatchToDatabase(Match match) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.saveOrUpdate(match);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public static Assets getAssetFromUser(int userID) {

        List<Assets> assets = getAssets();
        Assets a = null;

        for (Assets a1 : assets) {
            if (a1.getUserid() == userID) {
                a = a1;
                return a;
            }
        }
        return null;
    }

    public static boolean heimSpiel(int id) {

        int reward = 0;
        Assets a = getAssetFromUser(id);

        if (a == null) {
            return false;
        }
        switch (a.getImbiss()) {
            case 0:
                reward += 5;
                break;
            case 1:
                reward += 10;
                break;
            case 2:
                reward += 15;
                break;
            case 3:
                reward += 20;
                break;
        }
        switch (a.getKapazitaet()) {
            case 0:
                reward += 5;
                break;
            case 1:
                reward += 10;
                break;
            case 2:
                reward += 15;
                break;
            case 3:
                reward += 20;
                break;
        }
        switch (a.getParkplatz()) {
            case 0:
                reward += 5;
                break;
            case 1:
                reward += 10;
                break;
            case 2:
                reward += 15;
                break;
            case 3:
                reward += 20;
                break;
        }
        addSEP(id, reward);
        return true;
    }

    public static void addSEP(int id, int amount) {

        List<User> user = getUser();
        if (user == null) return;
        for (User u : user) {
            if (u.getId() == id) {
                u.setsEPS(u.getsEPS() + amount);
                Lootboxen(u, id);
            }
        }
    }

    public static int getRandomLottery() {

        Random random = new Random();

        int i = random.nextInt(2);
        int i2 = random.nextInt(2);

        if (i > i2) {
            return 1;
        } else if (i < i2) {
            return -1;
        }
        return 0;
    }

    public static int getPairStrength(List<Player> startElfUser1, List<Player> startElfFriend1) {

        ArrayList<Player> startElfUser = new ArrayList<>();
        ArrayList<Player> startElfFriend = new ArrayList<>();

        startElfUser.addAll(startElfUser1);
        startElfFriend.addAll(startElfFriend1);

        startElfUser.removeIf(player -> player.getPos() == "TW");
        startElfFriend.removeIf(player -> player.getPos() == "TW");
        int result = 0;
        Random random = new Random();
        for (int i = 0; i < startElfUser.size(); i++) {
            int randomPlayer = random.nextInt(startElfUser.size());
            int randomPlayer1 = random.nextInt(startElfFriend.size());

            if (startElfUser.get(randomPlayer).getGesamtStaerke() > startElfFriend.get(randomPlayer).getGesamtStaerke()) {
                result++;
            } else if (startElfUser.get(randomPlayer1).getGesamtStaerke() < startElfFriend.get(randomPlayer1).getGesamtStaerke()) {
                result--;
            }

            startElfUser.remove(randomPlayer);
            startElfFriend.remove(randomPlayer1);
        }

        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        }
        return 0;

    }

    public static int getChipsaStrength(List<Player> team, List<Player> team1) {

        Random random = new Random();

        List<Player> teamBench = new ArrayList<>();
        List<Player> friendsteamBench = new ArrayList<>();

        while (teamBench.size() < 3 || friendsteamBench.size() < 3) {
            Player p = team.get(random.nextInt(team.size()));
            if (!teamBench.contains(p) && teamBench.size() < 3) {
                teamBench.add(p);
            }

            Player p1 = team1.get(random.nextInt(team1.size()));
            if (!friendsteamBench.contains(p1) && friendsteamBench.size() < 3) {
                friendsteamBench.add(p1);
            }
        }

        int strength = 0;

        for (Player player : teamBench) {
            strength += player.getGesamtStaerke();
        }

        int strength1 = 0;

        for (Player player : friendsteamBench) {
            strength1 += player.getGesamtStaerke();
        }

        if (strength > strength1) {
            return 1;
        } else if (strength < strength1) {
            return -1;
        }
        return 0;
    }

    public static int getMFDurchschnittStärke(List<Player> startElfUser, List<Player> startElfFriend, String lineup, String lineupfriend) {

        int mf = 0;

        for (Player p : startElfUser) {
            switch (p.getPos()) {
                case "MF":
                    mf += p.getGesamtStaerke();
                    break;
            }
        }
        String[] lineups = lineup.split("-");

        double dMFStrength = mf / Integer.parseInt(lineups[1]);


        int mfFriend = 0;

        for (Player p : startElfFriend) {
            switch (p.getPos()) {
                case "MF":
                    mfFriend += p.getGesamtStaerke();
                    break;
            }
        }

        lineups = lineupfriend.split("-");

        double dMFStrengthFriend = mfFriend / Integer.parseInt(lineups[1]);

        if (dMFStrength > dMFStrengthFriend) {
            return 1;
        } else if (dMFStrength < dMFStrengthFriend) {
            return -1;
        } else {
            return 0;
        }

    }

    public static int getDurchschnittStärkeTeam(List<Player> startElfUser, List<Player> startElfFriend, String lineup, String lineupfriend) {

        int df = 0;
        int fw = 0;
        int tw = 0;

        for (Player p : startElfUser) {
            switch (p.getPos()) {
                case "TW":
                    tw = (int) p.getGesamtStaerke();
                    break;
                case "DF":
                    df += p.getGesamtStaerke();
                    break;
                case "FW":
                    fw += p.getGesamtStaerke();
                    break;
            }
        }

        int dfFriend = 0;
        int fwFriend = 0;
        int twFriend = 0;

        for (Player p : startElfFriend) {
            switch (p.getPos()) {
                case "TW":
                    twFriend = (int) p.getGesamtStaerke();
                    break;
                case "DF":
                    dfFriend += p.getGesamtStaerke();
                    break;
                case "FW":
                    fwFriend += p.getGesamtStaerke();
                    break;
            }
        }


        String[] lineups = lineup.split("-");

        double dFWStrength = fw / Integer.parseInt(lineups[2]);
        double dDFStrength = df / Integer.parseInt(lineups[0]);

        String[] lineups1 = lineupfriend.split("-");

        double dFWStrength1 = fwFriend / Integer.parseInt(lineups1[2]);
        double dDFStrength1 = dfFriend / Integer.parseInt(lineups1[0]);


        double firstTeamStrength = dFWStrength / (dDFStrength1 + twFriend);
        double secoundTeamStrength = dFWStrength1 / (dDFStrength + tw);


        if (firstTeamStrength > secoundTeamStrength) {
            return 1;
        } else if (firstTeamStrength < secoundTeamStrength) {
            return -1;
        } else {
            return 0;
        }

    }

    public static int getLineUpBonus(String lineup, String lineupfriend) {

        switch (lineup) {
            case "4-4-2":
                switch (lineupfriend) {
                    case "4-4-2":
                        return 0;
                    case "5-4-1":
                        return 1;
                    case "3-4-3":
                        return 1;
                    case "4-3-3":
                        return -1;
                    case "3-5-2":
                        return -1;
                }

            case "5-4-1":
                switch (lineupfriend) {
                    case "4-4-2":
                        return -1;
                    case "5-4-1":
                        return 0;
                    case "3-4-3":
                        return -1;
                    case "4-3-3":
                        return 1;
                    case "3-5-2":
                        return 1;
                }

            case "3-4-3":
                switch (lineupfriend) {
                    case "4-4-2":
                        return -1;
                    case "5-4-1":
                        return 1;
                    case "3-4-3":
                        return 0;
                    case "4-3-3":
                        return -1;
                    case "3-5-2":
                        return 1;
                }

            case "4-3-3":
                switch (lineupfriend) {
                    case "4-4-2":
                        return 1;
                    case "5-4-1":
                        return -1;
                    case "3-4-3":
                        return 1;
                    case "4-3-3":
                        return 0;
                    case "3-5-2":
                        return -1;
                }

            case "3-5-2":
                switch (lineupfriend) {
                    case "4-4-2":
                        return 1;
                    case "5-4-1":
                        return -1;
                    case "3-4-3":
                        return -1;
                    case "4-3-3":
                        return 1;
                    case "3-5-2":
                        return 0;
                }

        }
        return 0;
    }

    private static String identLineUp(List<Player> team) {

        int df = 0;
        int mf = 0;
        int fw = 0;

        for (Player p : team) {
            switch (p.getPos()) {
                case "DF":
                    df++;
                    break;
                case "MF":
                    mf++;
                    break;
                case "FW":
                    fw++;
                    break;
            }
        }

        return df + "-" + mf + "-" + fw;
    }

    public static int initAssets(int userID) {

        Assets assetToSave = new Assets(userID, "default");
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(assetToSave);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public static List<Assets> getAssets() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Assets> everyAsset = session.createQuery("FROM Assets").list();
            return everyAsset;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public static int upgradeAssets(int userID, int asset) {

        Assets assetToUpdate = getAssetFromUser(userID);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            assetToUpdate = session.get(Assets.class, assetToUpdate.getId());
            if (!assetToUpdate.upgrade(asset)) {
                return -2;
            }
            session.update(assetToUpdate);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public static int initTournementLiga(int userID, String name, boolean type, int fee, int maxAnzahl) {

        User userFromID = getUserFromID(userID);
        if (userFromID.getsEPS() < fee) {
            return -3;
        }
        List<Tournement> allTournements = getAllTournements();
        Tournement TournementToSave = new Tournement(userID, name, type, fee, maxAnzahl);
        TournementToSave.addContestant(userID);
        TournementToSave.setPending(true);
        for (Tournement t : allTournements) {
            if (t.getName().toLowerCase().equals(name.toLowerCase())) {
                return -2;
            }
        }
        addSEP(userID, -fee);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(TournementToSave);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public static int initTournement(int userID, Tournement tournement) {

        User userFromID = getUserFromID(userID);
        if (userFromID.getTeamName() == null) {
            System.out.println("Kein Team Name");
            return -1;
        }
        List<Team> allTeams = getAllTeams();
        allTeams.removeIf(Team -> Team.getUserID() != userID);
        allTeams.removeIf(Team -> !Team.getStartElf());
        if (allTeams.size() != 11) {
            System.out.println("Nicht genug Spieler");
            return -4;
        }
        if (tournement.getFee() > userFromID.getsEPS()) {
            System.out.println("nicht genug seps");
            return -5;
        }

        List<Tournement> allTournements = getAllTournements();
        for (Tournement t : allTournements) {
            if (t.getName().equals(tournement.getName())) {
                return -2;
            }
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(tournement);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public static List<Tournement> getAllTournements() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Tournement> everyTournementLiga = session.createQuery("FROM Tournement").list();
            return everyTournementLiga;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public static int addUserToTournement(int userID, String tournementName) {

        User userFromID = getUserFromID(userID);
        if (userFromID.getTeamName() == null) {
            System.out.println("Kein Team Name");
            return -1;
        }
        List<Team> allTeams = getAllTeams();
        allTeams.removeIf(Team -> Team.getUserID() != userID);
        allTeams.removeIf(Team -> !Team.getStartElf());
        if (allTeams.size() != 11) {
            System.out.println("Nicht genug Spieler");
            return -4;
        }
        Tournement tournement = getTournementByName(tournementName);
        if (tournement.getFee() > userFromID.getsEPS()) {
            System.out.println("nicht genug seps");
            return -5;
        }
        if (!tournement.isPending()) {
            System.out.println("Das tunier wurde bereits gespielt");
            return -3;
        }
        List<Integer> contestantList = tournement.getContestantID();
        for (int i : contestantList) {
            if (i == userID) {
                return -6;
            }
        }

        addSEP(userID, -tournement.getFee());

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tournement = session.get(Tournement.class, tournement.getId());
            tournement.getContestantID().add(userID);
            session.update(tournement);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            if (tournement.getContestantID().size() == tournement.getMaxAnzahl()) {
                return 2;
            }
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public static User getUserFromID(int userID) {

        List<User> user = getUser();
        if (user == null) return null;
        for (User u : user) {
            if (u.getId() == userID) {
                return u;
            }
        }
        return null;
    }

    public static User getUserFromUsername(String username) {

        List<User> user = getUser();
        for (User u : user) {
            if (u.getUserName().equals(username)) {
                System.out.println("Gefunden");
                return u;
            }
        }
        System.out.println("nicht gefunden");
        return null;
    }


    public static Tournement getTournementByName(String name) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Tournement> everyTournementLiga = session.createQuery("FROM Tournement").list();
            for (Tournement t : everyTournementLiga) {
                if (t.getName().toLowerCase().equals(name.toLowerCase())) {
                    return t;
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static List<Match> getMatchHistory(int userID) {

        List<Match> allMatches = getAllMatches();
        List<Match> allMatchesWithUser = new ArrayList<>();
        System.out.println(userID);
        for (Match m : allMatches) {
            if (!m.pending && (m.senderUserID == userID || m.receiverUserID == userID)) {
                allMatchesWithUser.add(m);
            }
        }
        return allMatchesWithUser;
    }

    private static List<Match> getAllMatches() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Match> everyUser = session.createQuery("FROM Match").list();
            return everyUser;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static int declineMatchRequest(int userID, String friendName) {
        User userWithName = findUserWithName(friendName);
        List<Match> allReceivedMatches = getAllReceivedMatches(userID);
        for (Match m : allReceivedMatches) {
            if (m.getSenderUserID() == userWithName.getId()) {
                return removeMatch(m);
            }
        }
        return -1;
    }

    private static int removeMatch(Match m) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            m = session.get(Match.class, m.getId());
            session.remove(m);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public static MatchWrapper matchWrapperFromMatch(Match match, int UserID) {

        MatchWrapper matchWrapper = new MatchWrapper();
        int[] resultFromMatch = getResultFromMatch(match);

        if (resultFromMatch[0] == UserID) {

            User userFromID = getUserFromID(resultFromMatch[2]);

            matchWrapper.setName(userFromID.getUserName());
            matchWrapper.setTeamName(userFromID.getTeamName());
            matchWrapper.setResult(match.getResult());
            matchWrapper.setWin(resultFromMatch[1] > resultFromMatch[3]);
        } else {
            User userFromID = getUserFromID(resultFromMatch[0]);

            matchWrapper.setName(userFromID.getUserName());
            matchWrapper.setTeamName(userFromID.getTeamName());
            matchWrapper.setResult(match.getResult());
            matchWrapper.setWin(resultFromMatch[3] > resultFromMatch[1]);
        }
        return matchWrapper;
    }

    private static int[] getResultFromMatch(Match match) {

        String[] split = match.getResult().split(":");
        int[] splitInt = new int[split.length];

        for (int i = 0; i < split.length; i++) {
            splitInt[i] = Integer.parseInt(split[i]);
        }
        return splitInt;
    }

    public static void beginTournement(String tName) {

        List<Tournement> allTournements = getAllTournements();

        for (Tournement t : allTournements) {
            if (t.getName().equals(tName)) {

                if (t.getType().equals("Knock-Out")) {
                    ArrayList<Integer> playerlist = new ArrayList<>();
                    playerlist.addAll(t.getContestantID());
                    while (playerlist.size() > 1) {
                        HashMap<Integer, Integer> matchMap = setupMatchesKnockOut(playerlist);
                        playerlist = new ArrayList<>();
                        for (Map.Entry<Integer, Integer> entry : matchMap.entrySet()) {
                            int winnerID = playKnockOutMatches(entry, t);
                            playerlist.add(winnerID);
                        }
                    }
                } else {
                    ArrayList<Integer> playerlist = new ArrayList<>();
                    playerlist.addAll(t.getContestantID());

                    playLigaMatches(playerlist, t);
                }

                t.setPending(false);
                updateTournement(t);
                addSepToWinner(t);
            }
        }

    }

    public static void addSepToWinner(Tournement t) {

        if (t == null || t.getContestantID().isEmpty() || t.getMatchIDs().isEmpty()) {
            return;
        }

        if (t.getType() == null || !t.getType().equals("Knock-Out") && !t.getType().equals("Liga")) {
            return;
        }

        int gewinn = t.getContestantID().size() * t.getFee();
        gewinn = gewinn / 6;
        if (t.getType().equals("Knock-Out")) {

            ArrayList<MatchWrapper> tMatches = getTMatches(t.getName());
            ArrayList<MatchWrapper> trim = trim(tMatches);
            int i = getLoserFromMatch(trim.get(trim.size() - 3).getResult());
            int i2 = getLoserFromMatch(trim.get(trim.size() - 5).getResult());
            int[] winnerFromResult1;
            Match match = new Match(i, i2);
            Match match1;
            do {
                match1 = acceptMatchRequest(match);
                winnerFromResult1 = getWinnerFromResult(match1.getResult());
            } while (winnerFromResult1 == null);

            t.addMatch(match1.getId());
            updateTournement(t);
            addSEP(winnerFromResult1[0], gewinn);
            System.out.println(winnerFromResult1[0] + " bekommt: " + gewinn);

            String result = trim.get(trim.size() - 1).getResult();
            int[] winnerFromResult = getWinnerFromResult(result);

            addSEP(winnerFromResult[0], gewinn * 3);
            System.out.println(winnerFromResult[0] + " bekommt: " + gewinn * 4);
            addSEP(winnerFromResult[1], gewinn * 2);
            System.out.println(winnerFromResult[1] + " bekommt: " + gewinn * 2);
        } else {

            ArrayList<MatchWrapper> tMatches = getTMatches(t.getName());
            ArrayList<HelperClass> helperClasses = matchListToHelperClass(tMatches);
            for (int i = 0; i < 3; i++) {
                addSEP(findUserWithName(helperClasses.get(i).getName()).getId(), gewinn * (3 - i));
                System.out.println(findUserWithName(helperClasses.get(i).getName()) + " bekommt: " + gewinn * (3 - i));

            }
        }
    }

    private static int getLoserFromMatch(String result) {

        String[] split = result.split(":");

        int id1 = Integer.parseInt(split[0]);
        int id2 = Integer.parseInt(split[2]);

        int score1 = Integer.parseInt(split[1]);
        int score2 = Integer.parseInt(split[3]);

        if (score1 > score2) {
            return id2;
        } else if (score2 > score1) {
            return id1;
        } else {
            return 0;
        }
    }

    private static ArrayList<HelperClass> matchListToHelperClass(List<MatchWrapper> matchList) {

        matchList.sort(new Comparator<MatchWrapper>() {
            @Override
            public int compare(MatchWrapper matchWrapper, MatchWrapper t1) {
                return matchWrapper.getName().compareTo(t1.getName());
            }
        });
        String name = "";
        int i = 0;
        for (MatchWrapper m : matchList) {
            if (!m.getName().equals(name) && !name.equals("")) {
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
            if (m.getGoals().split(" : ")[0].equals("Unentschieden")) {
                tore += Integer.parseInt(m.getGoals().split(" : ")[1]);
                punkte += 1;
            } else if (m.getGoals().split(" : ")[0].equals("Gewonnen")) {
                int i1 = Integer.parseInt(m.getGoals().split(" : ")[1]);
                int i2 = Integer.parseInt(m.getGoals().split(" : ")[2]);
                if (i1 > i2) {
                    tore += i1;
                } else {
                    tore += i2;
                }
                punkte += 3;
            } else {
                int i1 = Integer.parseInt(m.getGoals().split(" : ")[1]);
                int i2 = Integer.parseInt(m.getGoals().split(" : ")[2]);
                if (i1 < i2) {
                    tore += i1;
                } else {
                    tore += i2;
                }
            }
            counter++;
            if (counter == i) {
                helperClasses.add(matchWrapperToHelperClass(m, punkte, tore));
                tore = 0;
                punkte = 0;
                counter = 0;
            }
        }

        helperClasses.sort(new Comparator<HelperClass>() {
            @Override
            public int compare(HelperClass helperClass, HelperClass t1) {
                if (Integer.compare(helperClass.getPunkte(), t1.getPunkte()) == 0) {
                    return Integer.compare(helperClass.getTore(), t1.getTore()) * (-1);
                }
                return Integer.compare(helperClass.getPunkte(), t1.getPunkte()) * (-1);
            }
        });
        counter = 1;
        for (HelperClass h : helperClasses) {
            h.setPlatz(counter++);
        }
        return helperClasses;
    }


    private static HelperClass matchWrapperToHelperClass(MatchWrapper m, int punkte, int tore) {

        HelperClass helperClass = new HelperClass();
        helperClass.setPunkte(punkte);
        helperClass.setName(m.getName());
        helperClass.setTeamName(m.getTeamName());
        helperClass.setTore(tore);
        return helperClass;
    }

    private static int[] getWinnerFromResult(String result) {

        String[] split = result.split(":");

        int id1 = Integer.parseInt(split[0]);
        int id2 = Integer.parseInt(split[2]);

        int score1 = Integer.parseInt(split[1]);
        int score2 = Integer.parseInt(split[3]);

        if (score1 > score2) {
            return new int[]{id1, id2};
        } else if (score2 > score1) {
            return new int[]{id2, id1};
        } else {
            return null;
        }
    }

    private static ArrayList<MatchWrapper> trim(ArrayList<MatchWrapper> tMatches) {
        for (Iterator<MatchWrapper> iterator = tMatches.iterator(); iterator.hasNext(); ) {
            MatchWrapper mw = iterator.next();
            String[] split = mw.getResult().split(":");
            if (Integer.valueOf(split[1]) == Integer.valueOf(split[3])) {
                iterator.remove();
            }
        }
        return tMatches;
    }

    private static void playLigaMatches(ArrayList<Integer> playerlist, Tournement t) {

        for (int i = 0; i < playerlist.size(); i++) {
            for (int j = i + 1; j < playerlist.size(); j++) {
                Match match = new Match(playerlist.get(i), playerlist.get(j));
                Match match1 = acceptMatchRequest(match);
                t.addMatch(match1.getId());
                updateTournement(t);
            }
        }
    }

    private static int playKnockOutMatches(Map.Entry<Integer, Integer> matchMap, Tournement t) {

        while (true) {
            Integer key = matchMap.getKey();
            Integer value = matchMap.getValue();

            Match match = new Match(key, value);

            match = acceptMatchRequest(match);
            t.addMatch(match.getId());

            updateTournement(t);

            String[] split = match.getResult().split(":");
            int i1 = Integer.parseInt(split[1]);
            int i2 = Integer.parseInt(split[3]);

            if (i1 > i2) {
                return key;
            } else if (i2 > i1) {
                return value;
            }
        }
    }

    private static void updateTournement(Tournement t) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.update(t);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, Integer> setupMatchesKnockOut(List<Integer> contestantID) {

        List<Integer> contestantIDs = new ArrayList<>();
        contestantIDs.addAll(contestantID);
        HashMap<Integer, Integer> matchMap = new HashMap<>();
        Random random = new Random();

        while (contestantIDs.size() > 0) {

            Integer integer = contestantIDs.get(random.nextInt(contestantIDs.size()));
            contestantIDs.remove(integer);
            Integer integer1 = contestantIDs.get(random.nextInt(contestantIDs.size()));
            contestantIDs.remove(integer1);

            matchMap.put(integer, integer1);
        }
        return matchMap;
    }

    public static ArrayList<MatchWrapper> getTMatches(String tName) {

        List<Tournement> allTournements = getAllTournements();
        ArrayList<Match> matchList = new ArrayList<>();
        List<Match> allMatches = getAllMatches();
        ArrayList<MatchWrapper> matchWrappers = new ArrayList<>();

        for (Tournement t : allTournements) {
            if (t.getName().equals(tName)) {
                for (int i : t.getMatchIDs()) {
                    for (Match m : allMatches) {
                        if (m.getId() == i) {
                            matchList.add(m);
                        }
                    }
                }
                for (Match m : matchList) {
                    matchWrappers.add(matchWrapperFromMatch(m, m.getReceiverUserID()));
                    matchWrappers.add(matchWrapperFromMatch(m, m.getSenderUserID()));
                }
                return matchWrappers;
            }
        }
        return null;

    }

    public static int updateAsset(int userID, String s) {
        Assets assetFromUser = getAssetFromUser(userID);
        assetFromUser.setName(s);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.update(assetFromUser);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean deleteTeam(Team team) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.remove(team);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.remove(user);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static List<Team> getThisTeam(int id) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Team> everyPlayer = session.createQuery("FROM Team where userID ='" + id + "'").list();
            return everyPlayer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;

    }

    public static void deleteTournement(Tournement tournement) {
        tournement.setContestantID(new ArrayList<>());
        tournement.setMatchIDs(new ArrayList<>());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.update(tournement);
            session.delete(tournement);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void deleteAsset(Assets assetFromUser) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.remove(assetFromUser);
            transaction = session.beginTransaction();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static int saveNachricht(int userID, String name, String message) {

        Nachricht nachricht = new Nachricht();
        nachricht.setSenderID(userID);
        nachricht.setSenderName(DatabaseController.getUserFromID(userID).getUserName());
        User user = findUserWithName(name);
        nachricht.setReceiverName(user.getUserName());
        nachricht.setReceiverID(user.getId());
        nachricht.setNachricht(message);
        Date date = new Date(System.currentTimeMillis());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        nachricht.setDate(sqlDate);
        System.out.println(nachricht);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(nachricht);
            transaction.commit();
            session.close();
            return 1;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return -2;
        }

    }

    public static List<Nachricht> getAllNachricht() {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Nachricht> everyPlayer = session.createQuery("FROM Nachricht").list();
            return everyPlayer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;

    }
}

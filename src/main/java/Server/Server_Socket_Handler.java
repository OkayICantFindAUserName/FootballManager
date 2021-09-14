package Server;


import Server.Controller.DatabaseController;
import Server.Model.*;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Server_Socket_Handler extends Thread { //Interface runnable wird benutzt, deshalb muss die run() Methode implementiert werden

    // Es wird ein Socketobjekt erstellt
    private Socket client;
    private boolean go = true;
    private String speicher;
    private int UserID = -1;

    public Server_Socket_Handler(Socket client) {        //Klassenkonstruktur um Objekt zu bauen
        this.client = client;
    }

    @Override
    public void run() {
        while (go) {
            try {


                DataInputStream incoming = new DataInputStream(this.client.getInputStream()); // Hiermit werden Datenstreams empfangen
                //System.out.println(incoming.readUTF()); //Testzwecken ausgabe auf konsole
                this.speicher = incoming.readUTF();

                System.out.println("Erster speicher" + speicher);
                getRequest(speicher);

                //Server_Controller neu = new Server_Controller();
                /*DataOutputStream outgoing = new DataOutputStream(this.client.getOutputStream()); //Hiermit werden Outputstreams gesendet
                outgoing.writeUTF(speicher); //  Hier werden die Daten in den Outputstream geschrieben
                outgoing.flush(); //Stellt sicher dass alle Daten gesendet werden*/
                OutputStream outServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outServer);
                byte[] buff = speicher.getBytes("UTF-8");
                out.writeInt(buff.length);
                out.write(buff);
                out.flush();
                //client.close();

                if (this.client.isClosed()) {
                    incoming.close();  //DataInputStream wird geschlossen
                    //outgoing.close(); //OutPutStream wird geschlossen
                    this.client.close();

                    this.go = false;
                }
            } catch (Exception e) {
            }
        }
    }

    public void getRequest(String request) {

        String[] requestArray = request.split("/");
        switch (requestArray[0]) {
            case "GETSEPS":
                getSeps();
                return;
            case "GETUSER":
                getuser(requestArray[1]);
                return;
            case "POSTUSER":
                postuser(requestArray[1]);
                return;
            case "GETPLAYER":
                getplayer();
                return;
            case "UPDATEUSER":
                updateuser(requestArray[1]);
                return;
            case "SENDFRIENDINVITE":
                sendFriendInvite(requestArray[1]);
                return;
            case "CHECKFORPENDINGFRIENDS":
                checkForPendingFriends();
                return;
            case "CHECKFORNONPENDINGFRIENDS":
                checkForNonPendingFriends();
                return;
            case "GETTEAMNAMEN":
                getTeamNamen();
                return;
            case "GETFRIENDTEAMNAMEN":
                getFriendTeamNamen(requestArray[1]);
                return;
            case "ACCEPTFRIEND":
                accept(requestArray[1]);
                return;
            case "REMOVEFRIEND":
                removeFriends(requestArray[1]);
                return;
            case "DECLINEFRIEND":
                declineFriend(requestArray[1]);
                return;
            case "REQUESTEDFRIENDS":
                checkForRequestedFriends();
                return;
            case "GETTEAM":
                getTeam();
                return;
            case "OPENLOOTBOX":
                openLootBox(requestArray[1]);
                return;
            case "OPENSTARTLOOTBOX":
                openStartLootBox();
                return;
            case "ADDPLAYERTOSTARTELF":
                addPlayerToStartElf(requestArray[1], requestArray[2]);
                return;
            case "GETSTARTELF":
                getStartElf();
                return;
            case "GETFRIENDSTARTELF":
                getFriendStartElf(requestArray[1]);
                return;
            case "GETMATCHREQUEST":
                getMatchRequests();
                return;
            case "GETMATCHHISTORY":
                getMatchHistory();
                return;
            case "GETFRIENDMATCHHISTORY":
                getFriendMatchHistory(requestArray[1]);
                return;
            case "REQUESTMATCH":
                requestmatch(requestArray[1]);
                return;
            case "ACCEPTMATCH":
                acceptMatch(requestArray[1]);
                return;
            case "DECLINEMATCHREQUEST":
                declineMatchRequest(requestArray[1]);
                return;
            case "GETREQUESTEDMATCHES":
                getRequestedMatches();
                return;
            case "GETASSET":
                getAssets();
                return;
            case "GETALLTOURNEMENTS":
                getAllTournements();
                return;
            case "ADDUSERTOTOURNEMENT":
                addUserToTournement(requestArray[1]);
                return;
            case "GETALLPENDINGTOURNEMENTS":
                getAllPendingTournements();
                return;
            case "GETALLNONPENDINGTOURNEMENTS":
                getAllNonPendingTournements();
                return;
            case "GETALLCREATEDTOURNEMENTS":
                getAllCreatedTournements();
                return;
            case "CREATENEWTOURNEMENT":
                createNewTournement(requestArray[1]);
                return;
            case "GETMATCHFROMT":
                getMatchFromT(requestArray[1]);
                return;
            case "UPGRADEASSET":
                upgradeAsset(requestArray[1], requestArray[2]);
                return;
            case "UPDATEASSET":
                updateAsset(requestArray[1]);
                return;
            case "SENDMESSAGE":
                saveMessage(requestArray[1],requestArray[2]);
                return;
            case "GETMESSAGE":
                getMessage();
                return;
            case "GETSENTMESSAGE":
                getSentMessage();
                return;
            case "LOGOUT":
                this.UserID = -1;
                go = false;
                return;
            case "SELLPLAYER":
                sellPlayer(requestArray[1]);
        }
        System.out.println("Request nicht erkannt Server.");
    }

    private void updateAsset(String s) {

        int i = DatabaseController.updateAsset(UserID, s);
        if(i == 1){
            speicher = "Erfolgreich geändert.";
        }else{
            speicher = "Fehler bei der Änderung.";
        }
    }

    private void getMessage() {
        List<Nachricht> messages = new ArrayList<>() ;
        List <Nachricht> list = DatabaseController.getAllNachricht();
        for(Nachricht n : list) {
            if(n.getReceiverID() == UserID){
                n.setName();
                messages.add(n);
            }
        }
        speicher = JSONUtilClass.convertToJSONList(messages);
        System.out.println(speicher);

    }

    private void getSentMessage() {
        List<Nachricht> messages = new ArrayList<>() ;
        List <Nachricht> list = DatabaseController.getAllNachricht();
        for(Nachricht n : list) {
            if(n.getSenderID() == UserID){
                n.setName();
                messages.add(n);
            }
        }
        speicher = JSONUtilClass.convertToJSONList(messages);
        System.out.println(speicher);

    }

    private void saveMessage(String friendName, String message) {

        int code = DatabaseController.saveNachricht(UserID, friendName, message);
        speicher = String.valueOf(code);
    }

    private void upgradeAsset(String s, String cost) {
        
        User userFromID = DatabaseController.getUserFromID(UserID);
        if(userFromID == null) {
            speicher = "Nutzer exestiert nicht";
            return;
        }
        if (userFromID.getsEPS() < Integer.parseInt(cost)) {
            speicher = "Zuwenig Seps";
            return;
        }
        DatabaseController.addSEP(UserID, Integer.parseInt(cost) * (-1));
        int i = DatabaseController.upgradeAssets(UserID, Integer.parseInt(s));
        if (i == 1) {
            speicher = "Erfolgreich verbessert.";
        } else if (i == -2) {
            speicher = "Breits auf Maximalstufe.";
        } else {
            speicher = "Unerwarteter Server Error.";
        }
    }

    private void getMatchFromT(String tName) {

        List<MatchWrapper> hallo = DatabaseController.getTMatches(tName);
        speicher = JSONUtilClass.convertMatchWeapperToJSONList(hallo);
    }

    private void getAllCreatedTournements() {

        List<Tournement> allTournements = DatabaseController.getAllTournements();
        allTournements.removeIf(Tournement -> Tournement.getCreatorID() != UserID);
        speicher = JSONUtilClass.convertTournementToJSONList(allTournements);
    }

    private void getAllNonPendingTournements() {

        List<Tournement> allTournements = DatabaseController.getAllTournements();
        allTournements.removeIf(Tournement -> Tournement.isPending());
        allTournements.removeIf(Tournement -> !Tournement.getContestantID().contains(UserID));
        speicher = JSONUtilClass.convertTournementToJSONList(allTournements);
    }

    private void createNewTournement(String s) {

        Tournement tournement = JSONUtilClass.convertFromJSONTournement(s);
        tournement.setCreatorID(UserID);
        tournement.addContestant(UserID);
        tournement.setPending(true);
        int i = DatabaseController.initTournement(UserID, tournement);
        if(i == 1){
            DatabaseController.addSEP(UserID,-tournement.getFee());
        }
        if(i==-1){
            speicher = "-1";
        }
        if(i==-4){
            speicher = "-4";
        }
        speicher = String.valueOf(i);
    }


    private void getAllPendingTournements() {

        List<Tournement> allTournements = DatabaseController.getAllTournements();
        allTournements.removeIf(Tournement -> !Tournement.isPending());
        allTournements.removeIf(Tournement -> !Tournement.getContestantID().contains(UserID));
        speicher = JSONUtilClass.convertTournementToJSONList(allTournements);
    }

    private void addUserToTournement(String tName) {

        int i = DatabaseController.addUserToTournement(UserID, tName);
        if(i==-1){
            speicher = "-1";
        }
        if(i==-4){
            speicher = "-4";
        }
        if (i == 2) {
            DatabaseController.beginTournement(tName);
        }
        if (i == -5) {
            speicher = "-5";
        }
        if (i == -3) {
            speicher = "-3";
        }
        if (i == -6) {
            speicher = "-6";
        }
        if (i == 1) {
            speicher = "1";
        }
        speicher = String.valueOf(i);
    }

    private void getAllTournements() {

        List<Tournement> allTournements = DatabaseController.getAllTournements();
        allTournements.removeIf(Tournement -> !Tournement.isPending());
        speicher = JSONUtilClass.convertTournementToJSONList(allTournements);
    }

    private void getAssets() {

        Assets assetFromUser = DatabaseController.getAssetFromUser(UserID);
        speicher = JSONUtilClass.convertToJSONAssets(assetFromUser);
    }

    private void getRequestedMatches() {

        List<Match> allRequestedMatches = DatabaseController.getAllRequestedMatches(UserID);
        allRequestedMatches.removeIf(Match -> !Match.isPending());
        ArrayList<User> userList = new ArrayList<>();
        for (Match m : allRequestedMatches) {
            userList.add(DatabaseController.getUserFromID(m.getReceiverUserID()));
        }
        if (allRequestedMatches == null) {
            speicher = "-1";
        } else {
            speicher = JSONUtilClass.convertToJSONList(userList, null);
        }
    }

    private void declineMatchRequest(String friendName) {

        int i = DatabaseController.declineMatchRequest(UserID, friendName);
        if (i == 1) {
            speicher = "Erfolgreich gelöscht";
        } else {
            speicher = "-1";
        }
    }

    private void acceptMatch(String friendName) {

        Match match = DatabaseController.acceptMatchRequest(DatabaseController.matchfinder(UserID, friendName));
        if(match != null){
            speicher = "Match Erfolgreich gespielt.";
        }else{
            speicher = "Es ist ein Fehler aufgetreten.";
        }
    }

    private void requestmatch(String friendName) {

        boolean b = DatabaseController.sendMatchRequest(UserID, friendName);
        if (b) {
            speicher = "1";
        } else {
            speicher = "-1";
        }
    }

    private void getMatchHistory() {

        List<Match> matchHistory = DatabaseController.getMatchHistory(UserID);
        List<MatchWrapper> matchWrapperHistory = new ArrayList<>();
        for (Match m : matchHistory) {
            matchWrapperHistory.add(DatabaseController.matchWrapperFromMatch(m, UserID));
        }

        String s = JSONUtilClass.convertMatchWeapperToJSONList(matchWrapperHistory);
        speicher = s;
    }

    private void getFriendMatchHistory(String friend) {

        User userFromID = DatabaseController.getUserFromUsername(friend);
        List<Match> matchHistory = DatabaseController.getMatchHistory(userFromID.getId());
        List<MatchWrapper> matchWrapperHistory = new ArrayList<>();
        for (Match m : matchHistory) {
            matchWrapperHistory.add(DatabaseController.matchWrapperFromMatch(m, userFromID.getId()));
        }

        String s = JSONUtilClass.convertMatchWeapperToJSONList(matchWrapperHistory);
        speicher = s;
    }

    private void getMatchRequests() {

        List<Match> allReceivedMatches = DatabaseController.getAllReceivedMatches(UserID);
        allReceivedMatches.removeIf(Match -> !Match.isPending());
        ArrayList<User> userList = new ArrayList<>();
        for (Match m : allReceivedMatches) {
            User userFromID = DatabaseController.getUserFromID(m.getSenderUserID());
            userFromID.setsEPS(0);
            userFromID.setId(-1);
            userFromID.setPasswort("");
            userList.add(userFromID);
        }
        if (allReceivedMatches == null) {
            speicher = "-1";
        } else {
            speicher = JSONUtilClass.convertToJSONList(userList, null);
        }
    }

    private void getTeamNamen() {

        String teamNamen = DatabaseController.getTeamNamen(UserID);
        speicher = teamNamen;
    }

    private void getFriendTeamNamen(String friend) {

        User Userfriend = DatabaseController.getUserFromUsername(friend);
        String teamNamen = DatabaseController.getTeamNamen(Userfriend.getId());
        speicher = teamNamen;
    }

    private void getStartElf() {

        List<Player> startElf = DatabaseController.getStartElf(this.UserID);
        if (startElf == null) {
            speicher = "-1";
        } else {
            speicher = JSONUtilClass.convertToJSONList(null, startElf);
        }
    }

    private void getFriendStartElf(String friend) {

        User frienduser = DatabaseController.getUserFromUsername(friend);
        List<Player> startElf = DatabaseController.getStartElf(frienduser.getId());
        if (startElf == null) {
            speicher = "-1";
        } else {
            speicher = JSONUtilClass.convertToJSONList(null, startElf);
        }
    }


    private void addPlayerToStartElf(String playerListJSON, String lineUp) {

        List<Player> playerList = JSONUtilClass.convertFromJSONPlayerList(playerListJSON);
        DatabaseController.removePlayerToStartElf(this.UserID);
        for (Player p : playerList) {
            if (!DatabaseController.addPlayerToStartElf(this.UserID, p.getId())) {
                speicher = "-1";
            }
        }
        if (speicher != "-1") {
            speicher = "1";
        }
    }

    private void sellPlayer(String playerID) {

        int seps = DatabaseController.sellPlayer(this.UserID, Integer.parseInt(playerID));

        if(seps == -1){
            speicher = "-1";
        }else{
            speicher = String.valueOf(seps);
        }
    }

    private void openStartLootBox() {

        List<Player> playerList = DatabaseController.openStartLootBox(this.UserID);
        if (playerList == null) {
            speicher = "-1";
        } else {
            speicher = JSONUtilClass.convertToJSONList(null, playerList);
        }
    }

    private void openLootBox(String size) {

        List<Player> playerList = DatabaseController.openLootBox(this.UserID, Integer.parseInt(size));
        if (playerList == null) {
            speicher = "-1";
        } else {
            speicher = JSONUtilClass.convertToJSONList(null, playerList);
        }
    }

    private void getTeam() {

        List<Player> team = DatabaseController.getTeam(this.UserID);
        if (team == null) {
            speicher = "-1";
        } else {
            String playerListJSON = JSONUtilClass.convertToJSONList(null, team);
            speicher = playerListJSON;
        }
    }

    private void getSeps() {

        int seps = DatabaseController.getSeps(this.UserID);
        if (seps == -1) {
            speicher = "-1";
        } else {
            speicher = String.valueOf(seps);
        }
    }

    private void declineFriend(String friendName) {

        try {
            int Userid = this.UserID;
            int i = DatabaseController.declineRequest(Userid, friendName);
            speicher = String.valueOf(i);
        } catch (Exception e) {
            speicher = "-6";
        }
    }

    private void updateuser(String s) {

        User user = JSONUtilClass.convertFromJSONUser(s);
        int check = checkIfUserInDatabase(user);
        if (check == 200) {
            boolean b = DatabaseController.updateUser(user, this.UserID);
            if (b) {
                speicher = "400";
            } else {
                speicher = "401";
            }
        } else {
            speicher = String.valueOf(check);
        }
    }

    private void getplayer() {

        List<Player> playerList = DatabaseController.getPlayer();
        speicher = JSONUtilClass.convertToJSONList(null, playerList);
    }

    private void sendFriendInvite(String friendName) {

        try {
            int Userid = this.UserID;
            int i = DatabaseController.sendFriendRequest(Userid, friendName);
            speicher = String.valueOf(i);
        } catch (Exception e) {
            speicher = "-6";
        }
    }

    public void checkForPendingFriends() {

        try {
            int Userid = this.UserID;
            List<User> userList = DatabaseController.checkForPendingFriends(Userid);
            for (User u : userList) {
                u.setPasswort("");
                u.setId(0);
            }
            String s = JSONUtilClass.convertToJSONList(userList, null);
            speicher = s;
        } catch (Exception e) {
            speicher = "-6";
        }
    }

    public void checkForNonPendingFriends() {

        try {
            int Userid = this.UserID;
            List<User> userList = DatabaseController.checkForNoNPendingFriends(Userid);
            for (User u : userList) {
                u.setPasswort("");
                u.setId(0);
            }
            String s = JSONUtilClass.convertToJSONList(userList, null);
            speicher = s;
        } catch (Exception e) {
            speicher = "-6";
        }
    }

    public void checkForRequestedFriends() {

        try {
            int Userid = this.UserID;
            List<User> userList = DatabaseController.checkForRequestsFriends(Userid);
            for (User u : userList) {
                u.setPasswort("");
                u.setId(0);
            }
            String s = JSONUtilClass.convertToJSONList(userList, null);
            speicher = s;
        } catch (Exception e) {
            speicher = "-6";
        }
    }

    public void accept(String friendName) {

        try {
            int Userid = this.UserID;
            int i = DatabaseController.acceptRequest(Userid, friendName);
            speicher = String.valueOf(i);
        } catch (Exception e) {
            speicher = "-6";
        }
    }

    public void removeFriends(String friendName) {

        try {
            int Userid = this.UserID;
            int i = DatabaseController.removeFriend(Userid, friendName);
            speicher = String.valueOf(i);
        } catch (Exception e) {
            speicher = "-6";
        }
    }

    private void postuser(String userAsJSON) {

        User user = JSONUtilClass.convertFromJSONUser(userAsJSON);
        int i = checkIfUserInDatabase(user);
        if (i == 200) {
            boolean saved = DatabaseController.saveUser(user);
            User userWithName = DatabaseController.findUserWithName(user.getUserName());
            System.out.println(userWithName);
            DatabaseController.openStartLootBox(userWithName.getId());
            DatabaseController.initAssets(userWithName.getId());
            if (saved) {
                speicher = "204";
            } else {
                speicher = "205";
            }
        } else {
            speicher = String.valueOf(i);
        }
    }

    private int checkIfUserInDatabase(User userToCheck) {

        List<User> userList = DatabaseController.getUser();
        for (User user : userList) {
            if (userToCheck.getUserName() != null) {
                if (userToCheck.getUserName().equals(user.getUserName())) {
                    return 404;
                }
            }
            if (userToCheck.geteMail() != null) {
                if (userToCheck.geteMail().equals(user.geteMail())) {
                    return 406;
                }
            }
        }
        return 200;
    }

    private void getuser(String userAsJSON) {

        User userToCheck = JSONUtilClass.convertFromJSONUser(userAsJSON);
        System.out.println(userToCheck);
        List<User> userList = DatabaseController.getUser();
        for (User user : userList) {
            if (userToCheck.getUserName().equals(user.getUserName()) && userToCheck.getPasswort().equals(user.getPasswort())) {
                this.UserID = user.getId();
                speicher = "1";
                return;
            }
        }
        speicher = "-1";
    }
}
 
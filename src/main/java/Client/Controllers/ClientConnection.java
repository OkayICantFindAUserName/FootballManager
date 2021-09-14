package Client.Controllers;

import Client.*;
import Client.Models.*;

import java.util.ArrayList;
import java.util.List;

public class ClientConnection {

    public static Client_Socket client_socket = null;
    private static String ipv4 = "127.0.0.1";
    private static int portnummer = 8000;

    public static List<Player> getAllPlayerFromServer() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETPLAYER");
        client_socket.senden_empfangen();
        String playerListJSON = client_socket.getData();
        List<Player> playerList = JSONUtilClass.convertFromJSONPlayerList(playerListJSON);
        return playerList;
    }

    public static List<Player> getPlayersFromTeam() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETTEAM");
        client_socket.senden_empfangen();
        String playerListJSON = client_socket.getData();
        if(playerListJSON.equals("-1")){
            return new ArrayList<>();
        }
        List<Player> playerList = JSONUtilClass.convertFromJSONPlayerList(playerListJSON);
        return playerList;
    }

    public static List<Player> openStartLootBox() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("OPENSTARTLOOTBOX");
        client_socket.senden_empfangen();
        String playerListJSON = client_socket.getData();
        List<Player> playerList = JSONUtilClass.convertFromJSONPlayerList(playerListJSON);
        return playerList;
    }

    public static String addPlayerToStartelf(List<Player> playerList, String value) {
        if (client_socket == null) {
            logOutClient();
        }
        String s = JSONUtilClass.convertToJSONList(null, playerList);
        client_socket.setRequest("ADDPLAYERTOSTARTELF" + "/" + s + "/" + value);
        client_socket.senden_empfangen();
        String info = client_socket.getData();
        return info;
    }

    public static List<Player> getStartElf() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETSTARTELF");
        client_socket.senden_empfangen();
        String info = client_socket.getData();
        if (info != "-1") {
            return JSONUtilClass.convertFromJSONPlayerList(info);
        }
        return null;
    }

    public static List<Player> getFriendStartElf(String friend) {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETFRIENDSTARTELF"+"/"+friend);
        client_socket.senden_empfangen();
        String info = client_socket.getData();
        if (info != "-1") {
            return JSONUtilClass.convertFromJSONPlayerList(info);
        }
        return null;
    }

    public static int sellPlayer(int playerID) {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("SELLPLAYER" + "/" + playerID);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        if (code.equals("-1")) {
            return -1;
        } else {
            return Integer.parseInt(code);
        }
    }


    public static List<Player> openLootBox(int size) {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("OPENLOOTBOX" + "/" + size);
        client_socket.senden_empfangen();
        String info = client_socket.getData();
        if (info != "-1") {
            return JSONUtilClass.convertFromJSONPlayerList(info);
        }
        return null;
    }

    public static String checkLogin(String name, String passwort) {
        String userJSON = JSONUtilClass.convertToJSON(new User(name, passwort), null);

        if (client_socket == null) {
            client_socket = new Client_Socket(ipv4, portnummer);
            client_socket.connect();
        }
        client_socket.setRequest("GETUSER" + "/" + userJSON);
        client_socket.senden_empfangen();
        String userID = client_socket.getData();
        if(userID == null){
            return "unlucky";
        }
        if (!userID.equals("1")) {
            return null;
        }
        return "ez";

    }

    public static int getSEPS() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETSEPS");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        if (code.equals("-1")) {
            return -1;
        } else {
            return Integer.parseInt(code);
        }
    }

    public static String updateUSER(String name, String passwort, String email, String lineup) {
        String userJSON = JSONUtilClass.convertToJSON(new User(name, passwort, email, lineup), null);
        if (client_socket == null) {
            client_socket = new Client_Socket(ipv4, portnummer);
            client_socket.connect();
        }
        client_socket.setRequest("UPDATEUSER" + "/" + userJSON);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        switch (code) {
            case "400":
                return "Benutzerkonto erfolgreich geaendert.";
            case "404":
                return "Benutzername ist bereits vergeben.";
            case "405":
                return "Passwort ist bereits vergeben.";
            case "406":
                return "Email ist bereits vergeben.";
            case "204":
                return "Account erfolgreich angelegt";
            case "205":
                return "Datenbank Error. Rufen Sie bitte Ihren Admin an.";
        }
        return "Netzwerk Error. Rufen Sie bitte Ihren Admin an.";
    }

    public static String postUSER(String name, String passwort, String email) {
        String userJSON = JSONUtilClass.convertToJSON(new User(name, passwort, email), null);
        if (client_socket == null) {
            client_socket = new Client_Socket(ipv4, portnummer);
            client_socket.connect();        }
        client_socket.setRequest("POSTUSER" + "/" + userJSON);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        switch (code) {
            case "404":
                return "Benutzername ist bereits vergeben.";
            case "406":
                return "Email ist bereits vergeben.";
            case "204":
                return "Account erfolgreich angelegt";
            case "205":
                return "Datenbank Error. Rufen Sie bitte Ihren Admin an.";
        }
        return "Netzwerk Error. Rufen Sie bitte Ihren Admin an.";
    }

    public static void setip(String ip) {
        ipv4 = ip;
    }

    public static void setport(int port) {
        portnummer = port;
    }

    public static String sendFriendInvite(String friendName) {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("SENDFRIENDINVITE" + "/" + friendName);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        switch (code) {
            case "-4":
                return "Name des Freundes konnte nicht gefunden werden, achten Sie auf Groß und Kleinschreibung.";
            case "-5":
                return "Bitte verwenden Sie nicht ihren eignen Benutzernamen.";
            case "-1":
                return "Freundschaftsanfrage steht schon aus.";
            case "-2":
                return "Datenbank error.";
            case "1":
                return "Freundschaftsanfrage erfolgreich gesendet.";
            case "2":
                return "Freundschaftsanfrage bereits pending, anfrage wurde angenommen.";
            case "-7":
                return "Bereits in Freundsliste.";
        }
        return "Netzwerk error.";
    }

    public static List<User> checkForPendingFriends() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("CHECKFORPENDINGFRIENDS");
        client_socket.senden_empfangen();
        String list = client_socket.getData();
        List<User> users = JSONUtilClass.convertFromJSONUserList(list);
        return users;
    }

    public static List<User> checkForNonPendingFriends() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("CHECKFORNONPENDINGFRIENDS");
        client_socket.senden_empfangen();
        String list = client_socket.getData();
        List<User> users = JSONUtilClass.convertFromJSONUserList(list);
        return users;
    }

    public static List<User> getRequestedFriends() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("REQUESTEDFRIENDS");
        client_socket.senden_empfangen();
        String list = client_socket.getData();
        List<User> users = JSONUtilClass.convertFromJSONUserList(list);
        return users;
    }

    public static String acceptFriend(String friendName) {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("ACCEPTFRIEND" + "/" + friendName);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        switch (code) {
            case "-6":
                return "Ein Parse error ist entstanden.";
            case "-2":
                return "Benutzer nicht gefunden.";
            case "-1":
                return "Datenbank fehler.";
            case "1":
                return "Erfolgereich akzeptiert.";
        }
        return "Netzwerk error.";
    }

    public static String removeFriend(String friendName) {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("REMOVEFRIEND" + "/" + friendName);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        switch (code) {
            case "-6":
                return "Ein Parse error ist entstanden.";
            case "-2":
                return "Benutzer nicht gefunden.";
            case "-1":
                return "Datenbank fehler.";
            case "1":
                return "Erfolgereich entfernt.";
        }
        return "Netzwerk error.";
    }

    public static String declineFriend(String friendName) {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("DECLINEFRIEND" + "/" + friendName);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        switch (code) {
            case "-6":
                return "Ein Parse error ist entstanden.";
            case "-2":
                return "Benutzer nicht gefunden.";
            case "-1":
                return "Datenbank fehler.";
            case "1":
                return "Erfolgereich abgelehnt.";
        }
        return "Netzwerk error.";
    }

    public static void logOut() {
        client_socket.setRequest("LOGOUT");
        client_socket.senden_empfangen();
        client_socket = null;
    }

    private static void logOutClient() {
        Main.setRoot("LoginFenster");

    }

    public static String getTeamName(){
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETTEAMNAMEN");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;
    }

    public static String getfriendTeamName(String friend){
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETFRIENDTEAMNAMEN" + "/" + friend);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;
    }


    public static List<User> getMatchRequests() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETMATCHREQUEST");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List<User> Users = JSONUtilClass.convertFromJSONUserList(code);
        return Users;
    }

    public static List<MatchWrapper> getMatchHistory() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETMATCHHISTORY");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List<MatchWrapper> matches = JSONUtilClass.convertFromJSONMatchWrapperList(code);
        return matches;
    }

    public static List<MatchWrapper> getFriendMatchHistory(String friend) {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETFRIENDMATCHHISTORY" + "/" + friend);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List<MatchWrapper> matches = JSONUtilClass.convertFromJSONMatchWrapperList(code);
        return matches;
    }

    public static String requestMatch(String friendName) {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("REQUESTMATCH" + "/" + friendName);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;
    }

    public static String acceptMatch(String friendName) {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("ACCEPTMATCH" + "/" + friendName);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;
    }


    public static String declineMatchRequest(String friendName) {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("DECLINEMATCHREQUEST" + "/" + friendName);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;
    }

    public static List<User> getRequestedMatches() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETREQUESTEDMATCHES");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List<User> Users = JSONUtilClass.convertFromJSONUserList(code);
        return Users;

    }

    public static Assets getAssets() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETASSET");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        Assets assets = JSONUtilClass.convertFromJSONAssets(code);
        return assets;

    }

    public static List<Tournement> getAllTournements() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETALLTOURNEMENTS");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List<Tournement> tournements = JSONUtilClass.convertFromJSONTournementList(code);
        return tournements;
    }

    public static String addUserToTournement(String Name) {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("ADDUSERTOTOURNEMENT" + "/" + Name);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;
    }

    public static List<Tournement> getAllPendingTournements() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETALLPENDINGTOURNEMENTS");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List<Tournement> tournements = JSONUtilClass.convertFromJSONTournementList(code);
        return tournements;

    }

    public static String createNewTournement(Tournement tournement) {

        if (client_socket == null) {
            logOutClient();
        }
        String s = JSONUtilClass.convertToJSONTournement(tournement);
        client_socket.setRequest("CREATENEWTOURNEMENT" + "/" + s);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;

    }

    public static List<Tournement> getNonPendingTournements() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETALLNONPENDINGTOURNEMENTS");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List<Tournement> tournements = JSONUtilClass.convertFromJSONTournementList(code);
        return tournements;

    }

    public static List<Tournement> getAllCreatedTournements() {
        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETALLCREATEDTOURNEMENTS");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List<Tournement> tournements = JSONUtilClass.convertFromJSONTournementList(code);
        return tournements;
    }

    public static List<MatchWrapper> getMatchFromT(String name) {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETMATCHFROMT" + "/" + name);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        System.out.println("Code:"+code);
        List<MatchWrapper> matches = JSONUtilClass.convertFromJSONMatchWrapperList(code);
        return matches;

    }

    public static String upgradeAsset(int i, String text) {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("UPGRADEASSET" + "/" + i + "/" + text);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;

    }

    public static String updateAssetName(String text) {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("UPDATEASSET" + "/" + text);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        return code;

    }

    public static String sendMessages(String friendName,String message) {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("SENDMESSAGE" + "/" + friendName + "/" + message);
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        switch (code) {
            case "-2":
                return "Datenbank error.";
            case "1":
                return "Nachricht erfolgreich gesendet.";
        }
        return "Netzwerk error.";
    }

    public static List <Nachricht> getMessages() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETMESSAGE");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List <Nachricht> nachrichtenListe = JSONUtilClass.convertFromJSONNachrichtList(code);
        return nachrichtenListe;

    }

    public static List <Nachricht> getSentMessages() {

        if (client_socket == null) {
            logOutClient();
        }
        client_socket.setRequest("GETSENTMESSAGE");
        client_socket.senden_empfangen();
        String code = client_socket.getData();
        List <Nachricht> nachrichtenListe = JSONUtilClass.convertFromJSONNachrichtList(code);
        return nachrichtenListe;

    }
}

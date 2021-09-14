package Client;

import Client.Models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JSONUtilClass {


    public static String convertToJSONList(List<User> userList, List<Player> playerList) {
        String JSON;
        Gson gson = new Gson();
        if (playerList != null) {
            Type listType = new TypeToken<List<Player>>() {
            }.getType();
            JSON = gson.toJson(playerList);
            return JSON;
        } else if (userList != null) {
            Type listType = new TypeToken<List<User>>() {
            }.getType();
            JSON = gson.toJson(userList, listType);
            return JSON;
        }
        return null;
    }

    public static String convertToJSON(User user, Player player) {
        String JSON;
        Gson gson = new Gson();
        if (player != null) {
            JSON = gson.toJson(player);
            return JSON;
        } else if (user != null) {
            JSON = gson.toJson(user);
            return JSON;
        }
        return null;
    }

    public static User convertFromJSONUser(String JSON) {
        Gson gson = new Gson();
        User user = gson.fromJson(JSON, User.class);
        return user;
    }

    public static Player convertFromJSONPlayer(String JSON) {
        Gson gson = new Gson();
        Player player = gson.fromJson(JSON, Player.class);
        return player;
    }

    public static List<User> convertFromJSONUserList(String JSON) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        List<User> userList2 = gson.fromJson(JSON, listType);
        return userList2;
    }

    public static List<Player> convertFromJSONPlayerList(String JSON) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Player>>() {
        }.getType();
        List<Player> playerList2 = gson.fromJson(JSON, listType);
        return playerList2;
    }

    public static List<Match> convertFromJSONMatchList(String JSON) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Match>>() {
        }.getType();
        List<Match> MatchList2 = gson.fromJson(JSON, listType);
        return MatchList2;
    }

    public static String convertMatchToJSONList(List<Match> matchList) {
        String JSON;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Match>>() {
        }.getType();
        JSON = gson.toJson(matchList,listType);
        return JSON;

    }

    public static String convertMatchWeapperToJSONList(List<Server.MatchWrapper> matchWrapperHistory) {
        String JSON;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Server.Model.Match>>() {
        }.getType();
        JSON = gson.toJson(matchWrapperHistory,listType);
        return JSON;
    }

    public static List<MatchWrapper> convertFromJSONMatchWrapperList(String code) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<MatchWrapper>>() {
        }.getType();
        List<MatchWrapper> MatchList2 = gson.fromJson(code, listType);
        return MatchList2;
    }

    public static Assets convertFromJSONAssets(String code) {
        Gson gson = new Gson();
        Assets assets = gson.fromJson(code,Assets.class);
        return assets;
    }

    public static List<Tournement> convertFromJSONTournementList(String code) {

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Tournement>>() {
        }.getType();
        List<Tournement> MatchList2 = gson.fromJson(code, listType);
        return MatchList2;

    }

    public static List<Nachricht> convertFromJSONNachrichtList(String JSON){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Nachricht>>(){}.getType();
        List <Nachricht> playerList2 = gson.fromJson(JSON, listType);
        return playerList2;
    }

    public static String convertToJSONTournement(Tournement tournement) {

        Gson gson = new Gson();
        String s = gson.toJson(tournement);
        return s;

    }

}

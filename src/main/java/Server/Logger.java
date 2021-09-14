package Server;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import Server.Model.Player;
import org.apache.commons.io.input.ReversedLinesFileReader;

public class Logger {

    public static void log(String seite, Player player) {
        try {
            FileWriter myWriter = new FileWriter("Server.Logger.txt",true);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            myWriter.write(formatter.format(date) +
                    ": " + player + "von: " + seite + String.format("%n"));
            System.out.print(formatter.format(date) +
                    ": " + player + "von: " + seite + String.format("%n"));
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String getLatestPlayerName() {
        try {
            File file = new File("Server.Logger.txt");
            ReversedLinesFileReader object = new ReversedLinesFileReader(file);
            String URL = object.readLine().split("von: ")[1].split("-")[1].substring(0,2);
            return URL;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }catch (NullPointerException e){
        }

        return null;
    }

}
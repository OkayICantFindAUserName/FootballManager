package Server;



import Server.Controller.DatabaseController;
import Server.Model.Player;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.sql.Date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;


public class Scrapper {

    public static int scrapped;
    private static List<Player> players;
    private static boolean stop;

    //Hilfsfunktion um einen String in ein Date zu parsen
    public static Date dateFormatter(String s) {
        DateFormat fmt = new SimpleDateFormat("dd. MMMM yyyy", Locale.GERMANY);
        java.util.Date d = null;
        try {
            d = fmt.parse(s);
        } catch (ParseException e) {
        }
        return new Date(d.getTime());
    }

    public static Player parsePlayer(ArrayList<String> strings, String land, String name) {
        //Erstellen der 3 Fehlendes Variablen
        String pos = null;
        Date dateofBirth = null;
        String club = null;
        //Schleife die über den Paragraphen geht
        for (String s : strings) {
            //Falls Position gefunden wurden mit hilfe von Splits isolieren und in pos speichern
            if (s.contains("Position:")) {
                pos = s.split(": ")[1].split(" ")[0];
            }
            //Falls Geboren gefunden wurden mit hilfe von Splits isolieren und in dateofBirth speichern und Parsen
            if (s.contains("Geboren:")) {
                try {
                    dateofBirth = dateFormatter(s.split(":")[1]);
                } catch (Exception e) {
                }
            }
            //Falls Verein gefunden wurden mit hilfe von Splits isolieren und in club speichern
            if (s.contains("Verein:")) {
                club = s.split(": ")[1].split(" Mehr")[0];
            }
        }
        //Falls einer der Wert nicht gefunden wurde returnen wir null
        if (pos == null || dateofBirth == null || club == null) {
            System.out.println("Too little Data");
            return null;
        }
        //Sonst returnen wir den Server.Model.Player
        Player player = new Player(name, pos, dateofBirth, land, club);
        return player;

    }

    public static void getPlayerStats(String url, String land, String name) {
        try {
            // Verbindung durch Jsoup mit Website Aufnehmen und HTML in document speichern
            Document document;
            try {
                document = Jsoup.connect("https://fbref.com" + url).get();
            } catch (HttpStatusException e) {
                System.out.println("Die Website erhielt zuviele Anfrage bitte scrappen sie später weiter.");
                stop = true;
                return;
            }
            //Wichtige inforamtionen aus dem Meta daten speichern
            Elements elements = document.select("#meta p");
            //Elemente zu String konvertieren und in ene Liste speichern
            ArrayList<String> strings = new ArrayList<>();
            //Parsen von Element zu String für einfacher nutzung
            for (Element e : elements) {
                strings.add(e.text());
            }
            // Hilfsmethode zum parsen des Players
            Player player = parsePlayer(strings, land, name);
            //Falls der Spieler als null returned ist was schief gegangen
            if (players.contains(player)) {
                return;
            }
            if (player == null) {
                return;
            }

            //Hilfsmethode um den Spieler zu SQL datenbank via hibernate hinzuzufügen
            boolean b = DatabaseController.addPlayerTooDatabase(player);
            if (b) {
                Logger.log("https://fbref.com" + url, player);
                scrapped++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Diese Funktion findet die Links zu den Spieler profilen und Die namen nationalität der Spieler
    public static void getPlayerNames(String url) {
        try {
            // Verbindung durch Jsoup mit Website Aufnehmen und HTML in document speichern
            Document document = Jsoup.connect("https://fbref.com" + url).get();
            // Liste jedes paragraphens
            Elements listOfElementsOfPlayerAndCountries = document.select(".section_content").first().select("p");
            for (int i = 0; i < listOfElementsOfPlayerAndCountries.size(); i++) {
                int length = listOfElementsOfPlayerAndCountries.get(i).text().split(String.valueOf((char) 183)).length;

                if (length < 4) {
                    listOfElementsOfPlayerAndCountries.remove(i);
                    i--;
                }
            }

            listOfElementsOfPlayerAndCountries = listOfElementsOfPlayerAndCountries.select("a[href],.f-i");

            //Hilfsvariabelle
            boolean hasFlag;
            // eine liste aller Länder abkürzungen
            String[] locales = Locale.getISOCountries();
            //Schleife die durch die Paragraphen iteriert
            for (int i = 0; i < listOfElementsOfPlayerAndCountries.size(); i++) {
                //True da erstmal angenommen wir das es ein name sei
                hasFlag = true;
                //Falls an einer ungraden stelle ist soll es eine Flagge sein
                if (i % 2 == 1) {
                    //False da erstmal angenommen wird das es keine Flagge ist
                    hasFlag = false;
                    //Schleife durch die Länder abkürzungen
                    for (String countryCode : locales) {
                        //Falls vorhanden ist es eine Flagge
                        if (countryCode.toLowerCase().equals(listOfElementsOfPlayerAndCountries.get(i).text())) {
                            hasFlag = true;
                        }
                    }
                }
                //Falls keine Flagge ist wird der vorgänger removed und der counter um 1 zurück gesetzt
                if (!hasFlag) {
                    listOfElementsOfPlayerAndCountries.remove(i - 1);
                    i = i - 1;
                }
            }
            // 3 Listen um die Länder, Namen und URLs aus der Paragraphne zu speichern
            ArrayList<String> countries = new ArrayList<>();
            ArrayList<String> player = new ArrayList<>();
            ArrayList<String> URLs = new ArrayList<>();
            //schleife über die Paragraphen
            for (int i = 0; i < listOfElementsOfPlayerAndCountries.size(); i++) {
                //Falls ungrade ist es eine FLagge
                if (i % 2 == 1) {
                    //Schleife über alle länder Codes
                    for (String countryCode : locales) {
                        // Erstellen einen Objekts mit dem jeweiligen Länder code.
                        Locale obj = new Locale("", countryCode);
                        //Übersetzten des Flaggen codes in Länder Namen
                        if (obj.getCountry().toLowerCase().equals(listOfElementsOfPlayerAndCountries.get(i).text())) {
                            countries.add(obj.getDisplayCountry());
                        }
                    }
                    //Anstonsten ist es ein Link
                } else {
                    //Hinzufügen des Namens des Links und somit des Namens des spielers
                    player.add(listOfElementsOfPlayerAndCountries.get(i).text());
                    //Hinzufügen der URL
                    URLs.add(listOfElementsOfPlayerAndCountries.get(i).attr("href"));
                }
            }
            //Schleife geht durch Spieler durch die oben erstellt wurden
            for (int i = 0; i < player.size(); i++) {
                if (stop) {
                    return;
                }
                //An getPlayerStats übergeben
                try {
                    int finalI = i;
                    Thread t = new Thread(() -> {
                        getPlayerStats(URLs.get(finalI), countries.get(finalI), player.get(finalI));
                    });
                    t.start();
                    if (i % 5 == 0) {
                        t.join();
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Funktion um alle Anfangsbuchstaben zu bekommen
    public static void getNames() {
        players = DatabaseController.getPlayer();
        if (players == null) {
            players = new ArrayList<>();
        }
        scrapped = players.size();
        String latestPlayerName = Logger.getLatestPlayerName();
        if (latestPlayerName == null) {
            latestPlayerName = "Aa";
        }
        stop = false;
        try {
            // Verbindung durch Jsoup mit Website Aufnehmen und HTML in document speichern
            Document document = Jsoup.connect("https://fbref.com/de/players/").get();
            // Alle Elemente mit Klassen Namen page_index funden und den ersten eintragen in element speichern
            Elements element = document.getElementsByClass("page_index").first().select("a[href]");
            // Über alle Links iterieren und die funktion getPlayerNames aufrufen
            for (Element e : element) {
                if (stop) {
                    return;
                }
                if (latestPlayerName.compareToIgnoreCase(e.text()) <= 0) {

                    System.out.println(e.attr("href"));
                    getPlayerNames(e.attr("href"));
                }
            }
            stopScrapping();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  public static void main(String[] Args) {
    // 	System.out.println( getMatches("Manuel Neuer") );
    //regEx();
    //  }

    //Google Ergebnisse f�r St�rke der Spieler
    public static String getMatches(String playerName) {
        String playerMatches = "";
        String[] s = null;

        // Verbindung durch Jsoup mit Website Aufnehmen und HTML in document speichern
        Document document;
        try {
            document = Jsoup.connect("https://www.google.com/search?q=\"" + playerName.replace(" ", "%20") + "\"").get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // Alle Elemente mit Klassen Namen page_index funden und den ersten eintragen in element speichern
        //Element element = document.getElementsByClass("page_index").first();
        String element = document.select("#result-stats").html();

        playerMatches = element.split(" ")[1];

/*
        //regex pattern
        final String regex = "(Ungefähr)? (\\d*.?\\d*.?\\d*.?\\d*) Ergebnisse";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(element);


        while (matcher.find()) {
            playerMatches = matcher.group(2);
            break;
        }
*/
        playerMatches = playerMatches.replace("’", "");
        playerMatches = playerMatches.replace(".", "");
        return playerMatches.replace(",", "");
    }

    public static boolean stopScrapping() {

        stop = true;
        Boolean aBoolean = null;
        try {
            aBoolean = setScreachresults();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean b = errechneGesamtStärkeAnders();
        return b && aBoolean;
    }
    
    public static Boolean setScreachresults() throws InterruptedException {
        List<Player> player = DatabaseController.getPlayer();
        int i = 0;
        for (Player p : player) {
            Thread thread = new Thread() {
                public void run() {

                    if (p.getScreachResults() == 0) {
                        try {
                            String matches = Scrapper.getMatches(p.getName());
                            if (matches == null) {
                                return;
                            }
                            if (matches == ""||matches == "0") {
                                matches = "1";
                            }
                            System.out.print(matches + " Auf Spieler: ");
                            System.out.println(p.getName());
                            p.setScreachResults(Integer.parseInt(matches));
                            DatabaseController.updatePlayers(p);
                        } catch (NumberFormatException ex) {
                            DatabaseController.deletePlayer(p.getId());
                            return;
                        }
                    }
                }
            };
            thread.start();
            i++;
            if(i % 3 == 0){
                thread.join();
                i = 0;
            }
        }
        return true;
    }
   
    public static boolean errechneGesamtStärke() {

        List<Player> players = DatabaseController.getPlayerSorted();
        if(players==null)return false;
        int max = players.get(players.size() - 1).getScreachResults();
        int min = players.get(0).getScreachResults();
        for (Player p : players) {
            double minToZero = (p.getScreachResults() - min);
            double maxToOne = minToZero / (max - min);
            double allBetweenZeroAndHundred = maxToOne * 100;
            if (allBetweenZeroAndHundred > 100) {
                allBetweenZeroAndHundred = 100;
            }
            System.out.println("allBetweenZeroAndHundred: " + (int) allBetweenZeroAndHundred);
            p.setGesamtStaerke((int) allBetweenZeroAndHundred);
            DatabaseController.updatePlayers(p);
        }
        return true;

    }
    
    public static boolean errechneGesamtStärkeAnders() {

        List<Player> players = DatabaseController.getPlayerSorted();
        if(players == null)return false;
        double length = players.size();
        double intervall2 = (length / 100);
        double intervall = (1 / intervall2);
        System.out.println(intervall);
        System.out.println(length);
        int i = 0;
        double playerCounter = 0;
        while (i < length) {
            playerCounter += intervall;
            Player player = players.get(i);
            if(playerCounter < 0.2){
                player.setGesamtStaerke(0);
            }else if(playerCounter > 99.8){
                player.setGesamtStaerke(100);
            }else{
                double roundOff = (double) Math.round(playerCounter * 100) / 100;
                System.out.println(roundOff);
                player.setGesamtStaerke(roundOff);
            }
            DatabaseController.updatePlayers(player);
            i++;
        }
        return true;

    }

}

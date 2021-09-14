//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import Server.Controller.DatabaseController;
import Server.Model.Player;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * Das ist die Test Klasse für die einzelnen Kriterien und wurde mithilfe von JUnit5 erstellt.
 */
public class TestMatches {

    /**
     * Das sind unsere Beiden Team die für unsere Test die Matches Bestreiten, die beiden Listen werden immer aus Random
     * Players erstellt haben aber immer die Formation 4-4-2
     */
    List<Player> testPlayers1 = new ArrayList();
    List<Player> testPlayers2 = new ArrayList();

    /**
     * Hier werden die Beiden Listen gefüllt
     */
    @BeforeEach
    public void setup() {

        List<Player> player = DatabaseController.getPlayer();
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

        this.testPlayers1.clear();
        this.testPlayers2.clear();
        this.testPlayers1.addAll(TW);
        this.testPlayers1.addAll(DF);
        this.testPlayers1.addAll(MF);
        this.testPlayers1.addAll(FW);
        this.testPlayers2.addAll(this.testPlayers1);
    }

    /**
     * Dieser Test testet die Aufstellung in dem alle möglichen Aufstellung in einem Array gespeichert werden.
     * Die Aufstellung werden alle gegeneinander getestest und werden dann gegen die Aufstellungsmatrix getest
     * Die ist in resources/Icons/AufstellungMatrix zu finden.
     */
    @Test
    public void testAufstellung() {
        String[] lineups = new String[]{"4-4-2", "5-4-1", "3-4-3", "4-3-3", "3-5-2"};
        ArrayList<Integer> actual = new ArrayList();
        List<Integer> expected = Arrays.asList(0, 1, 1, -1, -1, 0, -1, 1, 1, 0, -1, 1, 0, -1, 0);

        for (int i = 0; i < lineups.length; ++i) {
            for (int j = i; j < lineups.length; ++j) {
                actual.add(DatabaseController.getLineUpBonus(lineups[i], lineups[j]));
            }
        }

        assertEquals(actual, expected);
    }
    /**
     * Da die beiden Teams identisch sind sollte durch dieses Kriterium immer unentschieden entstehen.
     * Danach wird ein Spieler aus der 1. Liste geupgraded so das die 1. Liste besser ist als die 2.
     * Nun sollte die 1. Mannschaft gewinnen und dannach wird das selbe nochmal mit der anderen Mannschaft gemacht.
     */
    @Test
    public void testDurchschnittsStaerke() {
        assertEquals(this.testPlayers1, this.testPlayers2);
        int durchschnittStÃ¤rkeTeam = DatabaseController.getDurchschnittStärkeTeam(this.testPlayers1, this.testPlayers2, "4-4-2", "4-4-2");

        assert durchschnittStÃ¤rkeTeam == 0;

        Player player = this.upgrade((Player) this.testPlayers1.get(0));
        this.testPlayers1.remove(0);
        this.testPlayers1.add(player);
        durchschnittStÃ¤rkeTeam = DatabaseController.getDurchschnittStärkeTeam(this.testPlayers1, this.testPlayers2, "4-4-2", "4-4-2");

        assert durchschnittStÃ¤rkeTeam == 1;

        this.setup();
        player = this.upgrade((Player) this.testPlayers2.get(0));
        this.testPlayers2.remove(0);
        this.testPlayers2.add(player);
        durchschnittStÃ¤rkeTeam = DatabaseController.getDurchschnittStärkeTeam(this.testPlayers1, this.testPlayers2, "4-4-2", "4-4-2");

        assert durchschnittStÃ¤rkeTeam == -1;

    }

    /**
     * Das selbe wie oben nur mit MF Spielern.
     */
    @Test
    public void testMFDurchscnittsStaerke() {
        assertEquals(this.testPlayers1, this.testPlayers2);
        int durchschnittStÃ¤rkeTeam = DatabaseController.getMFDurchschnittStärke(this.testPlayers1, this.testPlayers2, "4-4-2", "4-4-2");

        assert durchschnittStÃ¤rkeTeam == 0;

        Player player = this.upgrade((Player) this.testPlayers1.get(5));
        this.testPlayers1.remove(5);
        this.testPlayers1.add(player);
        durchschnittStÃ¤rkeTeam = DatabaseController.getMFDurchschnittStärke(this.testPlayers1, this.testPlayers2, "4-4-2", "4-4-2");

        assert durchschnittStÃ¤rkeTeam == 1;

        this.setup();
        player = this.upgrade((Player) this.testPlayers2.get(5));
        this.testPlayers2.remove(5);
        this.testPlayers2.add(player);
        durchschnittStÃ¤rkeTeam = DatabaseController.getMFDurchschnittStärke(this.testPlayers1, this.testPlayers2, "4-4-2", "4-4-2");

        assert durchschnittStÃ¤rkeTeam == -1;

    }
    /**
     * Eine Kleine Hilfsmethode um einen Spieler aus der Liste zu kopieren und zu verbesseren.
     */
    private Player upgrade(Player player) {
        Player player1 = new Player();
        player1.setName(player.getName());
        player1.setDateOfBirth(player.getDateOfBirth());
        player1.setNation(player.getNation());
        player1.setClub(player.getClub());
        player1.setGesamtStaerke(player.getGesamtStaerke() + 10.0D);
        player1.setPos(player.getPos());
        return player1;
    }

    /**
     * Wir verringern die Anzahl der Mannschaft auf 3 so das wir immer die selben 3 Spieler testen.
     * Da die Liste identisch ist sollten wir ein unentschieden erhalten.
     * ähnlich wie oben upgraden wir 2 spieler aus den beiden Teams und testen das beide verbesserten Team gewinnen.
     */
    @Test
    public void testBench() {
        for (int i = testPlayers1.size() - 1; i > 2; i--) {

            testPlayers1.remove(i);
            testPlayers2.remove(i);

        }

        assertEquals(this.testPlayers1, this.testPlayers2);

        int chipsaStrength = DatabaseController.getChipsaStrength(testPlayers1, testPlayers2);
        assert (chipsaStrength == 0);

        Player player = this.upgrade((Player) this.testPlayers1.get(0));
        this.testPlayers1.remove(0);
        this.testPlayers1.add(player);

        chipsaStrength = DatabaseController.getChipsaStrength(testPlayers1, testPlayers2);
        assert (chipsaStrength == 1);

        setup();

        for (int i = testPlayers1.size() - 1; i > 2; i--) {

            testPlayers1.remove(i);
            testPlayers2.remove(i);

        }

        assertEquals(this.testPlayers1, this.testPlayers2);

        player = this.upgrade((Player) this.testPlayers2.get(0));
        this.testPlayers2.remove(0);
        this.testPlayers2.add(player);

        chipsaStrength = DatabaseController.getChipsaStrength(testPlayers1, testPlayers2);
        assert (chipsaStrength == -1);

    }


    /**
     * ähnlich wie oben nur mit 1 Spieler in der Lsite um keinen Zufall zu haben.
     */
    @Test
    public void testFieldPlayer() {

        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Player> players1 = new ArrayList<>();

        Random random = new Random();
        List<Player> player = DatabaseController.getPlayer();

        int i = random.nextInt(player.size());
        players.add(player.get(i));
        players1.add(player.get(i));

        assertEquals(players, players1);

        int pairStrength = DatabaseController.getPairStrength(players, players1);

        assert (pairStrength == 0);

        Player upgrade = upgrade(players.get(0));
        players.remove(0);
        players.add(upgrade);

        pairStrength = DatabaseController.getPairStrength(players, players1);

        assert (pairStrength == 1);

    }

    /**
     * Hier wird lediglich geprüft ob es die möglichekeit für alle drei Ergebnisse erreichbar ist.
     */
    @Test
    public void zufallszahl() {

        ArrayList<Integer> intList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            intList.add(DatabaseController.getRandomLottery());
            System.out.println(intList);
        }
        assert (intList.contains(-1));
        assert (intList.contains(1));
        assert (intList.contains(0));

    }


}




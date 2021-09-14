package Client.Models;

import javafx.beans.property.*;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
public class Player {
    private int id;

    private String name;
    private String pos;
    private Date dateOfBirth;
    private String nation;
    private String club;
    private int screachResults;
    private double gesamtStaerke;

    public Player() {
    }

    public Player(int id, String name, String pos, Date dateOfBirth, String nation, String club, int screachResults, int gesamtStaerke) {
        this.id = id;
        this.name = name;
        this.pos = pos;
        this.dateOfBirth = dateOfBirth;
        this.nation = nation;
        this.club = club;
        this.screachResults = screachResults;
        this.gesamtStaerke = gesamtStaerke;
    }

    public Player(String pascal, String df, Date date, String deutschland, String sep) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getDateOfBirth() {
        java.util.Date today = new java.util.Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int d1 = Integer.parseInt(formatter.format(dateOfBirth));
        int d2 = Integer.parseInt(formatter.format(today));
        int age = (d2 - d1) / 10000;
        return age;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public int getScreachResults() {
        return screachResults;
    }

    public void setScreachResults(int screachResults) {
        this.screachResults = screachResults;
    }

    public double getGesamtStaerke() {
        return gesamtStaerke;
    }

    public void setGesamtStaerke(double gesamtStaerke) {
        this.gesamtStaerke = gesamtStaerke;
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public IntegerProperty dateOfBirthProperty() {

        java.util.Date today = new java.util.Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int d1 = Integer.parseInt(formatter.format(dateOfBirth));
        int d2 = Integer.parseInt(formatter.format(today));
        int age = (d2 - d1) / 10000;

        return new SimpleIntegerProperty(age);

    }

    public StringProperty nationProperty() {
        return new SimpleStringProperty(nation);
    }

    public StringProperty posProperty() {
        return new SimpleStringProperty(pos);
    }

    public StringProperty clubProperty() {
        return new SimpleStringProperty(club);
    }

    public DoubleProperty strengthProperty() {
        return new SimpleDoubleProperty(gesamtStaerke);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pos='" + pos + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nation='" + nation + '\'' +
                ", club='" + club + '\'' +
                ", screachResults=" + screachResults +
                ", gesamtStärke=" + gesamtStaerke +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return screachResults == player.screachResults &&
                gesamtStaerke == player.gesamtStaerke &&
                Objects.equals(name, player.name) &&
                Objects.equals(pos, player.pos) &&
                Objects.equals(dateOfBirth, player.dateOfBirth) &&
                Objects.equals(nation, player.nation) &&
                Objects.equals(club, player.club);
    }

}

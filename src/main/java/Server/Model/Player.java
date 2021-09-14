package Server.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "pos")
    private String pos;
    @Column(name = "dateOfBirth")
    private Date dateOfBirth;
    @Column(name = "nation")
    private String nation;
    @Column(name = "club")
    private String club;
    @Column(name = "screachResults")
    private int screachResults;
    @Column(name = "GESAMTSTAERKE")
    private double gesamtStaerke;

    public int getScreachResults() {
        return screachResults;
    }

    public void setScreachResults(int screachResults) {
        this.screachResults = screachResults;
    }

    public double getGesamtStaerke() {
        return gesamtStaerke;
    }

    public void setGesamtStaerke(double gesamtStärke) {
        this.gesamtStaerke = gesamtStärke;
    }

    public Player() {
    }

    public Player(String name, String pos, Date dateOfBirth, String nation, String club) {
        this.name = name;
        this.pos = pos;
        this.dateOfBirth = dateOfBirth;
        this.nation = nation;
        this.club = club;

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

    public Date getDateOfBirth() {
        return dateOfBirth;
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

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty dateOfBirthProperty() {
        return new SimpleStringProperty(dateOfBirth.toString());
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

        return name.equals(player.getName()) &&
                pos.equals(player.getPos()) &&
                dateOfBirth.equals(player.getDateOfBirth()) &&
                nation.equals(player.getNation()) &&
                club.equals(player.getClub());
    }

    public void update(Player newplayer) {

        setGesamtStaerke(newplayer.getGesamtStaerke());
        setScreachResults(newplayer.getScreachResults());

    }
}

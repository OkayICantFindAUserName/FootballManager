package Server.Model;

import javax.persistence.*;
@Entity
@Table(name = "Team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "UserID")
    int userID;
    @Column(name = "PlayerID")
    int playerID;
    @Column(name = "Startelf")
    Boolean startElf;

    public Team(int userID, int playerID) {
        this.userID = userID;
        this.playerID = playerID;
    }
    public Team() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }


    public Boolean getStartElf() {
        return startElf;
    }

    public void setStartElf(Boolean startElf) {
        this.startElf = startElf;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", userID=" + userID +
                ", playerID=" + playerID +
                ", startElf=" + startElf +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return
                userID == team.userID &&
                playerID == team.playerID;
    }

}

package Server.Model;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "result")
    String result;
    @Column(name = "senderUseriD")
    public
    int senderUserID;
    @Column(name = "receiverUserID")
    public
    int receiverUserID;
    @Column(name = "pending")
    public
    boolean pending;

    public Match() {
    }

    public Match(int senderUserID, int receiverUserID) {
        this.senderUserID = senderUserID;
        this.receiverUserID = receiverUserID;
        this.pending = true;
        this.result = "tbd";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getSenderUserID() {
        return senderUserID;
    }

    public void setSenderUserID(int homeTeamUserID) {
        this.senderUserID = homeTeamUserID;
    }

    public int getReceiverUserID() {
        return receiverUserID;
    }

    public void setReceiverUserID(int awayTeamUserID) {
        this.receiverUserID = awayTeamUserID;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    @Override
    public String toString() {

        return "Match{" +
                "id=" + id +
                ", result='" + result + '\'' +
                ", senderUserID=" + senderUserID +
                ", receiverUserID=" + receiverUserID +
                ", pending=" + pending +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return getId() == match.getId() &&
                isPending() == match.isPending() &&
                Objects.equals(getResult(), match.getResult()) &&
                Objects.equals(getSenderUserID(), match.getSenderUserID()) &&
                Objects.equals(getReceiverUserID(), match.getReceiverUserID());
    }
}

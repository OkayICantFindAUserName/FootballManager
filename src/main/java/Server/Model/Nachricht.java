package Server.Model;

import Server.Controller.DatabaseController;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "nachrichten")
public class Nachricht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "sender")
    int senderID;
    @Column(name = "receiver")
    int receiverID;
    @Column(name = "nachricht")
    String nachricht;
    @Column(name = "date")
    private Date date;
    @Column(name = "senderName")
    String senderName;
    @Column(name = "receiverName")
    String receiverName;

    public Nachricht() {
    }

    public void setName() {
        senderName = DatabaseController.getUserFromID(senderID).getUserName();
        receiverName = DatabaseController.getUserFromID(receiverID).getUserName();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "Nachricht [id=" + id + ", nachricht=" + nachricht + ", senderID=" + senderID + ", receiverName=" + receiverName + ",senderName=" + senderName + ", receiverID="
                + receiverID + ", date=" + date + "]";
    }
}

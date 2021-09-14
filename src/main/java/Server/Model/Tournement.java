package Server.Model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "toernement")
public class Tournement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "creatorID")
    int creatorID;
    @Column(name = "name")
    String name;
    @Column(name = "fee")
    int fee;
    @Column(name = "maxAnzahl")
    int maxAnzahl;
    @Column(name = "knockOut")
    boolean knockOut;
    @Column(name = "pending")
    boolean pending;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<Integer> contestantID = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<Integer> matchIDs = new ArrayList<>();

    public Tournement() {
    }

    public Tournement(int creatorID,String name,boolean knockOut, int fee,int maxAnzahl) {

        this.name = name;
        this.creatorID = creatorID;
        this.knockOut = knockOut;
        this.fee = fee;
        this.maxAnzahl = maxAnzahl;
    }

    public Tournement(String name, int fee, int maxAnzahl, boolean knockOut) {

        this.name = name;
        this.fee = fee;
        this.maxAnzahl = maxAnzahl;
        this.knockOut = knockOut;
    }

    public void addContestant(int i){
        contestantID.add(i);
    }

    public void addMatch(int i){
        matchIDs.add(i);
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getType(){

        if(isKnockOut()){
            return "Knock-Out";
        }else{
            return "Liga";
        }
    }

    public boolean isKnockOut() {
        return knockOut;
    }

    public void setKnockOut(boolean knockOut) {
        this.knockOut = knockOut;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getMaxAnzahl() {
        return maxAnzahl;
    }

    public void setMaxAnzahl(int maxAnzahl) {
        this.maxAnzahl = maxAnzahl;
    }

    public List<Integer> getMatchIDs() {
        return matchIDs;
    }

    public void setMatchIDs(List<Integer> matchIDs) {
        this.matchIDs = matchIDs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public List<Integer> getContestantID() {
        return contestantID;
    }

    public void setContestantID(List<Integer> contestantID) {
        this.contestantID = contestantID;
    }

    @Override
    public String toString() {

        return "Tournement{" +
                "id=" + id +
                ", creatorID=" + creatorID +
                ", name='" + name + '\'' +
                ", fee=" + fee +
                ", maxAnzahl=" + maxAnzahl +
                ", knockOut=" + knockOut +
                ", pending=" + pending +
                ", contestantID=" + contestantID +
                ", matchIDs=" + matchIDs +
                '}';
    }
}

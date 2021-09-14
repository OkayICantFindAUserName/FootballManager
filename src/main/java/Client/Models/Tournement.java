package Client.Models;


import java.util.ArrayList;
import java.util.List;

public class Tournement {

    int id;
    int creatorID;
    String name;
    int fee;
    int maxAnzahl;
    boolean knockOut;
    boolean pending;

    List<Integer> contestantID = new ArrayList<>();
    List<Integer> matchIDs = new ArrayList<>();

    public Tournement() {
    }

    public Tournement(int creatorID,String name,boolean knockOut, int fee) {

        this.name = name;
        this.creatorID = creatorID;
        this.knockOut = knockOut;
        this.fee = fee;
    }

    public Tournement(String name, int fee, int maxAnzahl, boolean knockOut) {

        this.name = name;
        this.fee = fee;
        this.maxAnzahl = maxAnzahl;
        this.knockOut = knockOut;
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


    public int getAnzahl(){
        return contestantID.size();
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

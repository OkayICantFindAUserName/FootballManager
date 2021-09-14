package Client.Models;

import java.util.Objects;

public class Match {

    int id;
    String result;
    int senderUserName;
    int receiverUserName;
    boolean pending;

    public Match() {
    }

    public Match(int senderUserName, int receiverUserName) {
        this.senderUserName = senderUserName;
        this.receiverUserName = receiverUserName;
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

    public int getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(int homeTeamUserID) {
        this.senderUserName = homeTeamUserID;
    }

    public int getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(int awayTeamUserID) {
        this.receiverUserName = awayTeamUserID;
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
                ", senderUserID=" + senderUserName +
                ", receiverUserID=" + receiverUserName +
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
                Objects.equals(getSenderUserName(), match.getSenderUserName()) &&
                Objects.equals(getReceiverUserName(), match.getReceiverUserName());
    }
}

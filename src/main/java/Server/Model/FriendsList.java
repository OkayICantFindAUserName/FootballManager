package Server.Model;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "FRIENDSLIST")
public class FriendsList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "currentUserId")
    int currentUserId;
    @Column(name = "userFriendId")
    int userFriendId;
    @Column(name = "PENDING")
    boolean pending;

    public FriendsList(int currentUserId, int userFriendId, boolean pending) {
        this.currentUserId = currentUserId;
        this.userFriendId = userFriendId;
        this.pending = pending;
    }

    public FriendsList(int currentUserId, int userFriendId) {
        this.currentUserId = currentUserId;
        this.userFriendId = userFriendId;
    }

    public FriendsList() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public int getUserFriendId() {
        return userFriendId;
    }

    public void setUserFriendId(int userFriendId) {
        this.userFriendId = userFriendId;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    @Override
    public String toString() {
        return "FriendsList{" +
                "id=" + id +
                ", currentUserId=" + currentUserId +
                ", userFriendId=" + userFriendId +
                ", pending=" + pending +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendsList that = (FriendsList) o;
        return currentUserId == that.currentUserId &&
                userFriendId == that.userFriendId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currentUserId, userFriendId, pending);
    }

    public void update(boolean b) {
        this.pending = b;
    }
}

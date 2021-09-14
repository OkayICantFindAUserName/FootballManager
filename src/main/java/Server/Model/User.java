package Server.Model;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "username")
    String userName;
    @Column(name = "passwort")
    String passwort;
    @Column(name = "email")
    String eMail;
    @Column(name = "SEPS")
    int sEPS;
    @Column(name = "teamName")
    String teamName;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passwort='" + passwort + '\'' +
                ", eMail='" + eMail + '\'' +
                ", sEPS=" + sEPS +
                ", teamName='" + teamName + '\'' +
                '}';
    }

    public User(int id, String userName, String passwort, String eMail, int sEPS, int lootboxen, String lineup, String teamName) {
        this.id = id;
        this.userName = userName;
        this.passwort = passwort;
        this.eMail = eMail;
        this.sEPS = sEPS;
        this.teamName = teamName;
    }

    public User(int id, String eMail, int sEPS, int lootboxen, String lineup) {
        this.id = id;
        this.eMail = eMail;
        this.sEPS = sEPS;
    }


    public User(){
    }

    public User(String name, String passwort) {
        this.userName = name;
        this.passwort = passwort;
    }

    public User(String userName, String passwort, String eMail) {
        this.id = id;
        this.userName = userName;
        this.passwort = passwort;
        this.eMail = eMail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public int getsEPS() {
        return sEPS;
    }

    public void setsEPS(int sEPS) {
        this.sEPS = sEPS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                userName.equals(user.userName) &&
                passwort.equals(user.passwort) &&
                eMail.equals(user.eMail);
    }


    public void update(User newUser, boolean Lootboxen) {
        if(newUser.getUserName() != null){
            setUserName(newUser.getUserName());
        }
        if(newUser.getPasswort() != null) {
            setPasswort(newUser.getPasswort());
        }
        if(newUser.geteMail() != null){
            seteMail(newUser.geteMail());
        }
        if(newUser.getTeamName() != null){
            setTeamName(newUser.getTeamName());
        }
        if(Lootboxen){
        if(newUser.getsEPS() >= 0){
            setsEPS(newUser.getsEPS());
        }}
    }

}

package Client.Models;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class User {

    int id;
    String userName;
    String passwort;
    String eMail;
    int sEPS;
    String teamName;

    public User(){
    }


    public User(String userName, String passwort, String eMail) {
        this.id = id;
        this.userName = userName;
        this.passwort = passwort;
        this.eMail = eMail;
    }

    public User(String userName, String passwort, String eMail,String line_up) {
        this.id = id;
        this.userName = userName;
        this.passwort = passwort;
        this.eMail = eMail;
        this.teamName = line_up;

    }

    public User(String name, String passwort) {
        this.userName = name;
        this.passwort = passwort;
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

    public StringProperty userNameProperty() {
        return new SimpleStringProperty(userName);
    }

    public StringProperty eMailProperty() {
        return new SimpleStringProperty(eMail);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passwort='" + passwort + '\'' +
                ", eMail='" + eMail + '\'' +
                ", sEPS=" + sEPS +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return this.passwort.equals(user.passwort) &&
                this.userName.equals((user.userName)) &&
                this.eMail.equals((user.eMail));

    }


}

package Client.Models;

import java.util.Objects;

public class Assets {
    int id;
    int userid;
    String name;
    int kapazitaet;
    int parkplatz;
    int imbiss;


    public Assets() {
    }

    public Assets(int userid,String name) {

        this.userid = userid;
        this.name = name;
        this.kapazitaet = 0;
        this.parkplatz = 0;
        this.imbiss = 0;

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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
        name = name;
    }

    public int getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(int kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    public int getParkplatz() {
        return parkplatz;
    }

    public void setParkplatz(int parkplatz) {
        this.parkplatz = parkplatz;
    }

    public int getImbiss() {
        return imbiss;
    }

    public void setImbiss(int imbiss) {
        this.imbiss = imbiss;
    }

    @Override
    public String toString() {
        return "Assets{" +
                "id=" + id +
                ", userid=" + userid +
                ", Name='" + name + '\'' +
                ", kapazitaet=" + kapazitaet +
                ", parkplatz=" + parkplatz +
                ", imbiss=" + imbiss +
                '}';
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getUserid(), getName(), getKapazitaet(), getParkplatz(), getImbiss());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assets assets = (Assets) o;
        return getId() == assets.getId() &&
                getUserid() == assets.getUserid() &&
                getKapazitaet() == assets.getKapazitaet() &&
                getParkplatz() == assets.getParkplatz() &&
                getImbiss() == assets.getImbiss() &&
                Objects.equals(getName(), assets.getName());
    }

    public boolean upgrade(int asset) {

        switch(asset){
            case 0:
                if(getImbiss() == 3){
                    return false;
                }else{
                    setImbiss(getImbiss() + 1);
                    break;
                }
            case 1:
                if(getParkplatz() == 3){
                    return false;
                }else{
                    setParkplatz(getParkplatz() + 1);
                    break;
                }
            case 2:
                if(getKapazitaet() == 3){
                    return false;
                }else{
                    setKapazitaet(getKapazitaet() + 1);
                    break;
                }
        }
        return true;
    }
}

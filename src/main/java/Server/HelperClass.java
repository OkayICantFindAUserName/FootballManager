package Server;

public class HelperClass {

    String Name;
    String teamName;
    int tore;
    int Punkte;
    int Platz;

    public HelperClass() {
    }

    public int getTore() {
        return tore;
    }

    public void setTore(int tore) {
        this.tore = tore;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPunkte() {
        return Punkte;
    }

    public void setPunkte(int punkte) {
        Punkte = punkte;
    }

    public int getPlatz() {
        return Platz;
    }

    public void setPlatz(int platz) {
        Platz = platz;
    }

    @Override
    public String toString() {
        return "HelperClass{" +
                "Name='" + Name + '\'' +
                ", teamName='" + teamName + '\'' +
                ", tore=" + tore +
                ", Punkte=" + Punkte +
                ", Platz=" + Platz +
                '}';
    }
}

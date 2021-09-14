package Server;

public class MatchWrapper {

    String Name;
    String teamName;
    String result;
    boolean win;

    public MatchWrapper() {
    }

    public MatchWrapper(String name, String teamName, String result) {
        Name = name;
        this.teamName = teamName;
        this.result = result;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getGoals() {

        String[] split = result.split(":");
        if(split[1].equals(split[3])){
            return "Unentschieden : " + split[1] + " : " + split[3];
        }else if(isWin()){
            return "Gewonnen : " + split[1] + " : " + split[3];
        }else{
            return "Verloren : " + split[1] + " : " + split[3];
        }
    }

    @Override
    public String toString() {

        return "MatchWrapper{" +
                "Name='" + Name + '\'' +
                ", TeamName='" + teamName + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}

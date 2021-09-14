package Client.Models;


import Client.Main;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.net.*;
import java.io.*;


public class Client_Socket {

    private String ipv4_adresse = "127.0.0.1";        //standardmaessige Variablen
    private int portnummer = 8000;                    // knnen nur ber Getter und Setter beeinflusst werden
    private String request;
    private boolean go = true;
    private String data;    // uebertragende DAten abgreifen
    private Socket client;
    private DataOutputStream outgoing;
    private DataInputStream incoming;

    public Client_Socket(String ipv4_adresse, int portnummer) {    // Konstruktor um ein Objekt mit anderer ipv4 Adresse oder Portnummer zu erstellen
        this.ipv4_adresse = ipv4_adresse;
        this.portnummer = portnummer;
        //Es wird ein Socket angelegt, welcher versucht mit
        //angegebener ip und portnummer eine Verbindung auzubauen
        // Der Socket dient also dazu um eine Verbindung aufzubauen

    }

    public void connect() {
        try {
            this.client = new Socket(this.ipv4_adresse, this.portnummer);
        } catch (Exception e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Verbindungsabbruch");
                    alert.setContentText("Sie haben die Verbindung zum Server verloren bitte veruschen Sie sich erneut anzumelden.");
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.show();
                }
            });
            this.go = false;
            return;
        }
        System.out.println("Verbindung mit dem Server ist hergestellt!");
        this.go = true;
        return;
    }

    public void senden_empfangen() {
        if (this.go) {    //Falls der Befehl falsch ist, wird go auf false gesetzt und die Methode beendet bzw. nicth ausgefuehrt
            try {

                this.outgoing = new DataOutputStream(client.getOutputStream());  //Der Outputstream dient dazu DAten zu senden

                try {
                    this.outgoing.writeUTF(getRequest());    // Hier wird in dem Outputstream reingeschrieben, also die Nachricht
                } catch (Exception e) {
                    this.go = false;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Main.setRoot("LoginFenster");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Verbindungsabbruch");
                            alert.setContentText("Sie haben die Verbindung zum Server verloren bitte veruschen Sie sich erneut anzumelden.");
                            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            alert.show();
                        }
                    });
                    return;
                }
                this.outgoing.flush(); // Mit dieser Methode wird erreicht, dass der komplette Buffer geleert wird und wirklich alles gesendet wird
                System.out.println("Befehl wurde gesendet");

               /* this.incoming = new DataInputStream(client.getInputStream()); // Mit DataInputstream werden Daten empfangen
                this.data = this.incoming.readUTF();
                System.out.println("Daten empfangen, tschuess");*/


                DataInputStream in = new DataInputStream(client.getInputStream());
                int len = in.readInt();
                byte[] buff = new byte[len];
                in.readFully(buff);
                this.data = new String(buff, "UTF-8");

                //System.out.println(incoming.readUTF()); // Hiermit lesen wir den Stream und holen uns die Daten raus

            } catch (Exception client) {    //falls irgendetwas schief geht, beim Aufbau, Senden, Empfangen oder Abbau, dann werden die Exceptions abgefangen und ausgegeben
                System.out.println("Ein Fehler ist aufgetreten: " + client.getMessage()); //Schlechtes Exception Handling
                client.printStackTrace();
                Main.setRoot("LoginFenster");
            }


        } else {
            connect();
            if (this.go) {
                this.senden_empfangen();
            }
        }

    }
    /*
    public void stop_connection() {
        try {
            this.outgoing.close();
            this.incoming.close(); // Inputstream wird geschlossen
            this.client.close(); // Socket wird geschlossen, also die Kommunikation unterbrochen
        } catch (IOException e) {
            e.printStackTrace();
        } //Outputstream wird geschlossen

        System.out.println("Verbindung mit Server getrennt");
        System.out.println();
    }*/


    public void setRequest(String request) {
                                                            //Hiermit wird die Requestnachricht festgelegt, es wird berprft, ob berhaupt der
        String[] strings = request.split("/");
        if (strings[0].equals("GETUSER") ||                // Befehl gueltig ist. Wenn ja dann kan auch die start-Methode ausgefuehrt werden.
                strings[0].equals("POSTUSER") ||            // Strings sind Referenzvariablen, deswegen equals zum vergleichen
                strings[0].equals("GETPLAYER") ||            // Strings sind Referenzvariablen, deswegen equals zum vergleichen
                strings[0].equals("UPDATEUSER") ||
                strings[0].equals("GETSEPS") ||
                strings[0].equals("SENDFRIENDINVITE") ||
                strings[0].equals("CHECKFORPENDINGFRIENDS") ||
                strings[0].equals("CHECKFORNONPENDINGFRIENDS") ||
                strings[0].equals("ACCEPTFRIEND") ||
                strings[0].equals("LOGOUT") ||
                strings[0].equals("DECLINEFRIEND") ||
                strings[0].equals("REQUESTEDFRIENDS") ||
                strings[0].equals("GETTEAM") ||
                strings[0].equals("OPENLOOTBOX") ||
                strings[0].equals("OPENSTARTLOOTBOX") ||
                strings[0].equals("ADDPLAYERTOSTARTELF") ||
                strings[0].equals("GETSTARTELF") ||
                strings[0].equals("GETFRIENDSTARTELF") ||
                strings[0].equals("GETTEAMNAMEN") ||
                strings[0].equals("GETFRIENDTEAMNAMEN") ||
                strings[0].equals("SELLPLAYER") ||
                strings[0].equals("GETMATCHREQUEST") ||
                strings[0].equals("GETMATCHHISTORY") ||
                strings[0].equals("GETFRIENDMATCHHISTORY") ||
                strings[0].equals("REQUESTMATCH") ||
                strings[0].equals("ACCEPTMATCH") ||
                strings[0].equals("GETREQUESTEDMATCHES") ||
                strings[0].equals("DECLINEMATCHREQUEST") ||
                strings[0].equals("GETASSET") ||
                strings[0].equals("GETALLTOURNEMENTS") ||
                strings[0].equals("ADDUSERTOTOURNEMENT") ||
                strings[0].equals("GETALLPENDINGTOURNEMENTS") ||
                strings[0].equals("GETALLNONPENDINGTOURNEMENTS") ||
                strings[0].equals("CREATENEWTOURNEMENT") ||
                strings[0].equals("GETALLCREATEDTOURNEMENTS") ||
                strings[0].equals("GETMATCHFROMT") ||
                strings[0].equals("UPGRADEASSET") ||
                strings[0].equals("UPDATEASSET") ||
                strings[0].equals("SENDMESSAGE") ||
                strings[0].equals("GETMESSAGE") ||
                strings[0].equals("GETSENTMESSAGE") ||
                strings[0].equals("REMOVEFRIEND")) {

            this.request = request;

        } else {
            System.out.println("Befehl nicht erkannt: " + request);
            this.go = false;
        }
    }

    public String getData() {
        return this.data;
    }

    // getter und Setter um im ipadressen, portnummer und request typ zu setzen bzw. auszulesen
    /*
    public String getIP() {
        return this.ipv4_adresse;
    }

    public void setIP(String ip) {
        this.ipv4_adresse = ip;
    }


    public int getPort() {
        return this.portnummer;
    }

    public void setPort(int port) {
        this.portnummer = port;
    }
*/
    public String getRequest() {
        return this.request;
    }

    public Socket getClient() {
        return this.client;
    }

}

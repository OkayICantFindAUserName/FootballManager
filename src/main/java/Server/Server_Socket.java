package Server;



import java.io.IOException;
import java.net.*;

public class Server_Socket implements Runnable {
	
	private int portnummer = 8000;		//standard Varirablen. Nur Portnummer kann geaendert werden
	private ServerSocket server;		// ServerSocket,welcher auf Verbindungsanfragen wartet auf dem Port. Dient dazu den Client zu merken, aber hört nicht auf Nachrichten
	private Socket client;				// Socket um DataOutput und Datainput zu haben. Mit client verbinden und auf Nachrichten hören
	private boolean go = true;
	public Server_Socket() {	// Klassenkonstruktor
		
	}
	
	public Server_Socket(int portnummer) {	// Konstruktor um ein Objekt mit anderer Portnummer zu erstellen
		this.portnummer = portnummer;
	}										
	
	public void run() {		//start MEthode um den Server zu starten
	
	//Endlossschleife, damit der Server ständig auf Verbindungen hoert
		
				
			try {
				
			this.server = new ServerSocket(this.portnummer); // Es wird ein neuer Serversocket erstellt der den angegeben Port an sich bindet
		
			System.out.println("Portnummer des Servers ist: "+this.portnummer);
			
			while(go) {
			synchronized(server) {
			client = server.accept(); // der client Socket aktzeptiert die Verbindungen zum verbundenen client
			new Server_Socket_Handler(client).start(); //Thread für jeden Client wird gestartet
			
			}
			}
			
			}catch(Exception e) {
				
			}
			
			
  //Sollten Fehler auftreten, so werden diese hier abgefangen. Schlechtes Exception Handling
			

			
//		}finally {  //Aufjedenfall soll server socket und client socket geschlossen werden
//			try {
//				server.close();
//				//client.close();
//			}catch(Exception server_client) { // Es könnten auch hierbei Fehler auftreten udn die werden abgefangen. Schlechtes Exception Handling
//				System.out.println("Ein Fehler ist aufgetreten: " + server_client.getMessage());
//				server_client.printStackTrace();
//			}

			System.out.println("Server wurde beendet");
	
	}

	
	
//	public static void main(String[]args) {
//		Server_Socket neu = new Server_Socket(6667);
//		neu.laufen();
//	}
//	
	// Getter und Setter um den Port zu ändern bzw. abzufragen
	
	public void setPort(int portnummer) {
		this.portnummer = portnummer;
	}
	
	public int getPort() {
		return this.portnummer;
	}
	
	public void setGo(boolean go) {
		this.go = go;
		
	}
	
	public void stop() throws IOException {
		this.server.close();
	}
	
}

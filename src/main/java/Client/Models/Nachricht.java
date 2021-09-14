package Client.Models;


import java.sql.Date;


public class Nachricht {
    private int id;
    private String nachricht;
    private int senderID;
    private int receiverID;
    private Date date;
    
    private String senderName;
    private String receiverName;

    public Nachricht() {
    	
    }
    
	public String getNachricht() {
		return nachricht;
	}
	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}
	public int getSenderID() {
		return senderID;
	}
	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	public int getReceiverID() {
		return receiverID;
	}
	public void setReceiverID(int receiverID) {
		this.receiverID = receiverID;
	}
	public String getSenderName(){ return senderName; }
	public void setSenderName(String senderName){ this.senderName = senderName; }
	public String getReceiverName(){ return receiverName; }
	public void setReceiverName(String receiverName){ this.receiverName = receiverName; }
	public String getDate() { return date.toString(); }
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Nachricht [id=" + id + ", nachricht=" + nachricht + ", senderID=" + senderID + ", receiverID="
				+ receiverID + ", date=" + date + ", senderName=" + senderName + ", receiverName=" + receiverName + "]";
	}

	
    
}

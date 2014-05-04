package if3230.tubes1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/** 
 * Protokol Client-Server
 * @author Kelompok10
 *
 */
public class Protocol {
	private Socket So;

	//constructor
	
	public Protocol() {
		this.So = null;
	}

	public Protocol(Socket so) {
		this.So = so;
	}
	
	
	//send and receive
	 
	public void send(String S) {
		try {
		   DataOutputStream outStream =  new DataOutputStream(So.getOutputStream());
		   outStream.writeUTF(S);		
		}	
		catch (Exception e) {
		   System.out.println(e.getMessage());
		}
	}

	public String receive() {
		try {
			DataInputStream inStream = new DataInputStream(So.getInputStream());
			return inStream.readUTF();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		//jika terjadi Exception
		return "NULL";
	}
	
	//mengirim sebuah ArrayList of String yang dipecah menjadi beberapa pengiriman String
	public void sendRepeatMessage(ArrayList<String> Messages) {
		this.send("REPEAT");
		if (Messages != null) {
			for (int i = 0; i < Messages.size(); i++) {
				this.send(Messages.get(i));
			}
		}
		this.send("DONE");
	}
	
	//melakukan receive() terus menerus hingga perintah DONE dimasukkan
	public ArrayList<String> repeatedReceive() {
		ArrayList<String> Messages = new ArrayList<String>();	
		String msg = this.receive();
		//terus melakukan receive() hingga pesan berupa perintah "DONE"
		while (!msg.equals("DONE")) {
			Messages.add(msg);
			msg = this.receive();
		}
		return Messages;
	}
}

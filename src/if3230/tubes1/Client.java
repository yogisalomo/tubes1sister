package if3230.tubes1;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/** 
 * Representasi Client
 * @author Kelompok10
 *
 */

public class Client {
	private Protocol P;
	
	public Client(String S) {
		try {
			String command="";
			
			//looking for available connection to server
			int port_number = Protocol.getAvailablePortNumber(S,Protocol.MIN_SERVER2CLIENT_PORTNUMBER,Protocol.MAX_SERVER2CLIENT_PORTNUMBER,0);
			if(port_number > Protocol.MAX_SERVER2CLIENT_PORTNUMBER){
				System.out.println("No port available");
				return;
			}
			
			Socket client_socket = new Socket(S, port_number);
			System.out.println("Terhubung ke server dengan address: " + client_socket.getRemoteSocketAddress());

			P = new Protocol(client_socket);
			Scanner reader = new Scanner(System.in);
			
			//antarmuka untuk client
			while (!command.equals("quit")) {
				System.out.print("SetanGundul> "); command = reader.nextLine();
				P.send(command);
				String response = P.receive();
				if (response.equals("REPEAT")) {
					//melakukan repeatedReceive()
					ArrayList<String> resp = P.repeatedReceive();
					for (int i = 0; i < resp.size(); i++) {
						System.out.println(resp.get(i));
					}
					System.out.println(P.receive());
				}else System.out.println(response);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}

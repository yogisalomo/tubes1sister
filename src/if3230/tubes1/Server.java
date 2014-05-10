package if3230.tubes1;

import java.net.*;
import java.io.*;
import java.util.*;

/** 
 * Representasi Server
 * @author Kelompok10
 *
 */

public class Server extends Thread {
	private ServerSocket server_Socket;
	private ServerSocket server_Socket_toServer;
	private Protocol P;
	private DBStructure dbStruct;

	//two port number, one for server, the other for client
	int to_client_port;
	int to_server_port;
	
	//constructor
	
	public Server(int Port) {
		dbStruct = new DBStructure();
		try {
			/* client-server port assignment */
			
			server_Socket = new ServerSocket(Port);
			//port yang didapat menjadi port number ke client
			to_client_port = server_Socket.getLocalPort();
			
			/* server-server port assignment */
			int port_number;
			//get random port number between Protocol.MIN_SERVER2CLIENT_PORTNUMBER and Protocol.MAX_SERVER2CLIENT_PORTNUMBER
			port_number = (int)(Math.random() * (Protocol.MAX_SERVER2SERVER_PORTNUMBER - Protocol.MIN_SERVER2SERVER_PORTNUMBER + 1));
			while(Protocol.isPortInUse(InetAddress.getLocalHost().getHostAddress(), port_number) == true){
				port_number = (int)(Math.random() * (Protocol.MAX_SERVER2SERVER_PORTNUMBER - Protocol.MIN_SERVER2SERVER_PORTNUMBER + 1));
			}
			server_Socket_toServer = new ServerSocket(Protocol.MIN_SERVER2SERVER_PORTNUMBER + port_number);
			//port yang didapat menjadi port number ke server lain
			to_server_port = server_Socket_toServer.getLocalPort();
			System.out.println("(will) Listening on server2server port " + server_Socket_toServer.getLocalPort() + "...");
			
			//looking for available connection to ANOTHER server
			port_number = Protocol.getAvailablePortNumber(InetAddress.getLocalHost().getHostAddress(),Protocol.MIN_SERVER2SERVER_PORTNUMBER,Protocol.MAX_SERVER2SERVER_PORTNUMBER,to_server_port);
			if(port_number > Protocol.MAX_SERVER2SERVER_PORTNUMBER){
				System.out.println("All port numbers are available. This is the FIRST server in THIS ADDRESS");
				return;
			}
			else{
				System.out.println("there is a server listening to port " + port_number);
				//TODO tambahin update server baru
			}
		}
		catch (Exception e){
			System.out.println("coy, ada exception");
			e.printStackTrace();
		}
	}	

	public void run() {
		while(true) 
		{			
			try {
				String recv_commands="";

				System.out.println("Listening on client-server port " +server_Socket.getLocalPort() + "...");
				//menerima koneksi yang dibuat ke server_Socket
				Socket server = server_Socket.accept();
				P = new Protocol(server);
				System.out.println("Connected with "+ server.getRemoteSocketAddress());
				
				//dilakukan hingga perintah "quit" dimasukkan
				while (!recv_commands.equals("quit")) {
					recv_commands = P.receive();
					if (recv_commands.equals("quit")){
						P.send("Server quits");
					}
					else{
						P.send(operateDatabase(recv_commands,P));
					}
					System.out.println("Isi recv_commands : "+recv_commands);
	
					if (recv_commands.equals("NULL")){
						break;
					}
				}	

				//menutup Socket
				server.close();
				System.out.println("Disconnected with "+ server.getRemoteSocketAddress());

			}
			catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			}catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (Exception e) {
				System.out.println("Exception");
				e.printStackTrace();
			}
		}
	}

	private String operateDatabase(String command, Protocol P) {
		ArrayList<String> commands = new ArrayList<String>();
		System.out.println("Client> "+command);
		//membaca command
		for (String retval: command.split(" ",4)){ //nilai limit 4, sesuai jumlah argumen maksimal
			commands.add(retval);
		}
		
		//command handling
		
		//create table <nama table>
		if (commands.get(0).equals("create")) {
			if (commands.size() != 3){
				return "FALSE-COMMAND (usage: \"create table <table_name>\")"; //command tidak sesuai
			}
			else {
				if (dbStruct.createTable(commands.get(2)) == true){
					return "OK"; //berhasil
				}
				else{
					return "FALSE-EXISTS (table already exists)"; //sudah exists
				}
			}
		}
		//insert <nama table> <key> <value>
		else if (commands.get(0).equals("insert")) {
			if (commands.size() != 4){
				return "FALSE-COMMAND (usage: \"insert <table_name> <key> <value>\")"; //command tidak sesuai
			}
			else {
				if (dbStruct.insertData(commands.get(1),commands.get(2),commands.get(3))){
					return "OK"; //OK
				}
				else{
					return "FALSE-NO-TABLE/KEY-NOT-VALID"; //tidak ada tabel atau key invalid ( > 0)
				}
			}		
		}
		//display <nama table>
		else if (commands.get(0).equals("display")) {
			if (commands.size() != 2){
				return "FALSE-COMMAND (usage: \"display <table_name>\")"; //salah command
			}
			else{
				//getAllDataFromTableStr, ambil data dari tabel tsb.
				P.sendRepeatMessage(dbStruct.getAllDataFromTableStr(commands.get(1),false));
				return "OK";
			}
		}
		//display_all, untuk melihat semua data, termasuk Data yang invisible
		else if (commands.get(0).equals("display_all")) {
			if (commands.size() != 2) return "FALSE-COMMAND (usage: \"display_all <table_name>\")"; //salah command
			else{
				P.sendRepeatMessage(dbStruct.getAllDataFromTableStr(commands.get(1),true));
				return "OK";
			} //ambil data dari tabel
		}
		else return "FALSE";
	}
}

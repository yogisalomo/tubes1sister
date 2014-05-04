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
	private Protocol P;
	private DBStructure dbStruct;

	//constructor
	
	public Server() {
		try {
			server_Socket = new ServerSocket(2014);
			dbStruct = new DBStructure();
		}catch (Exception e){
			e.printStackTrace();
		}
	}	

	public void run() {
		while(true) 
		{
			try {
				String recv_commands="";

				System.out.println("Listening on port " +server_Socket.getLocalPort() + "...");
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
				return "FALSE-COMMAND"; //command tidak sesuai
			}
			else {
				if (dbStruct.createTable(commands.get(2))){
					return "OK"; //berhasil
				}
				else{
					return "FALSE-EXISTS"; //sudah exists
				}
			}
		}
		//insert <nama table> <key> <value>
		else if (commands.get(0).equals("insert")) {
			if (commands.size() != 4){
				return "FALSE-COMMAND"; //command tidak sesuai
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
				return "FALSE-COMMAND"; //salah command
			}
			else{
				//getAllDataFromTableStr, ambil data dari tabel tsb.
				P.sendRepeatMessage(dbStruct.getAllDataFromTableStr(commands.get(1),false));
				return "OK";
			}
		}
		//display_all, untuk melihat semua data, termasuk Data yang invisible
		else if (commands.get(0).equals("display_all")) {
			if (commands.size() != 2) return "FALSE-COMMAND"; //salah command
			else{
				P.sendRepeatMessage(dbStruct.getAllDataFromTableStr(commands.get(1),true));
				return "OK";
			} //ambil data dari tabel
		}
		else return "FALSE";
	}
}

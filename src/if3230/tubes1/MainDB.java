package if3230.tubes1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class MainDB {
	public static void printTest(ArrayList<Data> D) {
		Iterator<Data> it = D.iterator();
		while(it.hasNext())
		{
		    Data obj = it.next();
		    System.out.println(obj.getData());
		}
	}
	
	public static void main(String[] args){
		
		Scanner reader = new Scanner(System.in);
		System.out.println("1. Server, 2. Client");
		System.out.print("Type 1 for Server. Type 2 for Client : "); int a = reader.nextInt();
		if (a == 1) {
//			System.out.println("Server Port : "); int Port = reader.nextInt();
//			Server S = new Server(Port);
//			S.start();
//			
			//coba
			System.out.println("Server Port : "); int Port = reader.nextInt();
			ServerBaru S = new ServerBaru(Port);
//			S.stoc.start();
//			S.stos.start();
//			S.stotr.start();
			
		} else if (a == 2) {
			reader.nextLine();
			System.out.println("Target IP : "); String IP = reader.nextLine();
			//reader.nextLine();
			System.out.println("Target Port : "); int Port = reader.nextInt();
//			reader.nextLine();
			Client C = new Client(IP,Port);
		}
	}
}
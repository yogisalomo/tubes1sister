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
			Server S = new Server();
			S.start();
		} else if (a == 2) {
			reader.nextLine();
			System.out.println("IP Target : "); String IP = reader.nextLine();
			Client C = new Client(IP);
		}
	}
}
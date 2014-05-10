package if3230.tubes1;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Database yang terdiri dari tabel-tabel
 * @author Kelompok10
 *
 */

public class DBStructure {	
	private Hashtable<String,ArrayList<Data>> database; //key merupakan table, dan value merupakan Data (lihat class Data) 

	//constructor
	
	public DBStructure() {
		database  = new Hashtable<String,ArrayList<Data>>();
	}
	
	
	//method

	public boolean createTable(String table_name){
		//cek apakah tabel sudah pernah dibuat atau belum
		if (database.get(table_name) == null) {
			ArrayList<Data> D = new ArrayList<Data>();
			//inisialisasi isi table dengan Data<0,0,0>
			D.add(new Data("0","0","0"));
			database.put(table_name,new ArrayList<Data>());
			database.put(table_name,D);
			return true;
		}
		else return false;
	}

	public boolean insertData(String table_name, String Key, String Value) {
		ArrayList<Data> D = database.get(table_name);
		if (Key.equals("0")) return false;
		else {
			//jika tabel dengan nama table_name belum ada
			if (D == null) return false;
			else {
				//jika key sama, update data dengan timestamp terbaru
				updateExistedKey(table_name,Key);
				D.add(new Data(Key,Value));
				database.put(table_name,D);
				return true;
			}
		}
	}

	private void updateExistedKey(String table,String Key) {
		ArrayList<Data> table_contents = getAllDataFromTable(table);
		if (table_contents != null){
			for (int i = 0; i < table_contents.size(); i++) {
				//semua data dengan key Key menjadi invisible
				if (table_contents.get(i).Key.equals(Key)) table_contents.get(i).setInvisible();
			}
		}
	}
	
	//mengembalikan "isi tabel" berupa value dari key yang bernilai table
	public ArrayList<Data> getAllDataFromTable(String table) {
		//System.out.println("Contents : "+database.get(table));
		return database.get(table);
	}
	
	//representasi string dari getAllDataFromTable()
	public ArrayList<String> getAllDataFromTableStr(String table, boolean print_all) {
		ArrayList<Data> table_contents = getAllDataFromTable(table);
		if (table_contents == null) return null;
		else {
			ArrayList<String> result = new ArrayList<String>();
			for (int i = 0; i < table_contents.size(); i++) {
				//menentukan apakah seluruh isi tabel akan ditampilkan atau tidak
				if (print_all == true){
					result.add(table_contents.get(i).getData());
				}
				else {
					//hanya data yang visible saja yang ditampilkan
					if (table_contents.get(i).isVisible) {
						result.add(table_contents.get(i).getData());
					}
				}
			}
			return result;
		}
	}
}

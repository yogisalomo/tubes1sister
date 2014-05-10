package if3230.tubes1;

import java.sql.Timestamp;

/**
 * Representasi data pada sebuah tabel
 * @author Kelompok10
 *
 */

public class Data {
	public String Key;
	public String Value;
	public String TimeStamp;
	public boolean isVisible; //bernilai true jika data ditampilkan 
	
	//constructor
	
	public Data(String Key, String Value) {
		this.Key = Key;
		this.Value = Value;
		this.TimeStamp = this.currentTimeStamp();
		this.isVisible = true;
	}

	public Data(String Key, String Value, String tm) {
		this.Key = Key;
		this.Value = Value;
		this.TimeStamp = tm;
		this.isVisible = false;
	}

	
	//method
	
	//mengembalikan timestamp berupa nilai waktu saat ini
	private String currentTimeStamp() {
		java.util.Date date= new java.util.Date();
		return (new Timestamp(date.getTime())).toString();
	}
	
	public void setInvisible(){
		this.isVisible = false;
	}
	
	public String getData(){
		return "<"+Key+","+Value+","+TimeStamp+">";
	}
}


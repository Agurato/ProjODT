package controller;

import model.*;
//import view.*;

public class Test {
	public static void main(String []args) {
<<<<<<< HEAD
		DataBase db = new DataBase("/home/vincent/Documents/odt");
		db.sync();
=======
		Database db = new Database("/home/vincent/Documents/odt");
		
		try {
			for(ODTFile file : db.getOdtFiles(db.getRoot().getAbsolutePath())) {
				file.unzipODT();
				file.parseContentXML();
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
>>>>>>> 07c5efef2f41de8855188e28a4fa2140d8e89520
	}
}

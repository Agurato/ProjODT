package controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.*;
//import view.*;

public class Test {
	public static void main(String []args) {
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
	}
}

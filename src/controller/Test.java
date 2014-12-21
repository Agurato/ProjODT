package controller;

import java.util.ArrayList;

import model.*;
//import view.*;

public class Test {
	public static void main(String []args) {
		DataBase db = new DataBase("/home/vincent/Documents/odt");
		ArrayList<String> research = new ArrayList<String>();
		research.add("presentation");
		research.add("Partie");
		
//		db.sync();
		db.search(research, "or");
	}
}
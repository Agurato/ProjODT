package controller;

import java.util.ArrayList;

import model.*;
//import view.*;

public class Test {
	public static void main(String []args) {
		DataBase db = new DataBase("/home/vincent/Documents/odt");
		String research = "contraintes conclusion";
		
//		db.sync();
		db.search(research);
	}
}
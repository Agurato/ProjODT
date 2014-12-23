package controller;

import java.util.ArrayList;

import model.*;
//import view.*;

public class Console {
	public static void main(String []args) {
		DataBase db = null;
		ArrayList<String> research;
		
		for(int i=0 ; i<args.length-1 ; i++) {
			switch(args[i]) {
			case "-d" :
				db = new DataBase(args[i+1]);
				i++;
				
				break;
			case "-f" :
				db = new DataBase();
				db.addOdt(new ODTFile(args[i+1]));
				i++;
				
				break;
			case "-w" :
				if(db != null) {
					db.search(args[i+1]);
				}
				else {
					System.out.println("DataBase not initialized !");
				}
				i++;
				
				break;
			default :
				
				break;
			}
		}
	}
}
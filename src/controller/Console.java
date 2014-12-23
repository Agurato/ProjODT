package controller;

import model.*;
//import view.*;

public class Console {
	public static void main(String []args) {
		DataBase db = null;
		
		for(int i=0 ; i<args.length-1 ; i++) {
			switch(args[i]) {
			case "-d" :
			case "--database" :
				db = new DataBase(args[i+1]);
				i++;
				
				break;
			case "-f" :
			case "--file" :
				db = new DataBase();
				db.addOdt(args[i+1]);
				i++;
				
				break;
			case "-w" :
			case "--word" :
				if(db != null) {
					db.search(args[i+1]);
				}
				else {
					System.out.println("DataBase not initialized !");
				}
				i++;
				
				break;
			default :
				System.out.println("ProjODT - v1.0 - (c) Vincent Monot & Louis Desportes");
				System.out.println("Utilitaire de recherche dans une base d'ODT");
				System.out.println("");
				System.out.println("Commandes:");
				System.out.println("  -d,  --database : défini la racine de la base de donnés à utiliser");
				System.out.println("  -f,  --file: ajoute un fichier à la base de donnés");
				System.out.println("  -w,  --word : recherche le mot dans la base de donnés");
			}
		}
	}
}
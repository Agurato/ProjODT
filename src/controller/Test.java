package controller;

import java.util.HashMap;
import java.util.Map.Entry;

import model.*;
//import view.*;

public class Test {
	public static void main(String []args) {
//		DataBase db = new DataBase();
//		db.addOdt("/home/vincent/Documents/ProjODT/odt/exemple2/exemple_traitement_de_texte_libre_office.odt");
		
		ODTFile odt = new ODTFile("/home/vincent/Documents/ProjODT/odt/exemple2/exemple_traitement_de_texte_libre_office.odt");
		HashMap<String, String> infos = odt.parseMetaXML();

		System.out.println("Office version : "+infos.get("officeVersion"));
		System.out.println("Initial creator : "+infos.get("initialCreator"));
		System.out.println("Initial date : "+infos.get("initialDate"));
		System.out.println("Final creator : "+infos.get("creator"));
		System.out.println("Final date : "+infos.get("date"));
		System.out.println("Title : "+infos.get("title"));
		System.out.println("Subject : "+infos.get("subject"));
		System.out.println("Number of word : "+infos.get("wordCount"));
		System.out.println("Number of pages : "+infos.get("pageCount"));
		
//		for(Entry<String, String> entry : odt.parseMetaXML().entrySet()) {
//			System.out.println(entry.getKey()+" : "+entry.getValue());
//		}
	}
}
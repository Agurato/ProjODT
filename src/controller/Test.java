package controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.*;
//import view.*;

public class Test {
	public static void main(String []args) {
		ODTFile file = new ODTFile("/home/vincent/Documents/odt/exemple_traitement_de_texte_libre_office.odt");
		try {
			file.parseContentXML(file.unzipODT(), "Contraintes");
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

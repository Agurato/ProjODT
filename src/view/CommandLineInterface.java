package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import controller.Controller;
import model.Result;

public class CommandLineInterface implements UserInterface{
	
	public CommandLineInterface() {
		Controller controller = new Controller();
	}

	@Override
	public void displayResults(ArrayList<Result> results) {
		if(!results.isEmpty()){
			System.out.println(results.size() + " résultats:");
			for(Result result: results){
				System.out.println(result.getQuote() + " dans " + result.getFilename());
			}
		}else{
			System.out.println("Aucun résultat.");
		}
	}

	@Override
	public void confirmSync() {
		System.out.println("La base de données à bien été synchronisé.");
	}

	@Override
	public void confirmChangeRoot(String rootPath) {
		System.out.println("La racine a bien été changée vers: " + rootPath);
	}
	
	@Override
	public void displayHelp() {
		System.out.println("ProjODT - v1.0 - (c) Vincent Monot & Louis Desportes");
		System.out.println("Utilitaire de recherche dans une base d'ODT");
		System.out.println("");
		System.out.println("Commandes:");
		System.out.println("  -d,  --database : défini la racine de la base de donnés à utiliser");
		System.out.println("  -h,  --help : affiche l'aide");
		System.out.println("  display : affiche tous les titres de la base de données");
		System.out.println("  search : recherche le/les termes donnés dans la base de données. Peut être utilisé avec ET/OU pour affiner les recherches (priorité au ET)");
		System.out.println("  open : ouvre le fichier adéquat");
	}

	@Override
	public void listTitles(ArrayList<Result> titles) {
		if(!titles.isEmpty()){
			if(titles.size()==1){
				System.out.println("1 titre dans " + titles.get(0).getFilename() +":");
			}else{
				System.out.println(titles.size() + " titres dans " + titles.get(0).getFilename() +":");
			}
			for(Result title: titles){
				int level = title.getLevel();
				if(level == 0) {
					System.out.print(">");
				}
				else {
					for(int i=1 ; i<level ; i++) {
						System.out.print(" ");
					}
					System.out.print("->");
				}
				System.out.println(title.getQuote());
			}
		}else{
			System.out.println("Aucun titre dans ce fichier.");
		}
	}

	@Override
	public void confirmOpening(String filename) {
		System.out.println("Ouverture de " + filename);
	}

	@Override
	public void listFiles(ArrayList<Result> files) {
		if(!files.isEmpty()){
			if(files.size()==1){
				System.out.println("1 fichier dans la base de données:");
			}else{
				System.out.println(files.size() + " fichiers dans la base de données:");
			}
			for(Result file: files){
				System.out.println("  * " + file.getFilename());
			}
		}else{
			System.out.println("La base de données est vide.");
		}
	}

	@Override
	public void displayInfos(HashMap<String, String> infos) {
		for(Entry<String, String> entry : infos.entrySet()) {
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
	}
}

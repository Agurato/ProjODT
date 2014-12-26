package view;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import controller.Controller;
import model.DataBase;
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
		System.out.println("\n---------------------");
		System.out.println("ProjODT - v1.0 - (c) Vincent Monot & Louis Desportes");
		System.out.println("Utilitaire de recherche dans une base d'ODT");
		System.out.println("");
		System.out.println("Commandes:");
		System.out.println("  -d,  --database : défini la racine de la base de donnés à utiliser");
		System.out.println("  -f,  --file :     ajoute un fichier à la base de donnés");
		System.out.println("  -w,  --word :     recherche le mot dans la base de donnés");
	}

	@Override
	public void ListTitles(ArrayList<Result> titles) {
		if(!titles.isEmpty()){
			System.out.println(titles.size() + " titres dans " + titles.get(0).getFilename() +":");
			for(Result title: titles){
				switch (title.getLevel()) {
				case 0:
					System.out.println(">" + title.getQuote());
					break;
				case 1:
					System.out.println("->" + title.getQuote());
					break;
				case 2:
					System.out.println(" ->" + title.getQuote());
					break;
				case 3:
					System.out.println("  ->" + title.getQuote());
					break;
				case 4:
					System.out.println("   ->" + title.getQuote());
					break;
				default:
					break;
				}
			}
		}else{
			System.out.println("Aucun titre dans ce fichier.");
		}
	}

	@Override
	public void confirmOpening(String filename) {
		System.out.println("Ouverture de " + filename);
	}

}

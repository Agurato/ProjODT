package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import controller.Controller;
import model.Result;

/**
 * Command Line Interface
 * @author Louis Desportes
 *
 */
public class CommandLineInterface implements UserInterface{
	
	public CommandLineInterface() {
		Controller controller = new Controller();
	}

	/**
	 * Display results of search
	 * 
	 * @param results
	 *            the results to display
	 */
	@Override
	public void displayResults(ArrayList<Result> results) {
		Collections.sort(results);
		if(!results.isEmpty()){
			System.out.println(results.size() + " résultats:");
			for(Result result: results){
				System.out.println(result.getQuote() + " dans " + result.getFilename());
			}
		}else{
			System.out.println("Aucun résultat.");
		}
	}

	/**
	 * Confirm that the database has been sync
	 */
	@Override
	public void confirmSync() {
		System.out.println("La base de données à bien été synchronisé.");
	}

	/**
	 * Confirm that root folder has been changed
	 * 
	 * @param rootPath
	 *            The new root folder
	 */
	@Override
	public void confirmChangeRoot(String rootPath) {
		System.out.println("La racine a bien été changée vers: " + rootPath);
	}
	
	/**
	 * Display Command Line Help
	 */
	//TODO add new commands
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

	/**
	 * List the titles of a file
	 */
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

	/**
	 * confirm that a file is about to open
	 * @param filename The path of the file opening
	 */
	@Override
	public void confirmOpening(String filename) {
		System.out.println("Ouverture de " + filename);
	}

	/**
	 * List files
	 * @param files The files to list
	 */
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

	/**
	 * Displays info about a file
	 * @param infos The informations to display
	 */
	@Override
	public void displayInfos(HashMap<String, String> infos) {
		String temp = null;
		
		if((temp = infos.get("officeVersion")) != null) {
			System.out.println("Office version : "+temp);
		}
		if((temp = infos.get("initialCreator")) != null) {
			System.out.println("Initial creator : "+temp);
		}
		if((temp = infos.get("initialDate")) != null) {
			System.out.println("Initial date : "+temp);
		}
		if((temp = infos.get("creator")) != null) {
			System.out.println("Final creator : "+temp);
		}
		if((temp = infos.get("date")) != null) {
			System.out.println("Final date : "+temp);
		}
		if((temp = infos.get("title")) != null) {
			System.out.println("Title : "+temp);
		}
		if((temp = infos.get("subject")) != null) {
			System.out.println("Subject : "+temp);
		}
		if((temp = infos.get("wordCount")) != null) {
			System.out.println("Number of word : "+temp);
		}
		if((temp = infos.get("pageCount")) != null) {
			System.out.println("Number of pages : "+temp);
		}
	}
}

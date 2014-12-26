package view;

import java.util.ArrayList;

import controller.Controller;
import model.DataBase;
import model.Result;

public class CommandLineInterface implements UserInterface{

	private enum Action{
		NONE, SEARCH, LIST, DISPLAY, SYNC
	}
	
	public CommandLineInterface(String[] args) {
		Controller controller = new Controller();
		int index = 0;
		Action action = Action.NONE;
		String param = "", root = "";
		
		//On
		for(int i=0 ; i<args.length-1 ; i++) {
			switch(args[i]) {
			case "-d" :
			case "--database" :
				root = args[i+1];
				i++;
				
			break;
			case "display" :
				action = Action.DISPLAY;
				param = args[i+1];
				i++;
				
			break;
			case "search" :
				action = Action.SEARCH;
				param = args[i+1];
				i++;
				
			break;
			case "list" :
				action = Action.LIST;
				
			break;
			case "sync" :
				action = Action.SYNC;
				
			break;
			default :
			}
		}
		
		switch(action) {
		case DISPLAY:
			controller.addOdt(param);
			//TODO: récupérer un tableau de titre en result
		break;
		case LIST :
			controller.changeRoot(root);
			System.out.println("Fichiers ODT présents dans la racine:");
			for (Result result: controller.listFiles()){
				System.out.println("  * " + result.getFilename());
			}
			
		break;
		case SEARCH :
			ArrayList<Result> results = controller.search(param);
			if(!results.isEmpty()){
				System.out.println("Résultats:");
				for(Result res : results) {
					System.out.println(res.getFilename() + " — " + res.getQuote());
				}
			}else{
				System.out.println("Aucun Résultat");
			}
		break; 
		default :
			System.out.println("\n---------------------");
			System.out.println("ProjODT - v1.0 - (c) Vincent Monot & Louis Desportes");
			System.out.println("Utilitaire de recherche dans une base d'ODT");
			System.out.println("");
			System.out.println("Commandes:");
			System.out.println("  -d,  --database : défini la racine de la base de donnés à utiliser");
			System.out.println("  -f,  --file :     ajoute un fichier à la base de donnés");
			System.out.println("  -w,  --word :     recherche le mot dans la base de donnés");
			break;
		}
		
	}

	@Override
	public void displayResults(ArrayList<Result> results) {
		// TODO Auto-generated method stub
		
	}

	public void chooseRoot(String pathName) {
		// TODO Auto-generated method stub
		
	}

	public void sync() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmSync() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmChangeRoot(String rootPath) {
		// TODO Auto-generated method stub
		
	}

}

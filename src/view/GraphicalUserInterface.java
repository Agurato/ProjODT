package view;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.BorderLayout;

import model.Result;

/**
 * 
 * @author Louis Desportes
 *
 */

public class GraphicalUserInterface extends JFrame implements UserInterface{
	private static final long serialVersionUID = 1L;
	JMenuBar menuWIMP;
	JMenu fileMenu;
	JMenuItem addItem;
	JMenuItem deleteItem;
	JMenuItem syncItem;
	
	JPanel searchPanel;
	JTextField searchField;
	JButton searchButton;
	
	JScrollPane scrollPanel;
	JPanel resultsPanel;
	JLabel helpText;
	
	Controller controller;
	
	public GraphicalUserInterface() {
		super("ProjODT");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Insets insets = getContentPane().getInsets();
		
		//searchPanel
		searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		getContentPane().add(searchPanel, BorderLayout.NORTH);
		//searchField
		searchField = new JTextField(10);
		searchPanel.add(searchField, BorderLayout.CENTER);
		//searchButton
		searchButton = new JButton("Rechercher");
		searchPanel.add(searchButton, BorderLayout.EAST);
		
		//Results Scroll
		resultsPanel = new JPanel(new FlowLayout());
		helpText = new JLabel("Tapez votre recherche et appuyez sur entrée.");
		resultsPanel.add(helpText);
		scrollPanel = new JScrollPane(resultsPanel);
		getContentPane().add(this.scrollPanel, BorderLayout.CENTER);
		
		//Menu
		//adding the JmenuBar
		menuWIMP = new JMenuBar();
		this.setJMenuBar(menuWIMP);
		//adding the fileMenu 
		fileMenu = new JMenu("Fichier");
		menuWIMP.add(fileMenu);
		//adding elements of fileMenu
		addItem = new JMenuItem("Ajouter");
		fileMenu.add(addItem);
		deleteItem= new JMenuItem("Supprimer");
		fileMenu.add(deleteItem);
		syncItem= new JMenuItem("Synchroniser");
		fileMenu.add(syncItem);
		
		//Set size
		this.setSize(500 + insets.left + insets.right,
				300 + insets.top + insets.bottom);
		this.setLocationRelativeTo(null);
		
		//Listen to actions
		searchField.addActionListener(new searchReact());
		searchButton.addActionListener(new searchReact());
		
		setVisible(true);
		
		// Start controller
		controller = new Controller();
	}
	
	//search validation listening
	public class searchReact implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Result> results = controller.search(searchField.getText());
			resultsPanel.removeAll();
			if(!results.isEmpty()){
				for (Result result : results) {
					resultsPanel.add(new JLabel(result.getFilename()));
					resultsPanel.add(new JLabel(Integer.toString(result.getFrequence())));
					resultsPanel.add(new JLabel(result.getExcerpt()));
				}
			}else{
				resultsPanel.add(helpText);
			}
			setVisible(true);
		}
		
	}
	
	@Override
	public void displayResults(ArrayList<Result> results) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void confirmAdd(String fileName) {
		JOptionPane.showMessageDialog(this, fileName + " a bien été ajouté à la base de donnée.", "Fichier ajouté", JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void confirmDelete(String fileName) {
		JOptionPane.showMessageDialog(this, fileName + " a bien été supprimé de la base de donnée.", "Fichier supprimé", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	@Override
	public void confirmSync() {
		JOptionPane.showMessageDialog(this, "La base de donnée a bien été synchronisée", "Base de données synchronisé", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
}

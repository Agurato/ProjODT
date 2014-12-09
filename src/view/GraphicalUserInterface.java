package view;

import java.util.ArrayList;

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
import javax.swing.JMenuBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Container;
import java.awt.Dimension;
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
	JScrollPane scrollPanel;
	JPanel resultsPanel;
	JTextField searchField;
	JLabel enteredText;
	JMenuBar menuWIMP;
	JMenu fileMenu;
	JMenuItem addItem;
	JMenuItem deleteItem;
	JMenuItem syncItem;

	public GraphicalUserInterface() {
		super("ProjODT");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
	    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ProjODT");
		getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Insets insets = getContentPane().getInsets();
		
		//TextField
		searchField = new JTextField(10);
		getContentPane().add(searchField, BorderLayout.NORTH);

		//Results Scroll
		resultsPanel = new JPanel();
		enteredText = new JLabel("Tapez votre recherche et appuyez sur entrée.");
		resultsPanel.add(enteredText);
		scrollPanel = new JScrollPane(resultsPanel);
		getContentPane().add(this.scrollPanel, BorderLayout.CENTER);
		
		//Menu
		menuWIMP = new JMenuBar();
		fileMenu = new JMenu("Fichier");
		addItem = new JMenuItem("Ajouter");
		deleteItem= new JMenuItem("Supprimer");
		syncItem= new JMenuItem("Synchroniser");
		fileMenu.add(addItem);
		fileMenu.add(deleteItem);
		fileMenu.add(syncItem);
		menuWIMP.add(fileMenu);
		this.setJMenuBar(menuWIMP);
		
		//Set size
		this.setSize(500 + insets.left + insets.right,
				300 + insets.top + insets.bottom);
		
		//Listen to actions
		searchField.addActionListener(new searchReact());
		
		setVisible(true);
	}
	
	//text entered listening
	public class searchReact implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			enteredText.setText(searchField.getText());
		}
		
	}
	@Override
	public void displayResults(ArrayList<Result> results) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmAdd(String fileName) {
		JOptionPane confirmation = new JOptionPane();
		confirmation.showMessageDialog(null, fileName + " a bien été ajouté à la base de donnée.", "Fichier ajouté", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void confirmDelelte(String fileName) {
		JOptionPane deletion = new JOptionPane();
		deletion.showMessageDialog(null, fileName + " a bien été supprimé de la base de donnée.", "Fichier supprimé", JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public void confirmSync(String fileName) {
		JOptionPane confirmation = new JOptionPane();
		confirmation.showMessageDialog(null, "La base de donnée a bien été synchronisée", "Base de données synchronisé", JOptionPane.INFORMATION_MESSAGE);
		
	}

}

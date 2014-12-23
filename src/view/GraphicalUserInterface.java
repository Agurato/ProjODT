package view;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import model.Result;
import view.ListResultCellRenderer;

/**
 * 
 * @author Louis Desportes
 *
 */

public class GraphicalUserInterface extends JFrame implements UserInterface {
	private static final long serialVersionUID = 1L;
	JMenuBar menuWIMP;
	JMenu fileMenu;
	JMenuItem chooseItem;
	JMenuItem syncItem;
	JMenuItem closeItem;

	JPanel searchPanel;
	JTextField searchField;
	JButton searchButton;

	JList<Result> resultsList;
	DefaultListModel<Result> resultsModel;
	Controller controller;

	public GraphicalUserInterface() {
		super("ProjODT");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Insets insets = getContentPane().getInsets();

		// searchPanel
		searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		getContentPane().add(searchPanel, BorderLayout.NORTH);
		// searchField
		searchField = new JTextField(10);
		searchPanel.add(searchField, BorderLayout.CENTER);
		// searchButton
		searchButton = new JButton("Rechercher");
		searchPanel.add(searchButton, BorderLayout.EAST);

		// Results List
		resultsModel = new DefaultListModel<Result>();
		resultsList = new JList<Result>(resultsModel);
		resultsList.setCellRenderer(new ListResultCellRenderer());
		resultsList
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		// resultsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		resultsList.setVisibleRowCount(-1);
		getContentPane().add(new JScrollPane(resultsList), BorderLayout.CENTER);
		resultsModel.addElement(new Result(-1, -1,
				"Entrez une chaine à rechercher", ""));

		// Menu
		// adding the JmenuBar
		menuWIMP = new JMenuBar();
		this.setJMenuBar(menuWIMP);
		// adding the fileMenu
		fileMenu = new JMenu("Fichier");
		menuWIMP.add(fileMenu);
		// adding elements of fileMenu
		// ChangeRoot
		chooseItem = new JMenuItem("Changer la racine");
		chooseItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(chooseItem);
		chooseItem.addActionListener(new ChooseReact());
		// Sync
		syncItem = new JMenuItem("Synchroniser");
		syncItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(syncItem);
		syncItem.addActionListener(new SyncReact());
		// Close
		closeItem = new JMenuItem("Fermer");
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(closeItem);
		closeItem.addActionListener(new CloseReact());

		// Set size
		this.setSize(500 + insets.left + insets.right, 300 + insets.top
				+ insets.bottom);
		// Center the window
		this.setLocationRelativeTo(null);

		// Listen to actions
		searchField.addActionListener(new SearchReact());
		searchButton.addActionListener(new SearchReact());

		setVisible(true);

		// Start controller
		controller = new Controller();
	}

	// search validation listening
	public class SearchReact implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				displayResults(controller.search(searchField.getText()));
			} catch (NoSuchElementException eNoElement) {
				JOptionPane.showMessageDialog(getContentPane(),
						"Impossible de trouver le dosser racine:",
						"Racine introuvable", JOptionPane.ERROR_MESSAGE);
				chooseRoot();
				displayResults(controller.search(searchField.getText()));
			}
		}

	}

	// Choose Folder Listening
	private class ChooseReact implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			chooseRoot();
		}

	}

	// Sync files Listening
	public class SyncReact implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sync();
		}
	}

	// Close window Listening
	public class CloseReact implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			System.exit(NORMAL);
		}
	}

	@Override
	public void displayResults(ArrayList<Result> results) {
		resultsModel.removeAllElements();
		if (!results.isEmpty()) {
			for (Result result : results) {
				resultsModel.addElement(result);
			}
		} else {
			resultsModel.addElement(new Result(-1, -1,
					"Entrez une chaine à rechercher", ""));
		}
		setVisible(true);
	}

	public void chooseRoot() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Sélectionnez le dossier où rechercher les fichiers");
		int returnVal = chooser.showOpenDialog(getContentPane());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			controller.changeRoot(chooser.getSelectedFile().getAbsolutePath());
			confirmChangeRoot(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	@Override
	public void confirmChangeRoot(String rootPath) {
		JOptionPane.showMessageDialog(this, "Racine changée vers: " + rootPath,
				"Racine changée", JOptionPane.INFORMATION_MESSAGE);

	}

	public void sync() {
		try {
			controller.sync();
			JOptionPane.showMessageDialog(this,
					"La base de donnée a bien été synchronisée",
					"Base de données synchronisé",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException eNotFound) {
			JOptionPane
					.showMessageDialog(
							GraphicalUserInterface.this,
							"Le dossier sélectionné n'existe plus, veuillez en choisir un nouveau",
							"Dossier Inexistant", JOptionPane.ERROR_MESSAGE);
			chooseRoot();
		}
	}

	@Override
	public void confirmSync() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(this,
				"La base de donnée a bien été synchronisée",
				"Base de données synchronisé", JOptionPane.INFORMATION_MESSAGE);
	}

}

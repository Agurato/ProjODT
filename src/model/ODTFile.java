package model;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author vincent
 *
 */

public class ODTFile implements TextFile {
	
	private File odt = null;
	private File repertory = null;
	private File extractedRepertory = null;
	private ArrayList<Result> titles = null;
	private HashMap<String, String> infos;
	private final String separator = ";";
	private String path;
	
	/**
	 * 
	 * @param path
	 */

	public ODTFile(String path) {
		if(path.endsWith(".odt")) {
			this.odt = new File(path);
			this.repertory = new File(odt.getParent());
			this.extractedRepertory = new File(repertory.getAbsolutePath()+"/"+odt.getName().replace(".odt", ""));
			this.unzipODT();
			this.titles = new ArrayList<Result>();
			parseContentXML();
			infos = new HashMap<String, String>();
			parseMetaXML();
			suppExtract(extractedRepertory);
		}
		else {
			System.out.println("The given path : "+path+" doesn't correspond to a odt file");
		}
		this.path = path;
	}
	
	/**
	 * 
	 * @return the odt as a File
	 */
	
	public File getFile() {
		return odt;
	}
	
	/**
	 * 
	 * @return the odt as a String (pathname)
	 */
	
	public String getFilename(){
		return path;
	}
	
	/**
	 * Delete the extract folder entirely
	 * @param supp
	 */
	
	private void suppExtract(File supp) {
		for(File file : supp.listFiles()) {
			if(file.isDirectory()) {
				suppExtract(file);
			}
			file.delete();
		}
		if(extractedRepertory.listFiles().length == 0) {
			extractedRepertory.delete();
		}
	}
	
	/**
	 * Extract the odt file in a folder which has the same name (without ".odt" at the end)
	 */

	private void unzipODT() {
		extractedRepertory.mkdir();
		// Create the folder for the extraction
		
		File bufferFile = null; // Used to extract all files one by one
		try {
			ZipInputStream zipI = new ZipInputStream(new BufferedInputStream(new FileInputStream(odt.getAbsolutePath())));
			ZipEntry zipE = null; // Like an Iterator
			
			while ((zipE = zipI.getNextEntry()) != null) {
				bufferFile = new File(extractedRepertory.getAbsolutePath(), zipE.getName());
				
				if(zipE.isDirectory()) {
					bufferFile.mkdirs();
					continue;
				}
				bufferFile.getParentFile().mkdirs();
				
				BufferedOutputStream extractBuffer = new BufferedOutputStream(new FileOutputStream(bufferFile));
				
				try {
					try {
						byte[] byteBuffer = new byte[8192];
						int bytesRead;
						while(-1 != (bytesRead = zipI.read(byteBuffer))) {
							extractBuffer.write(byteBuffer, 0, bytesRead);
						}
					} finally {
						extractBuffer.close();
					}
				} catch(IOException ioe) {
					bufferFile.delete();
				}				
			}

			zipI.close();
		}
		catch(FileNotFoundException fnne) {
			fnne.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Parse the content.xml file contained in the extract folder
	 * Save the titles in "results.txt"
	 */
	
	private void parseContentXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			System.out.println("In file : "+odt.getAbsolutePath()+" :");
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(extractedRepertory.getAbsolutePath(), "content.xml"));
			
			Element root = parser.getDocumentElement();
			// This is the "office:document-content", which contains everything in the content.xml file
			
			// Instantly get the "office:text" list even if it's not a direct child
			Element officeText = (Element) root.getElementsByTagName("office:text").item(0);
			
			// Same, it takes the "text:title" and "text:h" elements
			NodeList textTitleList = officeText.getElementsByTagName("text:title");
			NodeList textHList = officeText.getElementsByTagName("text:h");
			
			for(int i=0 ; i<textTitleList.getLength() ; i++) {
				Element textTitle = (Element) textTitleList.item(i);
				System.out.println(textTitle.getTextContent());
				
				titles.add(new Result(0, 1, odt.getAbsolutePath(), textTitle.getTextContent(), getThumbnail()));
				// Add the useful informations to write in the file later
			}
			
			for(int i=0 ; i<textHList.getLength() ; i++) {
				Element textH = (Element) textHList.item(i);
				System.out.println(textH.getTextContent());
				
				titles.add(new Result(Integer.parseInt(textH.getAttribute("text:outline-level")), 1, odt.getAbsolutePath(), textH.getTextContent(), getThumbnail()));
				
			}
		}
		catch(ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	 * Parse the meta.xml file
	 * @return HashMap<nameOfTheInfo, valueOfTheInfo>
	 */
	
	private void parseMetaXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(extractedRepertory.getAbsolutePath(), "meta.xml"));
			
			Element root = parser.getDocumentElement();
			
			// Get the office version
			String officeVersion = root.getAttribute("office:version");
			if(officeVersion != null){
				infos.put("officeVersion", officeVersion);
			}
			
			Element officeMeta = (Element) root.getElementsByTagName("office:meta").item(0);

			// Get the creator
			Element dcCreator = (Element) officeMeta.getElementsByTagName("dc:creator").item(0);
			if(dcCreator != null){
				infos.put("creator", dcCreator.getTextContent());
			}
			
			// Get the initial creator
			Element initialCreator = (Element) officeMeta.getElementsByTagName("meta:initial-creator").item(0);
			if(initialCreator != null){
				infos.put("initialCreator", initialCreator.getTextContent());
			}
			
			// Get the date
			Element dcDate = (Element) officeMeta.getElementsByTagName("dc:date").item(0);
			if(dcDate != null){
				infos.put("date", dcDate.getTextContent());
			}
			
			//Get the initial date
			Element initialDate = (Element) officeMeta.getElementsByTagName("meta:creation-date").item(0);
			if(initialDate != null){
				infos.put("initialDate", initialDate.getTextContent());
			}
			
			// Get the title
			Element dcTitle = (Element) officeMeta.getElementsByTagName("dc:title").item(0);
			if(dcTitle != null){
				infos.put("title", dcTitle.getTextContent());
			}
			
			// Get the subject
			Element dcSubject = (Element) officeMeta.getElementsByTagName("dc:subject").item(0);
			if(dcSubject != null){
				infos.put("subject", dcSubject.getTextContent());
			}
			
			Element pageCount = (Element) officeMeta.getElementsByTagName("meta:document-statistic").item(0);
			infos.put("pageCount", pageCount.getAttribute("meta:page-count"));
			infos.put("wordCount", pageCount.getAttribute("meta:word-count"));
			
		}
		catch(ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public HashMap<String, String> getInfos(){
		return infos;
	}

	/**
	 * Used to search a String among the titles of the odt file
	 * @return a list of Result (~quote) after searching in the file
	 */
	public ArrayList<Result> contains(String search) {
		ArrayList<Result> contained = new ArrayList<Result>();
		
		for(Result title: titles){
			System.out.println(title);
			if(title.getQuote().toLowerCase().contains(search.toLowerCase())){
				contained.add(title);
			}
		}
		
		return contained;
	}
	
	/**
	 * 
	 * @return the thumbnail image
	 */
	public BufferedImage getThumbnail() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(extractedRepertory.getAbsolutePath()+"/Thumbnails/thumbnail.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * @return a list of titles
	 */
	public ArrayList<Result> listTitles(){
		//TODO: Quick fix, results should be in memory since instanciation
		boolean resultsExists = false;
		
		for(File file : extractedRepertory.listFiles()) {
			if(file.getName().equals("results.txt")) {
				resultsExists = true;
			}
		}
		if(!resultsExists) {
			this.parseContentXML();
		}
		return null;
	}
}
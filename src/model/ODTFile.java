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
	private File extract = null;
	private File results = null;
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
			this.extract = new File(repertory.getAbsolutePath()+"/"+odt.getName().replace(".odt", ""));
			this.results = new File(extract.getAbsolutePath()+"/results.txt");
			
			this.unzipODT();
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
	 * @return the extract folder as a file
	 */
	
	public File getExtract() {
		return extract;
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
	
	public void suppExtract(File supp) {
		for(File file : supp.listFiles()) {
			if(file.isDirectory()) {
				suppExtract(file);
			}
			file.delete();
		}
		if(extract.listFiles().length == 0) {
			extract.delete();
		}
	}
	
	/**
	 * Extract the odt file in a folder which has the same name (without ".odt" at the end)
	 */

	public void unzipODT() {
		extract.mkdir();
		// Create the folder for the extraction
		
		File bufferFile = null; // Used to extract all files one by one
		try {
			ZipInputStream zipI = new ZipInputStream(new BufferedInputStream(new FileInputStream(odt.getAbsolutePath())));
			ZipEntry zipE = null; // Like an Iterator
			
			while ((zipE = zipI.getNextEntry()) != null) {
				bufferFile = new File(extract.getAbsolutePath(), zipE.getName());
				
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
	
	public void parseContentXML() {
		String writing = "", lineSeparator = "#";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			System.out.println("In file : "+odt.getAbsolutePath()+" :");
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(extract.getAbsolutePath(), "content.xml"));
			
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
				
				writing += "text:title"+separator+"0"+separator+textTitle.getTextContent()+lineSeparator;
				// Add the useful informations to write in the file later
			}
			
			for(int i=0 ; i<textHList.getLength() ; i++) {
				Element textH = (Element) textHList.item(i);
				System.out.println(textH.getTextContent());
				
				writing += "text:h"+separator+textH.getAttribute("text:outline-level")+separator+textH.getTextContent()+lineSeparator;
				
			}
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(results));
				
				for(String str : writing.split(lineSeparator)) {
					bw.write(str);
					bw.newLine();
				}
				
				bw.close();
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
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
	
	public HashMap<String, String> parseMetaXML() {
		HashMap<String, String> metaInfos = new HashMap<String, String>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(extract.getAbsolutePath(), "meta.xml"));
			
			Element root = parser.getDocumentElement();
			
			// Get the office version
			String officeVersion = root.getAttribute("office:version");
			if(officeVersion != null){
				metaInfos.put("officeVersion", officeVersion);
			}
			
			Element officeMeta = (Element) root.getElementsByTagName("office:meta").item(0);

			// Get the creator
			Element dcCreator = (Element) officeMeta.getElementsByTagName("dc:creator").item(0);
			if(dcCreator != null){
				metaInfos.put("creator", dcCreator.getTextContent());
			}
			
			// Get the initial creator
			Element initialCreator = (Element) officeMeta.getElementsByTagName("meta:initial-creator").item(0);
			if(initialCreator != null){
				metaInfos.put("initialCreator", initialCreator.getTextContent());
			}
			
			// Get the date
			Element dcDate = (Element) officeMeta.getElementsByTagName("dc:date").item(0);
			if(dcDate != null){
				metaInfos.put("date", dcDate.getTextContent());
			}
			
			//Get the initial date
			Element initialDate = (Element) officeMeta.getElementsByTagName("meta:creation-date").item(0);
			if(initialDate != null){
				metaInfos.put("initialDate", initialDate.getTextContent());
			}
			
			// Get the title
			Element dcTitle = (Element) officeMeta.getElementsByTagName("dc:title").item(0);
			if(dcTitle != null){
				metaInfos.put("title", dcTitle.getTextContent());
			}
			
			// Get the subject
			Element dcSubject = (Element) officeMeta.getElementsByTagName("dc:subject").item(0);
			if(dcSubject != null){
				metaInfos.put("subject", dcSubject.getTextContent());
			}
			
			Element pageCount = (Element) officeMeta.getElementsByTagName("meta:document-statistic").item(0);
			metaInfos.put("pageCount", pageCount.getAttribute("meta:page-count"));
			metaInfos.put("wordCount", pageCount.getAttribute("meta:word-count"));
			
		}
		catch(ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return metaInfos;
	}
	
	/**
	 * Used to search a String among the titles of the odt file
	 * @return a list of Result (~quote) after searching in the file
	 */
	
	public ArrayList<Result> examination(String search) {
		boolean resultsExists = false;
		ArrayList<Result> result = new ArrayList<Result>();
		
		for(File file : extract.listFiles()) {
			if(file.getName().equals("results.txt")) {
				resultsExists = true;
			}
		}
		if(!resultsExists) {
			this.parseContentXML();
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(results));
			String temp = null;
			String[] split;
			
			while((temp = br.readLine()) != null) {
				split = temp.split(separator);
				
				split[2] = Normalizer.normalize(split[2], Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
				search = Normalizer.normalize(search, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", ""); // Remove accents
				// e.g. : normalize("à", Normalizer.Form.NFD) returns "a`" and replaceAll("[\u0300-\u036F]", "") returns a
				// [\u0300-\u036F] is the interval for accents
				
				if(split[2].toLowerCase().contains(search.toLowerCase())) {
					result.add(new Result(Integer.parseInt(split[1]), 1, odt.getAbsolutePath(), split[2], getThumbnail()));
				}
			}
			
			br.close();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 
	 * @return the thumbnail image
	 */
	
	public BufferedImage getThumbnail() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(extract.getAbsolutePath()+"/Thumbnails/thumbnail.png"));
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
		ArrayList<Result> result = new ArrayList<Result>();
		
		for(File file : extract.listFiles()) {
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
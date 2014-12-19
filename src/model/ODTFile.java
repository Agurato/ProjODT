package model;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	private File repository = null;
	private String path = null;

	public ODTFile(String path) {
		this.odt = new File(path);
		this.repository = new File(odt.getParent());
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	public File unzipODT() throws FileNotFoundException, IOException {
		// Create the folder where it will be unzipped
		File folder = new File(repository.getAbsolutePath()+"/"+odt.getName().replace(".odt", ""));
		folder.mkdir();
		
		File bufferFile = null; // Used to extract all files one by one
		ZipInputStream zipI = new ZipInputStream(new BufferedInputStream(new FileInputStream(path)));
		ZipEntry zipE = null; // Like an Iterator
		try {
			while ((zipE = zipI.getNextEntry()) != null) {
				bufferFile = new File(folder.getAbsolutePath(), zipE.getName());
				
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
					throw ioe;
				}				
			}
		}
		finally {
			zipI.close();
		}
		return folder;
	}
	
	public Result parseContentXML(File folder, CharSequence search) {
		int frequency = 0;
		String separator = ";";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document parser = builder.parse(new File(folder.getAbsolutePath(), "content.xml"));
			
			Element root = parser.getDocumentElement();
			// This is the "office:document-content", which contains everything in the content.xml file
			NodeList rootNodes = root.getChildNodes();
			// We get his children ("office:scripts", "office:font-face-decls", "office:automatic-styles" & "office:body")
			
			for(int i=0 ; i<rootNodes.getLength() ; i++) {
				// Parse root children and get the "office:body" one
				if(rootNodes.item(i).getNodeName().equals("office:body")) {
					
					Element officeBody = (Element) rootNodes.item(i);
					NodeList officeBodyNodes = officeBody.getChildNodes();
					
					for(int j=0 ; j<officeBodyNodes.getLength() ; j++) {
						// Parse "office:body" children and get the "office:text" one, even if it should be the only child
						if(officeBodyNodes.item(j).getNodeName().equals("office:text")) {
						
							Element officeText = (Element) officeBodyNodes.item(j);
							
							// Take the text:h and the text:title elements
							NodeList textHList = officeText.getElementsByTagName("text:h");
							NodeList textTitleList = officeText.getElementsByTagName("text:title");
							
							// Search among them the search value
							for(int k=0 ; k<textHList.getLength() ; k++) {
								Element textH = (Element) textHList.item(k);
								System.out.println(textH.getTextContent());
								
								try {
									BufferedWriter bw = new BufferedWriter(new FileWriter(folder.getAbsolutePath()+"/results.txt"));
									BufferedReader br = new BufferedReader(new FileReader(folder.getAbsolutePath()+"/results.txt"));
									
									String temp = "";
									String line = "";
									
									while((line = br.readLine()) != null) {
										temp += line;
									}
									br.close();
									
									bw.write(temp+"text:h"+separator+textH.getAttribute("text:outline-level")+separator+textH.getTextContent());
									bw.newLine();
									bw.close();
								}
								catch(IOException ioe) {
									ioe.printStackTrace();
								}
								
								if(textH.getTextContent().contains(search.toString())) {
									System.out.println("** found **");
									frequency ++;
								}
							}
							
							for(int k=0 ; k<textTitleList.getLength() ; k++) {
								Element textTitle = (Element) textTitleList.item(k);
								System.out.println(textTitle.getTextContent());
								
								try {
									BufferedWriter bw = new BufferedWriter(new FileWriter(folder.getAbsolutePath()+"/results.txt"));
									BufferedReader br = new BufferedReader(new FileReader(folder.getAbsolutePath()+"/results.txt"));
									
									String temp = "";
									String line = "";
									
									while((line = br.readLine()) != null) {
										temp += line;
									}
									br.close();
									
									bw.write(temp+"text:title"+separator+textTitle.getTextContent());
									bw.newLine();
									bw.close();
								}
								catch(IOException ioe) {
									ioe.printStackTrace();
								}
								
								if(textTitle.getTextContent().contains(search.toString())) {
									System.out.println("** found **");
									frequency ++;
								}
							}
						}
					}
				}
			}
		}
		catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch(SAXException saxe) {
			saxe.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return new Result(frequency, path, "Quote");
	}
}

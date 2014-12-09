package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	
	public void parseContentXML(File folder, CharSequence search) {
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
							NodeList textHList = officeText.getElementsByTagName("text:h");
							NodeList textPList = officeText.getElementsByTagName("text:p");
							
							for(int k=0 ; k<textHList.getLength() ; k++) {
								Element textH = (Element) textHList.item(k);
								System.out.println(textH.getTextContent());
								
								if(textH.getTextContent().contains(search.toString())) {
									System.out.println("** found **");
								}
							}
							
							for(int k=0 ; k<textPList.getLength() ; k++) {
								Element textP = (Element) textPList.item(k);
								if(!(textP.getAttribute("text:style-name").startsWith("Text") ||
									 textP.getAttribute("text:style-name").startsWith("Content"))) {
									System.out.println(textP.getTextContent());
									
									if(textP.getTextContent().contains(search.toString())) {
										System.out.println("** found **");
									}
								}
							}
							
//							for(int k=0 ; k<officeTextNodes.getLength() ; k++) {
//								System.out.println(officeTextNodes.item(k).getNodeName());
//								
//								if(officeTextNodes.item(k).getNodeName().equals("text:h")) {
//									System.out.println("\tSearching in \"text:h\" ...");
//									
//									
//								}
//								
////								// Parse "office:text" children
////								Element element = (Element) officeTextNodes.item(k);
////								// Takes the k-th line
////								Element attribute = (Element) element.getElementsByTagName("text:syle-name").item(0);
////								// Takes the attribute of the line
////								try {
////									content = attribute.getTextContent();
////									// Get the content of the attribute
////								}
////								catch(NullPointerException npe) {
////									npe.printStackTrace();
////								}
////								
////								// If (the node is a HEADING type) OR ((it is a PARAGRAPH type) AND (it isn't a TEXT type))
////								if(element.getNodeName().equals("text:h") ||
////									(element.getNodeName().equals("text:p") && !content.startsWith("Text"))) {
////									if(content.contains(search.toString())) {
////										System.out.println("found");
////									}
////								}
//							}
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
	}
	/*
		for(File file : folder.listFiles()) {
			if(!file.getName().equals("content.xml")) {
				if(!file.isDirectory()) {
					file.delete();
				}
			}
		}
	*/
}

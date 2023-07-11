/**
 * Filename: JSONToCSV.java
 * Description: File implementing the convertToCSV from the CSVAdapter interface.
 * GoF Pattern: Adapter 
 * GoF Role: ConcreteAdapter
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.XML;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLToCSV implements XMLAdapter {

	/**
	 * Converts XML file data to CSV file data
	 * 
	 * @param xmlFile XML file 
	 * 
	 * @return CSV file data
	 */
	@Override
	public List<String[]> convertToCSV(File xmlFile) {
		List<String[]> data = new ArrayList<>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new FileInputStream(xmlFile));
			document.getDocumentElement().normalize();

			NodeList nodeList = document.getElementsByTagName("row");

			// Create header row
			List<String> header = new ArrayList<>();
			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					NodeList childNodes = element.getChildNodes();
					for(int j = 0; j < childNodes.getLength(); j++) {
						Node childNode = childNodes.item(j);
						if(childNode.getNodeType() == Node.ELEMENT_NODE) {
							String tag = childNode.getNodeName();
							header.add(tag);
						}
					}
				}
			}

			data.add(header.toArray(new String[0]));

			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element rowElement = (Element) node;
					List<String> rowData = new ArrayList<>();
					NodeList childNodes = rowElement.getChildNodes();
					for(int j = 0; j < childNodes.getLength(); j++) {
						Node childNode = childNodes.item(j);
						if(childNode.getNodeType() == Node.ELEMENT_NODE) {
							rowData.add(childNode.getTextContent());
						}
					}
					String[] rowArray = rowData.toArray(new String[0]);
					data.add(rowArray);
				}
			}
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		} 
		catch (SAXException saxe) {
			saxe.printStackTrace();
		} 
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		
		return data;
	}
}

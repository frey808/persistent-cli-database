/**
 * Filename: XMLToCSVConverter.java
 * Description: Implements method to import csv data from an xml file
 * GoF Pattern: Adapter
 * GoF Role: Adaptee
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.XML;

import java.io.File;
import java.io.IOException;
import java.util.List;

import Constants.Constants;
import Database.Processing.Reading.CSVReader;
import Database.Processing.Reading.ReadContext;
import Database.Processing.Reading.ReadStrategy;
import Database.Processing.Writing.CSVWriter;
import Database.Processing.Writing.WriteContext;
import Database.Processing.Writing.WriteStrategy;

public class XMLToCSVConverter {
	private XMLToCSV adapter;

	public XMLToCSVConverter(XMLToCSV adapter) {
		this.adapter = adapter;
	}

	/**
	 * Convert XML data to CSV data
	 * 
	 * @param xmlData XML data as a string
	 * 
	 * @return CSV data
	 */
	public List<String[]> convert(File xmlData) {
		return this.adapter.convertToCSV(xmlData);
	}

	/**
	 * Import XML data from a source to a given destination
	 * 
	 * @param xmlData XML data as a string
	 * @param source Source location
	 * @param destination Destination location
	 */
	public void importData(String source, String destination) {
		List<String[]> importedData = this.convert(new File(source));
		try {
			WriteStrategy writeStrategy = new CSVWriter();
			WriteContext writeContext = new WriteContext();
			writeContext.setWriteStrategy(writeStrategy);
			writeStrategy.writeFile(new File(destination), importedData);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}

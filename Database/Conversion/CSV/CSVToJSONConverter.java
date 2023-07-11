/**
 * Filename: CSVtoJSONConverter.java
 * Description: Implements method to export json data to a location
 * GoF Pattern: Adapter
 * GoF Role: Adaptee
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.CSV;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVToJSONConverter {
	private CSVAdapter adapter;

	public CSVToJSONConverter(CSVAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * Convert a CSV file to a JSON file
	 * 
	 * @param data CSV data as a List<String[]>
	 * 
	 * @return JSON data as a string
	 */
	public String convert(List<String[]> data) {
		return this.adapter.convertToJSON(data);
	}

	/**
	 * Export the CSV data to a given file
	 * 
	 * @param data Data to export
	 * 
	 * @param filename File to export data to
	 */
	public void export(List<String[]> data, String filename) {
		String exportedData = this.convert(data);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)))) {
			writer.write(exportedData);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}

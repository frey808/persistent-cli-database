/**
 * Filename: CSVtoJSONConverter.java
 * Description: Implements method to export csv data to a location
 * GoF Pattern: Adapter
 * GoF Role: Adaptee
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.JSON;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONTokener;

import Constants.Constants;
import Database.Processing.Reading.CSVReader;
import Database.Processing.Reading.ReadContext;
import Database.Processing.Reading.ReadStrategy;
import Database.Processing.Writing.CSVWriter;
import Database.Processing.Writing.WriteContext;
import Database.Processing.Writing.WriteStrategy;

public class JSONToCSVConverter {
	private JSONAdapter adapter;

	public JSONToCSVConverter(JSONAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * Convert JSON data to CSV data
	 * 
	 * @param jsonData JSON data as a string
	 * 
	 * @return CSV data
	 */
	public List<String[]> convert(JSONArray jsonData) {
		return this.adapter.convertToCSV(jsonData);
	}

	/**
	 * Import JSON data from a given source to a given destination
	 * 
	 * @param source Source location
	 * @param destination Destination location
	 */
	public void importData(String source, String destination) {
		try {
			FileReader reader = new FileReader(source);
			JSONTokener tokener = new JSONTokener(reader);
			JSONArray array = new JSONArray(tokener);
			List<String[]> importedData = this.convert(array);

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

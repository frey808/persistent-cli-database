/**
 * Filename: CSVtoJSONConverter.java
 * Description: Implements method to export csv data to a location
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

public class CSVToXMLConverter {
	private CSVAdapter adapter;


	public CSVToXMLConverter(CSVAdapter adapter) {
		this.adapter = adapter;
	}

	public String convert(List<String[]> data) {
		return this.adapter.convertToXML(data);
	}

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

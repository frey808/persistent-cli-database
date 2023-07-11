/**
 * Filename: CSVAdapter.java
 * Description: Interface defining method to export a csv file to a json and xml file
 * GoF Pattern: Adapter
 * GoF Role: Adapter
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.CSV;

import java.util.List;

import org.json.JSONArray;

public interface CSVAdapter {
	public String convertToJSON(List<String[]> data);
	public String convertToXML(List<String[]> data);
}

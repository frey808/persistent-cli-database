/**
 * Filename: CSVAdapter.java
 * Description: Interface defining method to import a JSON file to a CSV file
 * GoF Pattern: Adapter
 * GoF Role: Adapter
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.JSON;

import java.util.List;

import org.json.JSONArray;

public interface JSONAdapter {
	public List<String[]> convertToCSV(JSONArray jsonData);
}

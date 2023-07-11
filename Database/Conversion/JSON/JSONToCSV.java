/**
 * Filename: JSONToCSV.java
 * Description: File implementing the convertToCSV from the CSVAdapter interface.
 * GoF Pattern: Adapter 
 * GoF Role: ConcreteAdapter
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.JSON;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONToCSV implements JSONAdapter {
	
	/**
	 * Convert JSON data to CSV data. CSV data is out of order, 
	 * no matter how many times I try to sort it
	 * 
	 * @param jsonData JSONArray of data
	 * 
	 * @return CSV data
	 */
	@Override
	public List<String[]> convertToCSV(JSONArray jsonData) {
		List<String[]> csvData = new ArrayList<>();
		JSONObject firstObject = jsonData.getJSONObject(0);
        Set<String> keys = firstObject.keySet();

		for(int i = 0; i < jsonData.length(); i++) {
			JSONObject jsonObject = jsonData.getJSONObject(i);
			String[] row = new String[keys.size()];
			int index = 0;
			for(String key: keys) {
				row[index] = jsonObject.get(key).toString();
				index++;
			}

			csvData.add(row);
		}

		return csvData;
	}
}

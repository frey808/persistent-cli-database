/**
 * Filename: CSVToJSON.java
 * Description: File implementing the convertToJSON and convertToXML methods
 * 		from the CSVAdapter interface. convertToXML is only stubbed.
 * GoF Pattern: Adapter 
 * GoF Role: ConcreteAdapter
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.CSV;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class CSVToJSON implements CSVAdapter {
	
	@Override
	public String convertToJSON(List<String[]> data) {
		if (data.isEmpty()) {
			throw new IllegalArgumentException("Data cannot be empty");
		}
		JSONArray jsonData = new JSONArray();
		String[] header = data.remove(0);
		List<JSONObject> jsonList = new ArrayList<>();

		for (String[] row : data) {
			JSONObject jsonObject = new JSONObject();
			for (int i = 0; i < header.length; i++) {
				jsonObject.put(header[i], row[i]);
			}
			jsonList.add(jsonObject);
		}
		JSONArray sortedData = new JSONArray();
		jsonList.stream()
				.sorted(Comparator.comparing(jsonObject -> jsonObject.getString("NDB_No")))
				.forEach(sortedData::put);

		return sortedData.toString();
	}

	@Override
	public String convertToXML(List<String[]> data) {
		return null;
	}
}

/**
 * Filename: CSVToXML.java
 * Description: File implementing the convertToJSON and convertToXML methods
 * 		from the CSVAdapter interface. convertToJSON is only stubbed.
 * GoF Pattern: Adapter 
 * GoF Role: ConcreteAdapter
 * @author Peter Carbone pjc7686
 */

package Database.Conversion.CSV;

import java.util.List;

public class CSVToXML implements CSVAdapter {
	
	@Override
	public String convertToXML(List<String[]> data) {
		if (data.isEmpty()) {
			throw new IllegalArgumentException("Data cannot be empty");
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<data>\n");
		String[] header = data.remove(0);
	
		for (String[] row : data) {
			stringBuilder.append("\t<row>\n");
	
			for (int i = 0; i < header.length; i++) {
				stringBuilder.append("\t\t<");
				stringBuilder.append(header[i].replaceAll("[()]", "").replace("µ", "micro"));
				stringBuilder.append(">");
				stringBuilder.append(row[i].replaceAll("[()]", "").replace("µ", "micro"));
				stringBuilder.append("</");
				stringBuilder.append(header[i].replaceAll("[()]", "").replace("µ", "micro"));
				stringBuilder.append(">\n");
			}
	
			stringBuilder.append("\t</row>\n");
		}
		stringBuilder.append("</data>\n");
		return stringBuilder.toString();
	}

	@Override
	public String convertToJSON(List<String[]> data) {
		return null;
	}
}

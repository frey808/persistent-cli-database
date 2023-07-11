/**
 * Filename: CSVAdapter.java
 * Description: Interface defining method to import an XML file to a CSV file
 * GoF Pattern: Adapter
 * GoF Role: Adapter
 * @author Peter Carbone pjc7686
 */
package Database.Conversion.XML;

import java.io.File;
import java.util.List;

public interface XMLAdapter {
	
	public List<String[]> convertToCSV(File xmlFile);
}

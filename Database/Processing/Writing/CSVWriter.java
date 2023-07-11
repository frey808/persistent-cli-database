/**
 * Filename: CSVWriter.java
 * Description: Class for writing data to csv file
 * GoF Pattern: Strategy
 * GoF Role: ConcreteStrategy
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Writing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter implements WriteStrategy{
    
    @Override
    public void writeFile(File file, List<String[]> data) throws IOException {
        try(FileWriter writer = new FileWriter(file)) {
            for(String[] row: data) {
                for(int i = 0; i < row.length; i++) {
                    writer.append(row[i]);
                    if(i != row.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
        }
    }
}

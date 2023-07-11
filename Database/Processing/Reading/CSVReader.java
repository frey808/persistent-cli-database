/**
 * Filename: CSVReader.java
 * Description: Class for reading csv files
 * GoF Pattern: Strategy
 * GoF Role: ConcreteStrategy
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Reading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements ReadStrategy{

    public CSVReader() {

    }

    @Override
    public List<String[]> readFile(File file) throws IOException {
        List<String[]> data = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while((line = br.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        }
        return data;
    }
}

/**
 * Filename: ReadContext.java
 * Description: Class for managing file reading context
 * GoF Pattern: Strategy
 * GoF Role: Context
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Reading;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReadContext {
    private ReadStrategy reader;

    public void setReadStrategy(ReadStrategy reader) {
        this.reader = reader;
    }

    public List<String[]> parseFile(File file) throws IOException {
        return reader.readFile(file);
    }
}

/**
 * Filename: ReadStrategy.java
 * Description: Interface defining readFile method
 * GoF Pattern: Strategy
 * GoF Role: Strategy
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Reading;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ReadStrategy {
    List<String[]> readFile(File file) throws IOException;
}

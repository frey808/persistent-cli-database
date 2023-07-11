/**
 * Filename: WriteStrategy.java
 * Description: Interface defining writeFile method
 * GoF Pattern: Strategy
 * GoF Role: Strategy
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Writing;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface WriteStrategy {
    public void writeFile(File file, List<String[]> data) throws IOException;
}

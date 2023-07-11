/**
 * Filename: WriteContext.java
 * Description: Class for managing database writing context
 * GoF Pattern: Strategy
 * GoF Role: Context
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Writing;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WriteContext {
    private WriteStrategy writer;
    
    public void setWriteStrategy(WriteStrategy writer) {
        this.writer = writer;
    }

    public void writeFile(File file, List<String[]> data) throws IOException {
        writer.writeFile(file, data);
    }
}

package Main;

import DataStructures.DrawingInstanceFromFile;
import Storage.Reader;

import java.io.File;
import java.io.IOException;

/**
 * This class is used to separate the draw panel form the logic of the program.
 */
public class ControllerDrawing {

    /**
     * This method imports a given file.
     *
     * @param file the file to read.
     * @return the content of the given file
     * @throws IOException if reading does not work out.
     */
    public DrawingInstanceFromFile importFile(File file) throws IOException {
        return Reader.read(file);
    }

}

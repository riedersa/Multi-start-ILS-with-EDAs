package Main;

import GUI.GUI;

import java.io.IOException;
import java.text.ParseException;

/**
 * This is the main class.
 */
public class Main {

    /**
     * This is the main function. It starts the program.
     *
     * @param args The first argument should be the path to the file. Afterwards one can write UMDA if one wants to use
     *             the UMDA instead of the edge based EDA, and OrOpt makes the algorithm use OrOpt instead of 2Opt
     * @throws IOException    if the file cannot be read
     * @throws ParseException this exception is thrown, if some parsing for the GUI gos wrong.
     */
    public static void main(String[] args) throws IOException, ParseException {
        GUI.create();
    }

}

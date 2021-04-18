package assignment07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2020/12/02
 */
public class PathFinderTest {

    public static void main(String[] args) throws IOException {
        String stringFile = readFileAsString("src/assignment07/mazes/bigMaze.txt");
        String outputString = "";
        System.out.println("Input: \n" + stringFile);
        PathFinder.solveMaze(stringFile, outputString);
    }

    // This method reads in a file name and returns the string version of that file
    public static String readFileAsString(String fileName) {
        String stringFile = "";
        try {
            stringFile = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringFile;
    }

}

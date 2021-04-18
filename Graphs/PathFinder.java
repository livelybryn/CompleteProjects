package assignment07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.function.Supplier;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2020/11/30
 */
public class PathFinder {

    public static void solveMaze(String inputFile, String outputFile) throws IOException {
        Reader inputReader = new StringReader(inputFile);
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        // create graph
        Graph<Character> graph = createGraphData(bufferedReader);
        // connect the nodes on a graph
        graph.connectNodes();
        // find the shortest path
        graph.bfs();
        // if there is a path, change spaces to show path
        if (graph.goal().cameFrom() != null) {
            graph.changePath();
        }
        outputFile = rebuildStringFile(graph);
        System.out.println("Output: " + outputFile);
    }

    /**
     * This method takes a bufferedReader and reads in a string file, putting each character in a separate node
     * @param bufferedReader the bufferedReader reading in the string file
     * @return a graph with the data from the string file
     * @throws IOException
     */
    public static Graph createGraphData(BufferedReader bufferedReader)
        throws IOException {

        // reads in the height and width
        String[] firstLine = bufferedReader.readLine().split(" ");
        int height = Integer.parseInt(firstLine[0]);
        int width = Integer.parseInt(firstLine[1]);

        Node<Character>[][] graphData = new Node[height][width];
        Graph<Character> graph = new Graph<Character>(graphData, height, width);

        // loops through all characters
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char map = (char) bufferedReader.read();

                if (map == 'X') { // Walls are null
                    graphData[i][j] = null;
                } else {
                    Node<Character> newNode = new Node<>(map);
                    graphData[i][j] = newNode;
                    if (map == 'S') {
                        graph.setStart(newNode);
                    } else if (map == 'G') {
                        graph.setGoal(newNode);
                    }
                }
            }
            bufferedReader.read();
        }
        return graph;
    }

    /**
     * This method creates a new string file from the updated graph data after a path has been found
     * @param graph a graph containing the data
     * @return a string with the completed map
     */
    public static String rebuildStringFile(Graph graph) {
        StringBuilder output = new StringBuilder();

        // adds the height and width to the stringBuilder
        output.append(graph.height());
        output.append(' ');
        output.append(graph.width());
        output.append('\n');

        for (int i = 0; i < graph.height(); i++) {
            for (int j = 0; j < graph.width(); j++) {
                if (graph.data()[i][j] == null) {
                    output.append('X');
                } else {
                    output.append(graph.data()[i][j].data());
                }
            }
            output.append('\n');
        }
        return output.toString();
    }

}

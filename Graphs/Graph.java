package assignment07;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2020/11/30
 *
 */
public class Graph<E> {

    private final int HEIGHT;
    private final int WIDTH;
    private Node<E> start;
    private Node<E> goal;
    private Node<E>[][] data;


    public Graph(Node<E>[][] graph, int height, int width) {
        this.data = graph;
        this.HEIGHT = height;
        this.WIDTH = width;
    }

    /**
     * This method connects the nodes in a graph. If the node is null, it is not connected. Graph are
     * connected with their top, bottom, left, and right neighbor
     */
    public void connectNodes() {
        for (int i = 0; i < HEIGHT; i++) { // loops through the height
            for (int j = 0; j < WIDTH; j++) { // loops through the width
                if (data[i][j] != null) {
                    if (i > 0 && data[i - 1][j] != null) {
                        data[i][j].setTop(data[i - 1][j]);
                    }
                    if (i < HEIGHT - 1 && data[i + 1][j] != null) {
                        data[i][j].setBottom(data[i + 1][j]);
                    }
                    if (j > 0 && data[i][j] != null) {
                        data[i][j].setLeft(data[i][j-1]);
                    }
                    if (j < WIDTH - 1 && data[i][j] != null) {
                        data[i][j].setRight(data[i][j+1]);
                    }
                }
            }
        }
    }

    /**
     * THis method uses breadth-first search to search through a graph and find the shortest path from
     * the start to the goal
     */
    public void bfs() {
        Queue<Node<E>> queue = new LinkedList();

        Set<Node<E>> visited = new HashSet<>();
        queue.add(start);
        while (!queue.isEmpty()) { // while queue is not empty
            Node<E> current = queue.remove(); // get first node in queue
            if (visited.contains(current)) { // if node is null or already visited, skip
                continue;
            }
            if (current == goal) { // if node is goal, stop and return
                break;
            } else { // add neighbors to queue if not visited and add current node to visited
                visited.add(current);
                // add all neighbors if they aren't null and haven't been visited
                addToQueue(queue, visited, current.bottom(), current);
                addToQueue(queue, visited,  current.top(), current);
                addToQueue(queue, visited, current.left(), current);
                addToQueue(queue, visited, current.right(), current);
            }
        }
    }

    /**
     * This method adds neighbors to a queue if they are not null and have not already been visited.
     * The node the neighbor came from is also set as the cameFrom node.
     * @param queue the queue to add nodes
     * @param visited the set holding which nodes have been visited or not
     * @param neighbor the neighbor (edge) of the current node
     * @param current the current node
     */
    public void addToQueue(Queue<Node<E>> queue, Set<Node<E>> visited, Node<E> neighbor, Node<E> current)  {
        // if neighbor is wall and isn't in visited and isn't already in the queue
        if (neighbor != null && !visited.contains(neighbor) && !queue.contains(neighbor)) {
            queue.add(neighbor);
            neighbor.setCameFrom(current);
        }
    }

    /**
     * This method follows the cameFrom path from goal to start and changes ' ' to '.' showing the path
     * from start to goal
     */
    public void changePath() {
        Node<E> current = goal;
        current = current.cameFrom();
        Character character = '.';
        while (current != start) {
            current.setData((E) character);
            current = current.cameFrom();
        }
    }

    public Node<E> start() {
        return start;
    }

    public void setStart(Node<E> start) {
        this.start = start;
    }

    public Node<E> goal() {
        return goal;
    }

    public void setGoal(Node<E> goal) {
        this.goal = goal;
    }

    public int height() {
        return HEIGHT;
    }

    public int width() {
        return WIDTH;
    }

    public Node<E>[][] data() {
        return data;
    }

}

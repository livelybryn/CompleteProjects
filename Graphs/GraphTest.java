package assignment07;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2020/12/03
 */
class GraphTest {

    Node<Character>[][] data = new Node[5][5];
    Graph<Character> graph = new Graph<>(data, 5, 5);

    @BeforeEach
    void setUp() {

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                graph.data()[i][j] = new Node<>(' ');
            }
        }

        graph.setStart(graph.data()[2][2]);
        graph.start().setData('S');

        graph.setGoal(graph.data()[4][4]);
        graph.goal().setData('G');

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void connectNodes() {
        graph.connectNodes();
        assertEquals(graph.data()[0][0], graph.data()[1][0].top());
        assertEquals(graph.data()[0][0], graph.data()[0][1].left());

        assertEquals(graph.data()[3][3], graph.data()[3][4].left());
        assertEquals(graph.data()[3][3], graph.data()[3][2].right());
        assertEquals(graph.data()[3][3], graph.data()[2][3].bottom());
        assertEquals(graph.data()[3][3], graph.data()[4][3].top());

    }

    @Test
    void bfs() {
        graph.connectNodes();
        graph.bfs();
        assertEquals(graph.data()[4][3], graph.goal().cameFrom());
        assertEquals(graph.data()[4][2], graph.data()[4][3].cameFrom());
        assertEquals(graph.data()[3][2], graph.data()[4][2].cameFrom());
        assertEquals(graph.data()[2][2], graph.data()[3][2].cameFrom());
        assertEquals(graph.data()[2][2], graph.start());
    }

    @Test
    void addToQueue() {
        graph.connectNodes();

        Queue<Node<Character>> queue = new LinkedList();
        Set<Node<Character>> visited = new HashSet<>();

        graph.addToQueue(queue, visited, graph.data()[0][0], graph.data()[0][1]);
        assertTrue(queue.contains(graph.data()[0][0]));
        assertFalse(queue.contains(graph.data()[0][1]));
    }

    @Test
    void changePath() {
        graph.connectNodes();
        graph.bfs();
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                System.out.print(graph.data()[i][j].data());
            }
            System.out.println();
        }

        graph.changePath();

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                System.out.print(graph.data()[i][j].data());
            }
            System.out.println();
        }
    }
}
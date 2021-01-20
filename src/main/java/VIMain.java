import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;
import parser.GraphParser;

import java.io.IOException;

public class VIMain {
    public static void main(String[] args) {
        GraphParser parser = new GraphParser("/Users/igorvinokur/Development/Dev/Study/Assignment3AI/src/main/resources/graph.txt");
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Graph<Vertex, Edge> g = parser.getG();
        System.out.println(g);
    }
}

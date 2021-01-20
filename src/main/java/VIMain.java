import ValueIteration.Node;
import ValueIteration.ValueIterationAlgorithm;
import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;
import parser.GraphParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VIMain {
    public static void main(String[] args) {
//        GraphParser parser = new GraphParser("/Users/igorvinokur/Development/Dev/Study/Assignment3AI/src/main/resources/graph.txt");
        GraphParser parser = new GraphParser("/Users/igorvinokur/Downloads/Assignment3AI/src/main/resources/graph.txt");
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Graph<Vertex, Edge> g = parser.getG();
//        System.out.println(g);
        ValueIterationAlgorithm valueIterationAlgorithm = new ValueIterationAlgorithm();
        List<Node> network = valueIterationAlgorithm.generateBeliefNetwork(parser.getBlocked1(),
                parser.getBlocked2(),
                parser.getProbabilities(),
                g, new ArrayList<>(), 0, new ArrayList<>());

        System.out.println(network);
        List<Node> newNetwork = valueIterationAlgorithm.valueIteration(network, g, parser.getTargetVertex());
        System.out.println(network);
    }
}

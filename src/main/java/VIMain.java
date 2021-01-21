import ValueIteration.Node;
import ValueIteration.ValueIterationAlgorithm;
import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;
import parser.GraphParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VIMain {
    public static void main(String[] args) {
        GraphParser parser = new GraphParser("/Users/igorvinokur/Development/Dev/Study/Assignment3AI/src/main/resources/graph.txt");
//        GraphParser parser = new GraphParser("/Users/igorvinokur/Downloads/Assignment3AI/src/main/resources/graph.txt");
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
                g, new ArrayList<>(), 0);
        valueIterationAlgorithm.updateNeighbors(g, network, parser.getBlocked1(),
                parser.getBlocked2(),
                parser.getProbabilities());
        List<Node> newNetwork = valueIterationAlgorithm.valueIteration(network, g, parser.getTargetVertex());
        System.out.println(newNetwork);
        System.out.println("Network size: " + network.size());
        newNetwork.sort(Comparator.comparing(o -> o.getLocation().getId()));
        for (Node n : newNetwork) {
            n.print();
        }
        System.out.println("Optimal path: ");
        Vertex v = parser.getStartVertex();
        System.out.print("Start: " + v.getId());
        do {
            Node gt = null;
            double mv = Double.NEGATIVE_INFINITY;
            for (Node n : network) {
                if (n.getLocation().getId() == v.getId() && mv < n.getValue()) {
                    gt = n.getNext();
                    mv = n.getValue();
                }
            }
            if (gt == null) {
                System.out.println("UNREACHABLE");
                break;
            }
            else {
                System.out.print(" -> " + gt.getLocation().getId());
                v = gt.getLocation();
            }
        } while (v.getId() != parser.getTargetVertex().getId());
        System.out.println(" :Target");
    }
}

package ValueIteration;

import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;

public class ValueIterationAlgorithm {


    public List<Node> valueIteration(List<Node> network, Graph<Vertex, Edge> g, Vertex goal) {
        boolean somethingChanged = false;

        do {
            somethingChanged = false;

            for (Node n : network) {
                somethingChanged = somethingChanged || n.update(g);
            }
        } while (somethingChanged);

        return network;
    }

    /**
     *
     * @param blocked1 - vertex 1 of blocked edge
     * @param blocked2 - vertex 2 of blocked edge
     * @param probabilities
     * @param g
     * @param carry - belief state
     * @param iterator
     * @return
     */
    public List<Node> generateBeliefNetwork(List<Vertex> blocked1, List<Vertex> blocked2, List<Double> probabilities, Graph<Vertex, Edge> g, List<BeliefAboutConnection> carry, int iterator, List<Node> network) {
        if (blocked1.size() == iterator) {
            return generateBeliefFromGraphAndCarry(g, carry, network);
        }
        Vertex v1 = blocked1.get(iterator);
        Vertex v2 = blocked2.get(iterator);
        double probability = probabilities.get(iterator);
        carry.add(new BeliefAboutConnection(v1, v2, StateOfConnection.OPEN, probability));
        network.addAll(generateBeliefNetwork(blocked1, blocked2, probabilities, g, carry, iterator + 1, network));
        carry.remove(carry.size() - 1);
        carry.add(new BeliefAboutConnection(v1, v2, StateOfConnection.CLOSED, probability));
        network.addAll(generateBeliefNetwork(blocked1, blocked2, probabilities, g, carry, iterator + 1, network));
        carry.remove(carry.size() - 1);
        carry.add(new BeliefAboutConnection(v1, v2, StateOfConnection.UNKNOWN, probability));
        network.addAll(generateBeliefNetwork(blocked1, blocked2, probabilities, g, carry, iterator + 1, network));

        updateNeighbors(g, network);
        return network;
    }

    private List<Node> generateBeliefFromGraphAndCarry(Graph<Vertex, Edge> g, List<BeliefAboutConnection> carry, List<Node> network) {
        List<Node> netPart = new ArrayList<>();
        for (Vertex v : g.vertexSet()) {
            BeliefAboutConnection bac = findInCarry(carry, v);
            if (bac != null) {
                netPart.addAll(createNodeFromCarry(g, bac));
            }
            else {
                netPart.addAll(createNodeFromVertex(g, v));
            }
        }
        return netPart;
    }

    private List<Node> createNodeFromVertex(Graph<Vertex, Edge> g, Vertex vertex) {
        List<Node> nodes = new ArrayList<>();
        List<BeliefAboutConnection> beliefs = new ArrayList<>();
        for (Vertex v : g.vertexSet()) {
            if (v.getId() != vertex.getId()) {
                Edge e = g.getEdge(vertex, v);
                if (e != null) {
                    BeliefAboutConnection b = new BeliefAboutConnection(vertex, v, StateOfConnection.OPEN, 1);
                    beliefs.add(b);
                }
            }
        }
        BeliefAboutConnection []bs = new BeliefAboutConnection[beliefs.size()];
        nodes.add(new Node(vertex, beliefs.toArray(bs)));
        return nodes;
    }

    private void updateNeighbors(Graph<Vertex, Edge> g, List<Node> network) {
        for (Node n : network) {
            List<Node> neighbords = new ArrayList<>();
            for (Vertex v : g.vertexSet()) {
                if (v.getId() != n.location.getId()) {
                    Edge e = g.getEdge(n.location, v);
                    if (e != null) {
                        for (Node nn : network) {
                            if (nn.location.getId() == v.getId()) {
                                neighbords.add(nn);
                                break;
                            }
                        }
                    }
                }
            }
            n.neighbors = neighbords;
        }
    }

    private List<Node> createNodeFromCarry(Graph<Vertex, Edge> g, BeliefAboutConnection bac) {
        List<Node> nodes = new ArrayList<>();
        List<BeliefAboutConnection> beliefs = new ArrayList<>();
        beliefs.add(bac);
        for (Vertex v : g.vertexSet()) {
            if (v.getId() != bac.v1.getId() && v.getId() != bac.v2.getId()) {
                Edge e = g.getEdge(bac.v1, v);
                if (e != null) {
                    BeliefAboutConnection b = new BeliefAboutConnection(bac.v1, v, StateOfConnection.OPEN, 1);
                    beliefs.add(b);
                }
            }
        }
        BeliefAboutConnection []bs = new BeliefAboutConnection[beliefs.size()];
        nodes.add(new Node(bac.v1, beliefs.toArray(bs)));
        return nodes;
    }

    private BeliefAboutConnection findInCarry(List<BeliefAboutConnection> carry, Vertex v) {
        for (BeliefAboutConnection bac : carry) {
            if (bac.v1.getId() == v.getId() || bac.v2.getId() == v.getId()) {
                return bac;
            }
        }
        return null;
    }
}

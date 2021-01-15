package ValueIteration;

import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;

import java.util.List;
import java.util.Stack;

public class ValueIterationAlgorithm {


    public List<Node> valueIteration(List<Node> network, Graph<Vertex, Edge> g, Vertex goal){
        boolean somethingChanged=false;

        do {
            somethingChanged=false;

            for (Node n:
                    network) {
                somethingChanged=somethingChanged||n.update(g);
            }
        }while(somethingChanged);

        return network;
    }



    public List<Node> generateBeliefNetwork(List<Vertex> blocked1, List<Vertex> blocked2, List<Double> probabilities, Graph<Vertex,Edge> g, List<BeliefAboutConnection> carry, int iterator ){
            if(blocked1.size()==iterator){
            return generateBeliefFromGraphAndCarry(g, carry);
        }
        Vertex v1=blocked1.get(iterator);
        Vertex v2=blocked2.get(iterator);
        double probability=probabilities.get(iterator);
        carry.add(new BeliefAboutConnection(v1,v2,StateOfConnection.OPEN,probability));
        List<Node> ret = generateBeliefNetwork(blocked1,blocked2,probabilities, g,carry,iterator+1);
        carry.remove(carry.size()-1);
        carry.add(new BeliefAboutConnection(v1,v2,StateOfConnection.CLOSED, probability));
        ret.addAll(generateBeliefNetwork(blocked1,blocked2,probabilities, g,carry,iterator+1));
        carry.remove(carry.size()-1);
        carry.add(new BeliefAboutConnection(v1,v2,StateOfConnection.UNKNOWN, probability));
        ret.addAll(generateBeliefNetwork(blocked1,blocked2, probabilities,g,carry,iterator+1));
        return ret;





    }
}

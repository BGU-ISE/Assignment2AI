package BeliefNetwork;

import java.util.HashMap;
import java.util.LinkedList;

public class EvacuteesNode extends BeliefNode {
    public EvacuteesNode(double probabilityOfExistence){
        this.originalProbability=probabilityOfExistence;
        this.parents=new LinkedList<>();
        this.children=new LinkedList<>();
        this.messages=new HashMap<>();
    }

    @Override
    public void computeProbability() {

    }

}

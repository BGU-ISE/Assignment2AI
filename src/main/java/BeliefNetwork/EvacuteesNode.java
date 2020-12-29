package BeliefNetwork;

import java.util.HashMap;
import java.util.LinkedList;

public class EvacuteesNode extends BeliefNode {

    public EvacuteesNode(Integer id, double probabilityOfExistence){
        super(id);
        this.originalProbability=probabilityOfExistence;
        this.parents=new LinkedList<>();
        this.children=new LinkedList<>();
        this.messages=new HashMap<>();
    }

    @Override
    public void computeProbability() {


    }


}

package BeliefNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BeliefNode {
    protected Integer id;
    protected List<BeliefNode> children;
    protected List<BeliefNode> parents;
    protected HashMap<BeliefNode,Double> messages;
    protected NodeState state; //unknown\true\false
    protected double originalProbability;
    protected double[][] probabilityTable=new double[2][2];
    protected double currentProbability;

    protected BeliefNode(Integer id) {
        this.id = id;
        parents = new ArrayList<>();
        children = new ArrayList<>();
        messages = new HashMap<>();
    }

    public static void parentChild(BeliefNode parent,BeliefNode child){
        parent.children.add(child);
        child.messages.put(parent,parent.probability());

    }
    public void recieveMessage(BeliefNode parent, double message){
        if(parents.contains(parent)){
            messages.put(parent,message);
            computeProbability();
            updateAllChildren();
        }

    }

    public void addEvidence(double evidence){
        currentProbability=evidence;
        updateAllChildren();
    }

    public abstract void computeProbability();

    protected void updateAllChildren(){
        for (BeliefNode child:
                children) {
            child.recieveMessage(this, this.probability());
        }
    }

    public  double probability(){
        return currentProbability;
    }

    public void clear(){
        currentProbability=originalProbability;
        updateAllChildren();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "VERTEX " + id + ":\n" +
                "\t P(Evacuees " + id + ") = " + probability() + "\n" +
                "\t P(not Evacuees " + id +") = " + (1 - probability()) + "\n\n";
    }
}

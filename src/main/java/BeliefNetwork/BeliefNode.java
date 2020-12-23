package BeliefNetwork;

import java.util.HashMap;
import java.util.List;

public abstract class BeliefNode {
    protected List<BeliefNode> children;
    protected List<BeliefNode> parents;
    protected HashMap<BeliefNode,Double> messages;
    protected NodeState state; //unknown\true\false
    protected double originalProbability;
    protected double[][] probabilityTable=new double[2][2];
    protected double currentProbability;

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

}

package BeliefNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
public abstract class BeliefNode {
    protected Integer id;
    protected List<BeliefNode> children;
    protected List<BeliefNode> parents;
    protected HashMap<BeliefNode,Double> messages;
    protected double originalProbability;
    protected double[][] probabilityTable=new double[2][2];
    protected double value;
    protected boolean isConstant=false;
    protected int numberUpdated=0;
    protected BeliefNode(Integer id) {
        this.id = id;
        parents = new ArrayList<>();
        children = new ArrayList<>();
        messages = new HashMap<>();
    }

    public void startMonteCarlo(){
        if(!isConstant) {
            computeProbability();
            Random rand = new Random();
            value = 0;
            if (rand.nextDouble() < originalProbability) {
                value = 1;
            }
            for (BeliefNode child :
                    children) {
                child.propagateMonteCarlo(value, this);
            }
        }
        else{
            for (BeliefNode child :
                    children) {
                child.propagateMonteCarlo(value, this);
            }
        }
    }

    public void addChild(BeliefNode child){
        children.add(child);
    }

    public void propagateMonteCarlo(double value, BeliefNode parent  ){

        if (parents.contains(parent)) {
            numberUpdated = numberUpdated + 1;
            if (numberUpdated == parents.size()) {
                if (!isConstant) {
                    messages.put(parent, value);
                    computeProbability();
                    Random rand = new Random();
                    value = 0;
                    if (rand.nextDouble() < originalProbability) {
                        value = 1;
                    }
                    for (BeliefNode child :
                            children) {
                        child.propagateMonteCarlo(value, this);
                    }


                } else {
                    for (BeliefNode child :
                            children) {
                        child.propagateMonteCarlo(value, this);
                    }
                }
            }
        }
    }


    public void addEvidence(double evidence){
        value=evidence;
        isConstant=true;
    }

    public abstract void computeProbability();



    public  double value(){
        return value;
    }

    public double probability(){
        return originalProbability;
    }

    public void clear(){
        value=-1;
        isConstant=false;
        numberUpdated=0;
        for (BeliefNode parent:
             parents) {
            messages.put(parent, -1.0);
        }
        for(BeliefNode child: children){
            child.clear();
        }


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
                "\t P(Evacuees " + id + ") = " + value() + "\n" +
                "\t P(not Evacuees " + id +") = " + (1 - value()) + "\n\n";
    }
}

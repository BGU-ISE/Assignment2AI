package BeliefNetwork;

public class BlockNode extends BeliefNode {

    private Integer time;

    public BlockNode(Integer id, BeliefNode parent1, BeliefNode parent2, double weight, Integer time){
        super(id);
        this.time = time;
        parents.add(parent1);
        parents.add(parent2);
        messages.put(parent1,parent1.probability());
        messages.put(parent2,parent2.probability());
        probabilityTable[0][0]=0.001;
        probabilityTable[1][0] = (0.6 / weight);
        probabilityTable[0][1]=(0.6/weight);
        probabilityTable[1][1]=1-(1-0.6/weight)*(1-0.6/weight);
        computeProbability();
    }

    public BlockNode(Integer id, BeliefNode parent,double persistenceConstant, Integer time){
        super(id);
        this.time = time;
        parents.add(parent);
        messages.put(parent,parent.probability());

        probabilityTable[0][0]=0.001;
        probabilityTable[1][0]=persistenceConstant;
        probabilityTable[0][1]=0.0;
        probabilityTable[1][1]=0.0;
        computeProbability();
    }

    public void computeProbability(){
        if(parents.size()==2){
            double prob1= messages.get( parents.get(0));
            double prob2= messages.get( parents.get(1));
            double positiveProbability=prob1*prob2*probabilityTable[1][1]+(1-prob1)*prob2*probabilityTable[0][1]+prob1*(1-prob2)*probabilityTable[1][0]+
                    (1-prob1)*(1-prob2)*probabilityTable[0][0];
            double negativeProbability=prob1*prob2*(1-probabilityTable[1][1])+(1-prob1)*prob2*(1-probabilityTable[0][1])+prob1*(1-prob2)*(1-probabilityTable[1][0])+
                    (1-prob1)*(1-prob2)*(1-probabilityTable[0][0]);
            double normalizationFactor=1/(positiveProbability+negativeProbability);
            originalProbability=normalizationFactor*positiveProbability;
        }
        else{

            double prob1= messages.get( parents.get(0));
            double prob2= 0;
            double positiveProbability=prob1*(1-prob2)*probabilityTable[1][0]+
                    (1-prob1)*(1-prob2)*probabilityTable[0][0];
            double negativeProbability=prob1*(1-prob2)*(1-probabilityTable[1][0])+
                    (1-prob1)*(1-prob2)*(1-probabilityTable[0][0]);
            double normalizationFactor=1/(positiveProbability+negativeProbability);
            originalProbability=normalizationFactor*positiveProbability;
        }
    }

    @Override
    public String toString() {
        if (parents.size() == 1) {
            return "EDGE " + id + ", time " + time + ":\n" +
                    "\tP(Blocakge " + id + " | not Evacuees " + parents.get(0) .id + ") = " + probabilityTable[0][0] + "\n" +
                    "\tP(Blocakge " + id + " | Evacuees " + parents.get(0) .id + ") = " + probabilityTable[1][0] + "\n\n";
        }
        return "EDGE " + id + ", time " + time + ":\n" +
                "\tP(Blocakge " + id + " | not Evacuees " + parents.get(0) .id + ", not Evacuees " + parents.get(1) .id + ") = " + probabilityTable[0][0] + "\n" +
                "\tP(Blocakge " + id + " | Evacuees " + parents.get(0) .id + ", not Evacuees " + parents.get(1) .id + ") = " + probabilityTable[1][0] + "\n" +
                "\tP(Blocakge " + id + " | not Evacuees " + parents.get(0) .id + ", Evacuees " + parents.get(1) .id + ") = " + probabilityTable[0][1] + "\n" +
                "\tP(Blocakge " + id + " | Evacuees " + parents.get(0) .id + ", Evacuees " + parents.get(1) .id + ") = " + probabilityTable[1][1] + "\n\n";
    }

    public Integer getTime() {
        return time;
    }
}

package BeliefNetwork;

public class BlockNode extends BeliefNode{


    public BlockNode(BeliefNode parent1, BeliefNode parent2,double weight){
        parents.add(parent1);
        parents.add(parent2);
        messages.put(parent1,parent1.probability());
        messages.put(parent2,parent2.probability());
        probabilityTable[0][0]=0.001;
        probabilityTable[1][0]=(0.6/weight);
        probabilityTable[0][1]=(0.6/weight);
        probabilityTable[1][1]=1-(1-0.6/weight)*(1-0.6/weight);
        computeProbability();
    }

    public BlockNode(BeliefNode parent,double persistenceConstant){
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
}

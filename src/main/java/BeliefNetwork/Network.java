package BeliefNetwork;

import BeliefNetwork.data.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Network {
    private List<BeliefNode> nodes;
    private HashMap<String, Double> evidences = new HashMap<>();

    public Network() {
        nodes = new ArrayList<>();
    }

    public Network(List<BeliefNode> nodes) {
        this.nodes = nodes;
    }

    public void ask(Query query) {
        switch (query.getOperation()) {
            case EdgeProb: {
                List<BeliefNode> evacuteesNodes = new ArrayList<>();
                for (int i=0; i<nodes.size(); i++) {
                    if (nodes.get(i) instanceof EvacuteesNode) {
                        evacuteesNodes.add(nodes.get(i));
                    }
                }
                List<BeliefNode> evid = new ArrayList<>();
                List<Double> evidV = new ArrayList<>();
                for (String k : evidences.keySet()) {
                    for (BeliefNode n : nodes) {
                        if (k.charAt(0) == 'E') {
                            if (n instanceof BlockNode && n.getId() == Integer.parseInt("" + k.charAt(1))) {
                                evid.add(n);
                                evidV.add(evidences.get(k));
                                break;
                            }
                        }
                        else if (k.charAt(0) == 'V') {
                            if (n instanceof EvacuteesNode && n.getId() == Integer.parseInt("" + k.charAt(1))) {
                                evid.add(n);
                                evidV.add(evidences.get(k));
                                break;
                            }
                        }
                    }
                }
                for (BeliefNode n : nodes) {
                    if (n.getId().equals( query.getItemId()) && n instanceof BlockNode) {
                        double prob = importanceSampling(evacuteesNodes, evid, evidV, n);
                        System.out.println("Edge " + n.getId() + " probability is: " + prob);
                        break;
                    }
                }
                break;
            }
            case PathProb:{
                List<BeliefNode> evacuteesNodes = new ArrayList<>();
                for (int i=0; i<nodes.size(); i++) {
                    if (nodes.get(i) instanceof EvacuteesNode) {
                        evacuteesNodes.add(nodes.get(i));
                    }
                }
                List<BeliefNode> evid = new ArrayList<>();
                List<Double> evidV = new ArrayList<>();
                for (String k : evidences.keySet()) {
                    for (BeliefNode n : nodes) {
                        if (k.charAt(0) == 'E') {
                            if (n instanceof BlockNode && n.getId() == Integer.parseInt("" + k.charAt(1))) {
                                evid.add(n);
                                evidV.add(evidences.get(k));
                                break;
                            }
                        }
                        else if (k.charAt(0) == 'V') {
                            if (n instanceof EvacuteesNode && n.getId() == Integer.parseInt("" + k.charAt(1))) {
                                evid.add(n);
                                evidV.add(evidences.get(k));
                                break;
                            }
                        }
                    }
                }
                Double totalProbability = 1.0;
                for (Integer e : query.getEdges()) {
                    for (BeliefNode n : nodes) {
                        if (n.getId().equals(e) && n instanceof BlockNode) {
                            //System.out.println(n);
                            double prob = 1 - importanceSampling(evacuteesNodes, evid, evidV, n);
                            evid.add(n);
                            evidV.add(prob);
                            totalProbability *= prob;
                            break;
                        }
                    }
                }
                System.out.println("Path probability is: " + totalProbability);
                break;
            }
            case VertexProb: {
                    List<BeliefNode> evacuteesNodes = new ArrayList<>();
                    for (int i=0; i<nodes.size(); i++) {
                        if (nodes.get(i) instanceof EvacuteesNode) {
                            evacuteesNodes.add(nodes.get(i));
                        }
                    }
                    List<BeliefNode> evid = new ArrayList<>();
                    List<Double> evidV = new ArrayList<>();
                    for (String k : evidences.keySet()) {
                        for (BeliefNode n : nodes) {
                            if (k.charAt(0) == 'E') {
                                if (n instanceof BlockNode && n.getId() == Integer.parseInt("" + k.charAt(1))) {
                                    evid.add(n);
                                    evidV.add(evidences.get(k));
                                    break;
                                }
                            }
                            else if (k.charAt(0) == 'V') {
                                if (n instanceof EvacuteesNode && n.getId() == Integer.parseInt("" + k.charAt(1))) {
                                    evid.add(n);
                                    evidV.add(evidences.get(k));
                                    break;
                                }
                            }
                        }
                    }
                    for (BeliefNode n : nodes) {
                        if (n.getId().equals(query.getItemId()) && n instanceof EvacuteesNode) {
                            //System.out.println(n);
                            double prob = importanceSampling(evacuteesNodes, evid, evidV, n);
                            System.out.println("Vertex " + n.getId() + " probability is: " + prob);
                            break;
                        }
                    }
                    break;
            }
            case ResetEvidence:
                for (BeliefNode n : nodes) {
                    n.clear();
                }
                evidences.clear();
                break;
            case AddEdgeEvidence:
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).getId().equals( query.getItemId()) && nodes.get(i) instanceof BlockNode) {
                        nodes.get(i).addEvidence(query.getValue());
                        evidences.put("E" + nodes.get(i).getId(), query.getValue());
                        break;
                    }
                }
                break;
            case AddNodeEvidence:
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).getId().equals(query.getItemId()) && nodes.get(i) instanceof EvacuteesNode) {
                        nodes.get(i).addEvidence(query.getValue());
                        evidences.put("V" + nodes.get(i).getId(), query.getValue());
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    public void updateEvidence(List<BeliefNode> l1, List<Double> l2){
        for(int i=0;i<l1.size(); ++i){
            l1.get(i).addEvidence(l2.get(i));
        }
    }

    public double importanceSampling(List<BeliefNode> ancestors, List<BeliefNode> evidence, List<Double> evidenceValues, BeliefNode toCheck ){
        double weightsSumFalse=0;
        double weightSumTrue=0;
        for(int i=0;i < 1000; ++i){
            updateEvidence(evidence, evidenceValues);
            for (BeliefNode ancestor:
                 ancestors) {
                ancestor.startMonteCarlo();
            }
            double weight=1;
            for (BeliefNode e:
                evidence ) {
                weight=weight*e.probability();

            }
            if(toCheck.value()==1.0){
                weightSumTrue+=weight;
            }
            else{
                weightsSumFalse+=weight;
            }
            for (BeliefNode ancestor:
                    ancestors) {
                ancestor.clear();
            }

        }

        if (weightSumTrue == 0) {
            return 0;
        }

        return weightSumTrue/(weightsSumFalse+weightSumTrue);
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}

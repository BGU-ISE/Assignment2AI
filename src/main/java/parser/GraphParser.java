package parser;

import BeliefNetwork.BeliefNode;
import BeliefNetwork.Network;
import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphParser {

    private File graphFile;
    private Graph<Vertex, Edge> g;
    private HashMap<Integer, Vertex> vertexes;
    private Vertex startVertex;
    private Vertex targetVertex;

    List<Vertex> blocked1;
    List<Vertex> blocked2;
    List<Double> probabilities;

    public GraphParser(String filePath) {
        this.graphFile = new File(filePath);
        this.vertexes = new HashMap<>();
        this.g = new DefaultUndirectedWeightedGraph<>(Edge.class);
        this.blocked1 = new ArrayList<>();
        this.blocked2 = new ArrayList<>();
        this.probabilities = new ArrayList<>();
    }

    public void parse() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.graphFile));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() > 0) {
//                System.out.println(line);
                detectResource(line);
            }
        }
    }

    private void detectResource(String line) {
        if (line.startsWith("#V")) {
            String []parts = line.split("[ ]+");
            int numberOfVertexes = Integer.parseInt(parts[0].replace("#V", ""));
            for (int i = 0; i < numberOfVertexes; i++) {
                Vertex v = new Vertex(i+1);
                vertexes.put(i + 1, v);
                g.addVertex(v);
            }
        }
        else if (line.startsWith("#E")) {
            String []parts = line.split("[ ]+");
            Edge e = new Edge();
            e.setId(Integer.parseInt(parts[0].replace("#E", "")));
            int fromId = Integer.parseInt(parts[1]);
            int toId = Integer.parseInt(parts[2]);
            double weight = Integer.parseInt(parts[3].replace("W", ""));
            double probability = 0;
            if (parts[4].startsWith("B")) {
                probability = Double.parseDouble(parts[4].replace("B", ""));
                e.setProbability(probability);
                blocked1.add(vertexes.get(fromId));
                blocked2.add(vertexes.get(toId));
                probabilities.add(probability);
            }
            g.addEdge(vertexes.get(fromId), vertexes.get(toId), e);
            g.setEdgeWeight(e, weight);
        }
        else if (line.startsWith("#Start")) {
            String []parts = line.split("[ ]+");
            Integer id = Integer.parseInt(parts[1]);
            startVertex = vertexes.get(id);
        }
        else if (line.startsWith("#Target")) {
            String []parts = line.split("[ ]+");
            Integer id = Integer.parseInt(parts[1]);
            targetVertex = vertexes.get(id);
        }
    }

    public Graph<Vertex, Edge> getG() {
        return g;
    }

    public void setG(Graph<Vertex, Edge> g) {
        this.g = g;
    }

    public HashMap<Integer, Vertex> getVertexes() {
        return vertexes;
    }

    public void setVertexes(HashMap<Integer, Vertex> vertexes) {
        this.vertexes = vertexes;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }

    public Vertex getTargetVertex() {
        return targetVertex;
    }

    public void setTargetVertex(Vertex targetVertex) {
        this.targetVertex = targetVertex;
    }


    public List<Vertex> getBlocked1() {
        return blocked1;
    }

    public void setBlocked1(List<Vertex> blocked1) {
        this.blocked1 = blocked1;
    }

    public List<Vertex> getBlocked2() {
        return blocked2;
    }

    public void setBlocked2(List<Vertex> blocked2) {
        this.blocked2 = blocked2;
    }

    public List<Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(List<Double> probabilities) {
        this.probabilities = probabilities;
    }

    public Network getNetwork() {
//        HashMap<Integer, EvacuteesNode> evacuteesNodes = new HashMap<>();
//        for (DummyVertex dv : vertexes) {
//            EvacuteesNode n = new EvacuteesNode(dv.id, dv.probability);
//            evacuteesNodes.put(dv.id, n);
//        }

        List<BeliefNode> nodes = new ArrayList<>();
//        nodes.addAll(evacuteesNodes.values());
//        List<BlockNode> timeNodes = new ArrayList<>();
//        for (DummyEdge de : edges) {
//            BlockNode bn = new BlockNode(de.id, evacuteesNodes.get(de.fromId), evacuteesNodes.get(de.toId), de.weight, 0);
//            nodes.add(bn);
//            evacuteesNodes.get(de.fromId).addChild(bn);
//            evacuteesNodes.get(de.toId).addChild(bn);
//            BlockNode tbn = new BlockNode(de.id + edges.size(), bn, persistenceProd, 1);
//            timeNodes.add(tbn);
//            bn.addChild(tbn);
//        }
//        nodes.addAll(timeNodes);
        Network n = new Network(nodes);
        return n;
    }

//    class DummyEdge {
//        Integer id;
//        Integer fromId;
//        Integer toId;
//        Integer weight;
//
//        @Override
//        public String toString() {
//            return "DummyEdge {\n" +
//                    "\tid = " + id +
//                    ", \n\tfromId = " + fromId +
//                    ", \n\ttoId = " + toId +
//                    ", \n\tweight = " + weight +
//                    "\n}";
//        }
//    }
//
//    class DummyVertex {
//        Integer id;
//        NodeState flag;
//        Double probability;
//
//        @Override
//        public String toString() {
//            return "DummyVertex {\n" +
//                    "\tid = " + id +
//                    ", \n\tflag = " + flag +
//                    ", \n\tprobability = " + probability +
//                    "\n}";
//        }
//    }

}

package parser;

import BeliefNetwork.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphParser {

    private File graphFile;
    private Integer numberOfVertexes;
    private List<DummyEdge> edges;
    private List<DummyVertex> vertexes;
    private Double persistenceProd;

    public GraphParser(String filePath) {
        this.graphFile = new File(filePath);
        this.edges = new ArrayList<>();
        this.vertexes = new ArrayList<>();
        this.numberOfVertexes = 0;
        this.persistenceProd = 0.0;
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
//        System.out.println(numberOfVertexes);
//        System.out.println(persistenceProd);
//        System.out.println(vertexes);
//        System.out.println(edges);
    }

    private void detectResource(String line) {
        if (line.startsWith("#N")) {
            String []parts = line.split("[ ]+");
            numberOfVertexes = Integer.parseInt(parts[1]);
        }
        else if (line.startsWith("#V")) {
            String []parts = line.split("[ ]+");
            DummyVertex vertex = new DummyVertex();
            vertex.id = Integer.parseInt(parts[0].replace("#V", ""));
            vertex.flag = parts[1].equals("F") ? NodeState.FALSE : NodeState.TRUE;
            vertex.probability = Double.parseDouble(parts[2]);
            vertexes.add(vertex);
        }
        else if (line.startsWith("#E")) {
            String []parts = line.split("[ ]+");
            DummyEdge e = new DummyEdge();
            e.id = Integer.parseInt(parts[0].replace("#E", ""));
            e.fromId = Integer.parseInt(parts[1]);
            e.toId = Integer.parseInt(parts[2]);
            e.weight = Integer.parseInt(parts[3].replace("W", ""));
            edges.add(e);
        }
        else if (line.startsWith("#P")) {
            String []parts = line.split("[ ]+");
            persistenceProd = Double.parseDouble(parts[1]);
        }
    }

    public Network getNetwork() {
        HashMap<Integer, EvacuteesNode> evacuteesNodes = new HashMap<>();
        for (DummyVertex dv : vertexes) {
            EvacuteesNode n = new EvacuteesNode(dv.id, dv.probability);
            evacuteesNodes.put(dv.id, n);
        }

        List<BeliefNode> nodes = new ArrayList<>();
        nodes.addAll(evacuteesNodes.values());
        for (DummyEdge de : edges) {
            BlockNode bn = new BlockNode(de.id, evacuteesNodes.get(de.fromId), evacuteesNodes.get(de.toId), de.weight, 0);
            nodes.add(bn);
        }
        Network n = new Network(nodes);
        return n;
    }

    class DummyEdge {
        Integer id;
        Integer fromId;
        Integer toId;
        Integer weight;

        @Override
        public String toString() {
            return "DummyEdge {\n" +
                    "\tid = " + id +
                    ", \n\tfromId = " + fromId +
                    ", \n\ttoId = " + toId +
                    ", \n\tweight = " + weight +
                    "\n}";
        }
    }

    class DummyVertex {
        Integer id;
        NodeState flag;
        Double probability;

        @Override
        public String toString() {
            return "DummyVertex {\n" +
                    "\tid = " + id +
                    ", \n\tflag = " + flag +
                    ", \n\tprobability = " + probability +
                    "\n}";
        }
    }

}

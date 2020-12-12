import agents.NoncooperativeAgent;
import agents.ZeroSumAgent;
import agents.ZeroSumCooperativeAgent;
import datatypes.Edge;
import datatypes.Vertex;
import environment.EnvironmentState;
import heuristics.SemicooperativeHeuristic;
import heuristics.ZSHeuristics;
import heuristics.ZeroSumCooperativeHeuristic;
import heuristics.ZeroSumNonCooperativeHeuristic;
import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedMultigraph;
import simulator.Simulator;

import java.util.Arrays;
import java.util.HashMap;

public class testMain {
    public static void main(String[] args){
        Graph<Vertex, Edge> g=new WeightedMultigraph<>(Edge.class);
        Vertex v1=new Vertex(1,0);
        Vertex v2=new Vertex(2,4);
        Vertex v3=new Vertex(3,0);
        Vertex v4=new Vertex(4,0);
        Vertex v5=new Vertex(5,5);
        Vertex v6=new Vertex(6,4);
        Vertex v7=new Vertex(7,0);
        Vertex v8=new Vertex(8,7);
        Vertex v9=new Vertex(9,0);
        Vertex v10=new Vertex(10,0);
        Vertex v11=new Vertex(11,2);
        Vertex v12=new Vertex(12,0);
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addVertex(v8);
        g.addVertex(v9);
        g.addVertex(v10);
        g.addVertex(v11);
        g.addVertex(v12);
        Edge e1=new Edge(1);
        Edge e2=new Edge(2);
        Edge e3=new Edge(3);
        Edge e4=new Edge(4);
        Edge e5=new Edge(5);
        Edge e6=new Edge(6);
        Edge e7=new Edge(7);
        Edge e8=new Edge(8);
        Edge e9=new Edge(9);
        Edge e10=new Edge(10);
        Edge e11=new Edge(11);
        Edge e12=new Edge(12);
        Edge e13=new Edge(13);
        Edge e14=new Edge(14);
        g.addEdge(v1,v2,e1);
        g.setEdgeWeight(e1,8);

        g.addEdge(v1,v6,e2);
        g.setEdgeWeight(e2,20);

        g.addEdge(v2,v3,e3);
        g.setEdgeWeight(e3,5);

        g.addEdge(v2,v7,e4);
        g.setEdgeWeight(e4,8);

        g.addEdge(v3,v4,e5);
        g.setEdgeWeight(e5,6);

        g.addEdge(v6,v5,e6);
        g.setEdgeWeight(e6,8);

        g.addEdge(v4,v11,e7);
        g.setEdgeWeight(e7,9);


        g.addEdge(v6,v7,e9);
        g.setEdgeWeight(e9,8);

        g.addEdge(v7,v8,e10);
        g.setEdgeWeight(e10,16);

        g.addEdge(v7,v10,e11);
        g.setEdgeWeight(e11,11);

        g.addEdge(v9,v10,e12);
        g.setEdgeWeight(e12,9);

        g.addEdge(v9,v11,e13);
        g.setEdgeWeight(e13,1);

        g.addEdge(v9,v12,e14);
        g.setEdgeWeight(e14,7);

//        GreedyAgent agent=new GreedyAgent();
//        GraphMovementAction action=agent.processNextAction(new Perception(v1,g));
        EnvironmentState.getInstance().setGraph(g);
        EnvironmentState.getInstance().setWorldTimeout(24);
//        WorldLoader loader = new WorldLoader("/Users/igorvinokur/Development/Dev/Study/Assignment1AI/src/main/resources/graph-test.json");
//        EnvironmentState.getInstance().setGraph(loader.loadWorld());
//        EnvironmentState.getInstance().setWorldTimeout(loader.getWorldTimout().intValue());
//        for (Vertex v : EnvironmentState.getInstance().getGraph().vertexSet()) {
//            if (v.getId() == 1) {
//                v1 = v;
//            }
//            if (v.getId() == 6) {
//                v2 = v;
//            }
//        }
//        ZSHeuristics heuristics = new ZeroSumNonCooperativeHeuristic();

        HashMap<Integer, Integer> peopleAtVertex = new HashMap<>();
        for (Vertex v : EnvironmentState.getInstance().getGraph().vertexSet()) {
            if (v.getNumberOfPeople() > 0) {
                peopleAtVertex.put(v.getId(), v.getNumberOfPeople());
            }
        }
//        SemicooperativeHeuristic heuristics =new SemicooperativeHeuristic();
        ZSHeuristics heuristics=new ZeroSumNonCooperativeHeuristic();
        EnvironmentState.getInstance().setPeopleAtVertex(peopleAtVertex);
        ZeroSumAgent zeroSumAgent1 = new ZeroSumAgent(EnvironmentState.getInstance(), "Player 1", heuristics);
        ZeroSumAgent zeroSumAgent2 = new ZeroSumAgent(EnvironmentState.getInstance(), "Player 2", heuristics);
//        NoncooperativeAgent zeroSumAgent1 = new NoncooperativeAgent(EnvironmentState.getInstance(), "Player 1", heuristics);
//        NoncooperativeAgent zeroSumAgent2 = new NoncooperativeAgent(EnvironmentState.getInstance(), "Player 2", heuristics);
        zeroSumAgent1.setEnemy(zeroSumAgent2);
        zeroSumAgent2.setEnemy(zeroSumAgent1);
        EnvironmentState.getInstance().setAgentInit(zeroSumAgent1, v7);
        EnvironmentState.getInstance().setAgentInit(zeroSumAgent2, v1);
        EnvironmentState.getInstance().setAgents(Arrays.asList(zeroSumAgent1, zeroSumAgent2));
//        RescuePeopleGoal rpg = new RescuePeopleGoal();
//        GreedyHeuristicAgent a = new GreedyHeuristicAgent(g, v1);
//        AStarAgent a = new AStarAgent(g, v1);
//        RealTimeAStarAgent a = new RealTimeAStarAgent(g, v1);
//        a.setGoal(rpg);
//        a.setGoal(rpg);
//        a.processNextAction(null);
//        EnvironmentState.getInstance().getAgents().add(a);
        Simulator s = new Simulator();
        s.start();
//        System.out.println(a.getInternal().edgeSet());
    }
}

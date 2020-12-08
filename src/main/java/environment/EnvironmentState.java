package environment;

import agents.Agent;
import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnvironmentState {

    private static EnvironmentState instance;

    private int numberOfAgents = 0;
    private List<Agent> agents;
    private int worldTimeout;

    private Graph<Vertex, Edge> graph;
    private Graph<Vertex, Edge> simulationGraph;
    private Graph<Vertex, Edge> simulationTravelGraph;

    private HashMap<Agent, Vertex> agentsLocation;
    private HashMap<Agent, Integer> travelTime;
    private HashMap<Agent, Integer> agentScore; // people saved by agent

    private HashMap<Integer, Integer> peopleAtVertex;

    private Vertex init;

    private EnvironmentState() {
        agents = new ArrayList<>();
        agentsLocation = new HashMap<>();
        travelTime = new HashMap<>();
        agentScore = new HashMap<>();
        peopleAtVertex = new HashMap<>();
    }

    public static EnvironmentState getInstance() {
        if (instance == null) {
            instance = new EnvironmentState();
        }
        return instance;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        for (Agent a : agents) {
            travelTime.put(a, 0);
            agentScore.put(a, 0);
        }
        this.agents = agents;
    }

    public Graph<Vertex, Edge> getGraph() {
        return graph;
    }

    public void setGraph(Graph<Vertex, Edge> graph) {
        this.graph = graph;
    }

    public Vertex getInit() {
        return init;
    }

    public void setInit(Vertex init) {
        for (Agent a : agents) {
            agentsLocation.put(a, init);
        }
        this.init = init;
    }

    public int getWorldTimeout() {
        return worldTimeout;
    }

    public void setWorldTimeout(int worldTimeout) {
        this.worldTimeout = worldTimeout;
    }

    public Graph<Vertex, Edge> getSimulationGraph() {
        return simulationGraph;
    }

    public void setSimulationGraph(Graph<Vertex, Edge> simulationGraph) {
        this.simulationGraph = simulationGraph;
    }

    public Graph<Vertex, Edge> getSimulationTravelGraph() {
        return simulationTravelGraph;
    }

    public void setSimulationTravelGraph(Graph<Vertex, Edge> simulationTravelGraph) {
        this.simulationTravelGraph = simulationTravelGraph;
    }

    public HashMap<Agent, Vertex> getAgentsLocation() {
        return agentsLocation;
    }

    public void setAgentsLocation(HashMap<Agent, Vertex> agentsLocation) {
        this.agentsLocation = agentsLocation;
    }

    public HashMap<Agent, Integer> getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(HashMap<Agent, Integer> travelTime) {
        this.travelTime = travelTime;
    }

    public HashMap<Integer, Integer> getPeopleAtVertex() {
        return peopleAtVertex;
    }

    public void setPeopleAtVertex(HashMap<Integer, Integer> peopleAtVertex) {
        this.peopleAtVertex = peopleAtVertex;
    }

    public HashMap<Agent, Integer> getAgentScore() {
        return agentScore;
    }

    public void setAgentScore(HashMap<Agent, Integer> agentScore) {
        this.agentScore = agentScore;
    }

    public Graph<Vertex, Edge> getSimulationGraphCopy() {
        WeightedMultigraph<Vertex, Edge> newGrapg = new WeightedMultigraph<>(Edge.class);
        newGrapg.vertexSet().addAll(simulationGraph.vertexSet());
        newGrapg.edgeSet().addAll(simulationGraph.edgeSet());
        return newGrapg;
    }

    public Graph<Vertex, Edge> getSimulationTravelGraphCopy() {
        WeightedMultigraph<Vertex, Edge> newGrapg = new WeightedMultigraph<>(Edge.class);
        newGrapg.vertexSet().addAll(simulationTravelGraph.vertexSet());
        newGrapg.edgeSet().addAll(simulationTravelGraph.edgeSet());
        return newGrapg;
    }

    public static Graph<Vertex, Edge> cloneGraph(Graph<Vertex, Edge> graph) {
        WeightedMultigraph<Vertex, Edge> newGrapg = new WeightedMultigraph<>(Edge.class);
        for (Vertex v : graph.vertexSet()) {
            newGrapg.addVertex(v);
        }
        for (Edge e : graph.edgeSet()) {
            newGrapg.addEdge(e.getSource(), e.getTarget(), e);
        }
        return newGrapg;
    }
}

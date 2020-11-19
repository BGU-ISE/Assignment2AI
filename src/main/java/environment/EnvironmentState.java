package environment;

import agents.Agent;
import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;

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

    private Vertex init;

    private EnvironmentState() {
        agents = new ArrayList<>();
        agentsLocation = new HashMap<>();
        travelTime = new HashMap<>();
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
}

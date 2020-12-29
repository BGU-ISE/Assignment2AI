import BeliefNetwork.Network;
import BeliefNetwork.data.Query;
import parser.GraphParser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("Graph file not provided");
//            System.exit(0);
//        }
        //"C:\\Users\\itain\\Ideaprojects\\Assignment3AI\\src\\main\\resources\\input.txt");
        GraphParser parser = new GraphParser("/Users/igorvinokur/Development/Dev/Study/Assignment3AI/src/main/resources/input1.txt");
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Network network = parser.getNetwork();
        boolean exit = false;
        while(!exit) {
            System.out.println("============= Main menu ===============");
            System.out.println("1. Reset evidence list to empty.");
            System.out.println("2. Add piece of evidence to evidence list.");
            System.out.println("3. What is the probability that each of the vertices contains evacuees.");
            System.out.println("4. What is the probability that each of the edges is blocked.");
            System.out.println("5. What is the probability that a certain path (set of edges) is free from blockages.");
            System.out.println("6. Print network");
            System.out.println("7. Quit");
            Scanner command = new Scanner(System.in);
            switch (command.nextInt()) {
                case 1:
                    resetEvidence(network);
                    break;
                case 2:
                    addEvidence(network);
                    break;
                case 3:
                    vertexProbability(network);
                    break;
                case 4:
                    edgeProbability(network);
                    break;
                case 5:
                    pathProbability(network);
                    break;
                case 6:
                    System.out.println(network);
                    break;
                case 7:
                    exit = true;
                    break;
            }
        }
        System.out.println("Bye");
    }

    public static void resetEvidence(Network network) {
        Scanner command = new Scanner(System.in);
        System.out.println("============================");
        System.out.println("Reset all evidence");
        Query query = new Query(Query.Operation.ResetEvidence);
        network.ask(query);
        System.out.println();
    }

    public static void addEvidence(Network network) {
        Scanner command = new Scanner(System.in);
        System.out.println("============================");
        System.out.println("Add evidence to:");
        System.out.println("1. Vertex");
        System.out.println("2. Edge");
        Integer v = command.nextInt();
        System.out.println("Please enter " + (v == 1 ? "vertex" : "edge") + " id: ");
        Integer node = command.nextInt();
        System.out.println("Enter evidence probability: ");
        Double d = command.nextDouble();
        Query q = new Query(v == 1 ? Query.Operation.AddNodeEvidence : Query.Operation.AddEdgeEvidence, node, d);
        network.ask(q);
        System.out.println();
    }

    public static void vertexProbability(Network network) {
        Scanner command = new Scanner(System.in);
        System.out.println("============================");
        System.out.println("Please enter vertex id: ");
        Integer v = command.nextInt();
        Query query = new Query(Query.Operation.VertexProb, v);
        network.ask(query);
        System.out.println();
    }

    public static void edgeProbability(Network network) {
        Scanner command = new Scanner(System.in);
        System.out.println("============================");
        System.out.println("Please enter edge id: ");
        Integer e = command.nextInt();
        Query query = new Query(Query.Operation.EdgeProb, e);
        network.ask(query);
        System.out.println();
    }

    public Main() {
    }

    public static void pathProbability(Network network) {
        Scanner command = new Scanner(System.in);
        System.out.println("============================");
        System.out.println("Please enter set of edges (press Enter after each id and 0 for stop): ");
        Set<Integer> edges = new HashSet<>();
        Integer v = 0;
        while ((v = command.nextInt()) > 0) {
            edges.add(v);
        }
        System.out.println(edges);
        Query query = new Query(Query.Operation.PathProb, edges);
        network.ask(query);
        System.out.println();
    }
}

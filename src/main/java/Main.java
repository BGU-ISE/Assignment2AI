import BeliefNetwork.Network;
import BeliefNetwork.data.Query;
import parser.GraphParser;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        GraphParser parser = new GraphParser("/Users/igorvinokur/Development/Dev/Study/Assignment3AI/src/main/resources/input.txt");
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

    public static void pathProbability(Network network) {
        Scanner command = new Scanner(System.in);
        System.out.println("============================");
        System.out.println("Please enter from id: ");
        Integer from = command.nextInt();
        System.out.println("Please enter to id: ");
        Integer to = command.nextInt();
        Query query = new Query(Query.Operation.PathProb, from, to);
        network.ask(query);
        System.out.println();
    }
}

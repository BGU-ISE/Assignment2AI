import parser.GraphParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        GraphParser parser = new GraphParser("/Users/igorvinokur/Development/Dev/Study/Assignment3AI/src/main/resources/input.txt");
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

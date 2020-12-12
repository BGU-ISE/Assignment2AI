package heuristics;

import agents.state.ZeroSumState;

public class SemicooperativeHeuristic {
    public int[] evaluate(ZeroSumState state) {
        int[] x= {state.getiSaved(),state.getEnemySaved()};
        return x;
    }
    public int[] evaluate2(ZeroSumState state) {
        int[] x= {state.getEnemySaved(),state.getiSaved()};
        return x;
    }
}

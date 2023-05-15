package caes;

public class Rule30 {
    private int[] state;
    private int rule;

    public Rule30(int[] initialState, int rule) {
        this.state = initialState;
        this.rule = rule;
    }

    public int[] getState() {
        return state;
    }

    public void update() {
        int[] nextState = new int[state.length];
        for (int i = 0; i < state.length; i++) {
            int left = (i == 0) ? state[state.length - 1] : state[i - 1];
            int center = state[i];
            int right = (i == state.length - 1) ? state[0] : state[i + 1];

            int pattern = left * 4 + center * 2 + right;
            int nextCell = (rule >> pattern) & 1;

            nextState[i] = nextCell;
        }
        state = nextState;
    }
}

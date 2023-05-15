package caes;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class OneDimensionalAutomata extends JPanel {
    private int[] state;
    private int ruleNumber;

    public OneDimensionalAutomata(int ruleNumber, int[] initialState) {
        this.ruleNumber = ruleNumber;
        this.state = initialState;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        int cellSize = 10;
        int margin = 10;

        // Draw the state of the automaton
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 1) {
                g2d.setColor(Color.BLACK);
            } else {
                g2d.setColor(Color.WHITE);
            }
            g2d.fillRect(margin + i * cellSize, margin, cellSize, cellSize);
        }

        // Update the state of the automaton
        int[] nextState = new int[state.length];
        for (int i = 0; i < state.length; i++) {
            int left = (i == 0) ? state[state.length - 1] : state[i - 1];
            int center = state[i];
            int right = (i == state.length - 1) ? state[0] : state[i + 1];

            int pattern = left * 4 + center * 2 + right;
            int nextCell = (ruleNumber >> pattern) & 1;

            nextState[i] = nextCell;
        }
        state = nextState;

        // Schedule the next repaint
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 200);
    }

    public static void main(String[] args) {
        int ruleNumber = 30;
        int[] initialState = new int[80];
        initialState[40] = 1;

        OneDimensionalAutomata automaton = new OneDimensionalAutomata(ruleNumber, initialState);

        JFrame frame = new JFrame("One-Dimensional Cellular Automaton");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(automaton);
        frame.pack();
        frame.setVisible(true);
    }
}

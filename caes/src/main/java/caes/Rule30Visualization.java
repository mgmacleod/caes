package caes;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.Random;

public class Rule30Visualization extends JPanel {
    private Rule30 automaton;
    private int generation;
    private int[][] states;

    public Rule30Visualization(Rule30 automaton) {
        this.automaton = automaton;
        this.generation = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        int cellSize = 2;
        int margin = 20;
        int xStart = (getWidth() - (automaton.getState().length * cellSize + margin * 2)) / 2;
        int yStart = (getHeight() - (generation * cellSize + margin * 2)) / 2;

        // Set anti-aliasing to get smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the past states of the automaton
        for (int i = 0; i < Math.min(generation, states.length); i++) {
            int[] state = states[i];
            for (int j = 0; j < state.length; j++) {
                if (state[j] == 1) {
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect(xStart + j * cellSize, yStart + i * cellSize, cellSize, cellSize);
            }
        }

        // Update the state of the automaton
        automaton.update();
        int[] state = automaton.getState();
        if (generation < states.length) {
            states[generation] = Arrays.copyOf(state, state.length);
        } else {
            for (int i = 0; i < states.length - 1; i++) {
                states[i] = states[i + 1];
            }
            states[states.length - 1] = Arrays.copyOf(state, state.length);
        }
        generation++;

        // Schedule the next repaint
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        repaint();
    }



    @Override
    public Dimension getPreferredSize() {
        int height = 1080;
        int width = 1920;
        states = new int[height][automaton.getState().length];
        return new Dimension(width, height);
    }

    public static void main(String[] args) {
    	int numCells=960;
        int[] initialState = new int[numCells];
//        initialState[initialState.length / 2] = 1;
        int numLiveCells = 60;
        Random random = new Random();
        for (int i=0; i<numLiveCells; i++) {
        	int cell = random.nextInt(numCells);
        	initialState[cell] = 1;
        }

        int rule = 150; //random.nextInt(256);

        System.out.println(String.format("Running rule %d with %d cells, %d of which are alive on G1", rule, numCells, numLiveCells));
        
        Rule30 automaton = new Rule30(initialState, rule);
        Rule30Visualization visualization = new Rule30Visualization(automaton);

        JFrame frame = new JFrame("Rule 30 Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(visualization);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}

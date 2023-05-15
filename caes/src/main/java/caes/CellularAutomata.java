package caes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class CellularAutomata {
    public static void main(String[] args) {
    	int width = 1920;
    	int height = 1080;
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Elementary Cellular Automata");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(width, height);
            frame.add(new CellularAutomataPanel(width, height));
            frame.setVisible(true);
        });
    }
}

class CellularAutomataPanel extends JPanel {
	private static final long serialVersionUID = 4398744796139936363L;
	private final Automaton automaton;
    private final JButton startStopButton;
    private final JComboBox<Integer> ruleBox;
    private final JSpinner initialSpinner;

    public CellularAutomataPanel(int width, int height) {
        setLayout(new BorderLayout());

        automaton = new Automaton(width, height);
        add(automaton, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        startStopButton = new JButton("Start");
        startStopButton.addActionListener(e -> {
            automaton.toggleRunning();
            startStopButton.setText(automaton.isRunning() ? "Stop" : "Start");
        });

        controlPanel.add(startStopButton);

        ruleBox = new JComboBox<>();
        for (int i = 0; i < 256; i++) {
            ruleBox.addItem(i);
        }
        ruleBox.addActionListener(e -> automaton.setRule(ruleBox.getSelectedIndex()));
        controlPanel.add(new JLabel("Rule:"));
        controlPanel.add(ruleBox);

        initialSpinner = new JSpinner(new SpinnerNumberModel(1, 1, automaton.grid[0].length, 1));
        controlPanel.add(new JLabel("Initial live cells:"));
        controlPanel.add(initialSpinner);
        JButton initButton = new JButton("Initialize");
        initButton.addActionListener(e -> automaton.initialize((int) initialSpinner.getValue()));
        controlPanel.add(initButton);

        add(controlPanel, BorderLayout.SOUTH);
        
        automaton.initialize(1);
    }
}

class Automaton extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 946668756875714236L;
	private final Timer timer;
    private final int cellSize = 2;
    private boolean running;
    public int[][] grid;
    private int currentRow;
    private int rule;

    public Automaton(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        grid = new int[height / cellSize][width / cellSize];
        timer = new Timer(100, e -> advance());
    }

    public void setRule(int rule) {
        this.rule = rule;
        initialize(1);
    }

    public void initialize(int liveCells) {
        Random random = new Random();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0;
            }
        }
        if (liveCells == 1) {
            grid[0][grid[0].length / 2] = 1;
        } else {
            for (int i = 0; i < liveCells; i++) {
                grid[0][random.nextInt(grid[0].length)] = 1;
            }
        }
        currentRow = 1;
        repaint();
    }

    public void toggleRunning() {
        running = !running;
        if (running) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public boolean isRunning() {
        return running;
    }

    private void advance() {
        for (int i = 0; i < grid[currentRow].length; i++) {
        	int left = i == 0 ? grid[(currentRow - 1 + grid.length) % grid.length][grid[currentRow].length - 1] : grid[(currentRow - 1 + grid.length) % grid.length][i - 1];
        	int center = grid[(currentRow - 1 + grid.length) % grid.length][i];
        	int right = i == grid[currentRow].length - 1 ? grid[(currentRow - 1 + grid.length) % grid.length][0] : grid[(currentRow - 1 + grid.length) % grid.length][i + 1];


            int pattern = (left << 2) | (center << 1) | right;
            grid[currentRow][i] = (rule & (1 << pattern)) != 0 ? 1 : 0;
        }
        currentRow = (currentRow + 1) % grid.length;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        
        // Set anti-aliasing to get smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
            	g2d.setColor(grid[i][j] == 1 ? Color.BLACK : Color.WHITE);
            	g2d.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SGSOA extends JFrame {

    private JPanel statisticsPanel;
    private JPanel visualizationPanel;
    private JToggleButton startButton;

    private int unknownNumber;

    private Timer simulationTimer;
    private int numAttempts;
    private Random random;

    private boolean started = false;

    public SGSOA() {
        setTitle("NaturalismSimulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main container panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        // Statistics Panel
        statisticsPanel = new JPanel();
        statisticsPanel.setPreferredSize(new Dimension(200, 400));
        statisticsPanel.setBorder(BorderFactory.createTitledBorder("Algorithm Statistics"));
        mainPanel.add(statisticsPanel, BorderLayout.WEST);

        // Visualization Panel
        visualizationPanel = new JPanel();
        visualizationPanel.setBorder(BorderFactory.createTitledBorder("Trial and Error Visualization"));
        mainPanel.add(visualizationPanel, BorderLayout.CENTER);

        // Start Button
        startButton = new JToggleButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!started) {
                    startSimulation();
                } else {
                    simulationTimer.stop();
                }
            }
        });
        mainPanel.add(startButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Generate an unknown number (for simulation purposes)
        random = new Random();
        unknownNumber = random.nextInt(100) + 1; // Generate a random number between 1 and 100
    }

    private void startSimulation() {
        numAttempts = 0;

        started = true;

        AtomicInteger initialGuess = new AtomicInteger(random.nextInt(100) + 1); // Start with a random guess
        AtomicReference<Double> temperature = new AtomicReference<>(100.0); // Initial "temperature" for simulated annealing

        simulationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int guess = initialGuess.get();

                // Visualize the guess
                visualizeGuess(guess);

                // Update statistics
                updateStatistics(numAttempts);

                // Check if the guess is correct
                if (guess == unknownNumber) {
                    JOptionPane.showMessageDialog(SGSOA.this, "Found the unknown number: " + unknownNumber + " in " + numAttempts + " attempts!");
                    simulationTimer.stop(); // Stop the timer
                } else {
                    // Simulated annealing: generate a new guess based on a nearby number
                    int nextGuess = guess + random.nextInt(11) - 5; // Randomly adjust the guess by -5 to +5
                    double delta = Math.abs(nextGuess - guess);

                    // Accept the new guess probabilistically based on the simulated annealing mechanism
                    if (delta < 0 || random.nextDouble() < Math.exp(-delta / temperature.get())) {
                        initialGuess.set(nextGuess);
                    }

                    // Decrease the temperature (annealing process)
                    temperature.set(temperature.get() * 0.99);

                    // Increment number of attempts
                    numAttempts++;
                }
            }
        });

        simulationTimer.start(); // Start the timer for simulating the search process
    }

    private void visualizeGuess(int guess) {
        // Simulate visualization of guess
        visualizationPanel.removeAll();
        visualizationPanel.add(new JLabel("Guess: " + guess));
        visualizationPanel.revalidate();
        visualizationPanel.repaint();
    }

    private void updateStatistics(int numAttempts) {
        // Simulate updating statistics
        statisticsPanel.removeAll();
        statisticsPanel.add(new JLabel("Attempts: " + numAttempts));
        statisticsPanel.revalidate();
        statisticsPanel.repaint();
    }
}
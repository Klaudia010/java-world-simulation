package game;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Virtual World Simulation");
            GamePanel panel = new GamePanel();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.requestFocusInWindow(); // so key input works
        });

        System.out.println("Simulation started.");
    }
}

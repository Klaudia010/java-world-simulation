package game;

import game.organisms.Organism;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class BoardCanvas extends JPanel {
    private final int cellSize = 32;
    private World world;
    private final Map<String, Image> organismImages;

    public BoardCanvas(World world, Map<String, Image> organismImages) {
        this.world = world;
        this.organismImages = organismImages;

        setFocusable(true);
        requestFocusInWindow();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("CLICKED: " + e.getX() + ", " + e.getY());  // Debug line

                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                if (world.isValidPosition(x, y) && world.getOrganismAt(x, y) == null) {
                    String[] options = {
                            "Wolf", "Sheep", "Fox", "Turtle", "Antelope",
                            "Grass", "Guarana", "SowThistle", "Belladonna", "Hogweed"
                    };

                    String selected = (String) JOptionPane.showInputDialog(
                            BoardCanvas.this,
                            "Select organism to add:",
                            "Add Organism",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    if (selected != null) {
                        Organism org = world.createOrganismBySpecies(selected, x, y);
                        if (org != null) {
                            world.addOrganism(org);
                            repaint();
                            System.out.println("Added " + selected + " at (" + x + ", " + y + ")");
                        }
                    }
                }
            }
        });
    }

    public void setWorld(World newWorld) {
        this.world = newWorld;
        setPreferredSize(new Dimension(world.getWidth() * cellSize, world.getHeight() * cellSize));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Organism[][] grid = world.getBoard();

        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);

                Organism org = grid[y][x];
                if (org != null) {
                    Image img = organismImages.get(org.getSpecies());
                    if (img != null) {
                        Image scaled = img.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
                        g.drawImage(scaled, x * cellSize, y * cellSize, null);
                    } else {
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(org.getSymbol()), x * cellSize + 10, y * cellSize + 20);
                    }
                }
            }
        }
    }
}

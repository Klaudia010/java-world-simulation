package game;

import game.organisms.Organism;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel implements KeyListener {
    private World world;
    private final int cellSize = 32;
    private final JTextArea logArea;
    private final Map<String, Image> organismImages = new HashMap<>();
    private BoardCanvas boardCanvas;
    private boolean waitingForHumanMove = false;
    private int pendingKeyCode = -1;

    public GamePanel() {
        setLayout(new BorderLayout());

        // Initialize world
        world = new World(20, 17);
        GameInitializer.initializeGame(world);

        // Load images
        loadOrganismImages();

        // Board display (use external BoardCanvas with images)
        boardCanvas = new BoardCanvas(world, organismImages);
        boardCanvas.setPreferredSize(new Dimension(world.getWidth() * cellSize, world.getHeight() * cellSize));
        boardCanvas.setFocusable(true);
        boardCanvas.addKeyListener(this);

        // Buttons
        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton waitForMoveBtn = new JButton("Wait for Move");
        JButton abilityBtn = new JButton("Activate Ability");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        JButton newGameBtn = new JButton("New Game");
        JButton exitBtn = new JButton("Exit");

        waitForMoveBtn.addActionListener(e -> {
            waitingForHumanMove = true;
            pendingKeyCode = -1;
            log("Press an arrow key to move Human...");
            boardCanvas.requestFocusInWindow();
        });

        abilityBtn.addActionListener(e -> {
            world.handleKeyInput(KeyEvent.VK_SPACE);
            log("Human ability activated.");
        });

        saveBtn.addActionListener(e -> {
            world.saveToFile("save.txt");
            log("Game saved.");
        });

        loadBtn.addActionListener(e -> {
            World loaded = World.loadFromFile("save.txt");
            if (loaded != null) {
                setWorld(loaded);
                log("Game loaded.");
            } else {
                log("Failed to load game.");
            }
        });

        newGameBtn.addActionListener(e -> {
            world = new World(20, 17);
            GameInitializer.initializeGame(world);
            boardCanvas.setWorld(world);
            boardCanvas.repaint();
            log("New game started.");
        });

        exitBtn.addActionListener(e -> {
            System.exit(0);
        });

        buttonPanel.add(waitForMoveBtn);
        buttonPanel.add(abilityBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(loadBtn);
        buttonPanel.add(newGameBtn);
        buttonPanel.add(exitBtn);

        // Log area
        logArea = new JTextArea(6, 40);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        JPanel bottomInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomInfo.add(new JLabel("Klaudia Gołubowska"));
        bottomInfo.add(new JLabel(" | s201097"));

        controlPanel.add(buttonPanel, BorderLayout.NORTH);
        controlPanel.add(scrollPane, BorderLayout.CENTER);
        controlPanel.add(bottomInfo, BorderLayout.SOUTH);

        add(boardCanvas, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        boardCanvas.requestFocusInWindow();
    }

    public void setWorld(World newWorld) {
        this.world = newWorld;
        boardCanvas.setWorld(newWorld);
        repaint();
    }

    private void loadOrganismImages() {
        try {
            organismImages.put("Wolf", ImageIO.read(new File("src/wolf.png")));
            organismImages.put("Sheep", ImageIO.read(new File("src/sheep.png")));
            organismImages.put("Fox", ImageIO.read(new File("src/fox.png")));
            organismImages.put("Turtle", ImageIO.read(new File("src/turtle.png")));
            organismImages.put("Antelope", ImageIO.read(new File("src/antelope.png")));
            organismImages.put("Human", ImageIO.read(new File("src/human.png")));
            organismImages.put("Grass", ImageIO.read(new File("src/grass.png")));
            organismImages.put("Guarana", ImageIO.read(new File("src/guarana.png")));
            organismImages.put("SowThistle", ImageIO.read(new File("src/sowthistle.png")));
            organismImages.put("Belladonna", ImageIO.read(new File("src/belladonna.png")));
            organismImages.put("Hogweed", ImageIO.read(new File("src/hogweed.png")));
        } catch (IOException e) {
            System.err.println("Error loading organism images: " + e.getMessage());
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("Exiting game...");
            System.exit(0);
        }

        if (waitingForHumanMove) {
            pendingKeyCode = e.getKeyCode();
            world.handleKeyInput(pendingKeyCode);
            waitingForHumanMove = false;
            world.makeTurn();
            boardCanvas.repaint();
            log("Turn completed with Human move.");
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}

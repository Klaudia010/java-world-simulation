package game;

import game.organisms.Organism;
import game.organisms.animals.Human;
import game.organisms.plants.Plant;
import game.organisms.animals.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class World {
    private final int width;
    private final int height;
    private final Organism[][] board;
    private final List<Organism> organisms;
    private int turnNumber = 0;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new Organism[height][width];
        this.organisms = new ArrayList<>();
    }

    public void addOrganism(Organism org) {
        if (isValidPosition(org.getX(), org.getY()) && board[org.getY()][org.getX()] == null) {
            board[org.getY()][org.getX()] = org;
            organisms.add(org);
        }
    }

    public void moveOrganism(Organism org, int oldX, int oldY, int newX, int newY) {
        if (!isValidPosition(newX, newY)) return;

        Organism target = board[newY][newX];
        if (target != null && target != org) {
            org.collision(this, target);
            if (!org.isAlive() || (board[newY][newX] != null && board[newY][newX] != org)) {
                return;
            }
        }

        board[oldY][oldX] = null;
        board[newY][newX] = org;
        org.setX(newX);
        org.setY(newY);
    }

    public void removeOrganismAt(int x, int y) {
        if (!isValidPosition(x, y)) return;

        Organism toRemove = board[y][x];
        if (toRemove == null) return;

        board[y][x] = null;

        if (toRemove instanceof Plant && toRemove.isAlive()) {
            Organism consumer = getConsumingOrganism(x, y);
            if (consumer != null) {
                System.out.println(consumer.getSpecies() + " is consuming " + toRemove.getSpecies() + ".");
                toRemove.collision(this, consumer);
            }
        }

        organisms.remove(toRemove);
        toRemove.die();
    }

    public Organism getConsumingOrganism(int x, int y) {
        Organism plant = getOrganismAt(x, y);
        if (!(plant instanceof Plant)) return null;

        for (Organism organism : organisms) {
            if (!(organism instanceof Plant) && organism.getX() == x && organism.getY() == y) {
                return organism;
            }
        }
        return null;
    }

    public void makeTurn() {
        System.out.println("=== Begin Turn ===");
        turnNumber++;

        organisms.sort(Comparator
                .comparingInt(Organism::getInitiative).reversed()
                .thenComparingInt(Organism::getAge).reversed());

        List<Organism> activeOrganisms = new ArrayList<>(organisms);
        for (Organism org : activeOrganisms) {
            if (org.isAlive()) {
                org.action(this);
            }
        }

        // Cleanup dead organisms
        Iterator<Organism> iterator = organisms.iterator();
        while (iterator.hasNext()) {
            Organism org = iterator.next();
            if (!org.isAlive()) {
                board[org.getY()][org.getX()] = null;
                iterator.remove();
            }
        }
    }

    public void drawWorld(Graphics g, int cellSize) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Organism org = board[y][x];
                if (org != null) {
                    g.drawString(String.valueOf(org.getSymbol()), x * cellSize + 5, y * cellSize + 15);
                } else {
                    g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public Organism getOrganismAt(int x, int y) {
        return isValidPosition(x, y) ? board[y][x] : null;
    }

    public void handleKeyInput(int keyCode) {
        for (Organism org : organisms) {
            if (org instanceof Human) {
                ((Human) org).handleKey(keyCode);
                break;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Organism[][] getBoard() {
        return board;
    }

    public List<Organism> getOrganisms() {
        return organisms;
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println(width + " " + height + " " + turnNumber);
            for (Organism org : organisms) {
                writer.printf("%s %d %d %d %d\n",
                        org.getSpecies(),
                        org.getX(),
                        org.getY(),
                        org.getAge(),
                        org.getStrength()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static World loadFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int w = scanner.nextInt();
            int h = scanner.nextInt();
            int turn = scanner.nextInt();

            World newWorld = new World(w, h);
            newWorld.turnNumber = turn;

            while (scanner.hasNext()) {
                String species = scanner.next();
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int age = scanner.nextInt();
                int strength = scanner.nextInt();

                Organism org = newWorld.createOrganismBySpecies(species, x, y);
                if (org != null) {
                    org.setAge(age);
                    org.setStrength(strength);
                    newWorld.addOrganism(org);
                }
            }

            System.out.println("Game state loaded from " + filename + ".");
            return newWorld;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Organism createOrganismBySpecies(String species, int x, int y) {
        return switch (species) {
            case "Wolf" -> new game.organisms.animals.Wolf(x, y, this);
            case "Sheep" -> new game.organisms.animals.Sheep(x, y, this);
            case "Fox" -> new game.organisms.animals.Fox(x, y, this);
            case "Turtle" -> new game.organisms.animals.Turtle(x, y, this);
            case "Antelope" -> new game.organisms.animals.Antelope(x, y, this);
            case "Human" -> new game.organisms.animals.Human(x, y, this);
            case "Grass" -> new game.organisms.plants.Grass(x, y, this);
            case "Guarana" -> new game.organisms.plants.Guarana(x, y, this);
            case "SowThistle" -> new game.organisms.plants.SowThistle(x, y, this);
            case "Belladonna" -> new game.organisms.plants.Belladonna(x, y, this);
            case "Hogweed" -> new game.organisms.plants.Hogweed(x, y, this);
            default -> null;
        };
    }

    public Human getHuman() {
        for (Organism org : organisms) {
            if (org instanceof Human) {
                return (Human) org;
            }
        }
        return null;
    }

    public void clear() {
        for (int y = 0; y < height; y++) {
            Arrays.fill(board[y], null);
        }
        organisms.clear();
    }
}
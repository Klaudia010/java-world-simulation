package game.organisms;

import game.World;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Organism {
    protected int strength;
    protected int initiative;
    protected int x;
    protected int y;
    protected World world;
    protected int age = 0;
    protected boolean alive = true;

    public Organism(int strength, int initiative, int x, int y, World world) {
        this.strength = strength;
        this.initiative = initiative;
        this.x = x;
        this.y = y;
        this.world = world;
    }

    // Abstract methods to be implemented by subclasses
    public abstract void action(World world);
    public abstract void collision(World world, Organism other);
    public abstract char getSymbol();
    public abstract String getSpecies();

    public void incrementAge() { age++; }

    public int getAge() { return age; }

    public void setAge(int newAge) {
        this.age = newAge;
    }

    // Life status methods
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) { this.alive = alive; }

    public void die() {
        this.alive = false;
    }

    // Attribute accessors and mutators
    public int getStrength() {
        return strength;
    }

    public void setStrength(int newStrength) {
        this.strength = newStrength;
    }

    public int getInitiative() {
        return initiative;
    }

    // Positioning methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    // Collision helpers
    public boolean hasDeflectedAttack(Organism attacker) {
        return false;
    }

    public boolean handleSpecialCollision(World world, Organism other) {
        return false;
    }

    public boolean defaultCollision(World world, Organism other) {
        if (this.strength >= other.getStrength()) {
            System.out.println(this.getSpecies() + " (" + this.strength + ") defeats " +
                    other.getSpecies() + " (" + other.getStrength() + ") at (" +
                    other.getX() + ", " + other.getY() + ").");
            world.removeOrganismAt(other.getX(), other.getY());
            world.moveOrganism(this, this.getX(), this.getY(), other.getX(), other.getY());
            return true; // This organism wins
        } else {
            System.out.println(other.getSpecies() + " (" + other.getStrength() + ") defeats " +
                    this.getSpecies() + " (" + this.strength + ") at (" +
                    this.getX() + ", " + this.getY() + ").");
            world.removeOrganismAt(this.getX(), this.getY());
            return false; // Other organism wins
        }
    }

    // Find an empty adjacent field
    public Point findEmptyField(World world) {
        List<Point> offsets = new ArrayList<>();
        offsets.add(new Point(-1, 0));
        offsets.add(new Point(1, 0));
        offsets.add(new Point(0, -1));
        offsets.add(new Point(0, 1));
        Collections.shuffle(offsets);

        for (Point offset : offsets) {
            int newX = this.getX() + offset.x;
            int newY = this.getY() + offset.y;

            if (world.isValidPosition(newX, newY) && world.getOrganismAt(newX, newY) == null) {
                return new Point(newX, newY);
            }
        }
        return new Point(-1, -1); // No valid field found
    }

}

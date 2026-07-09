package game.organisms.animals;

import game.World;
import game.organisms.Organism;
import game.organisms.plants.Plant;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Animal extends Organism {

    public Animal(int strength, int initiative, int x, int y, World world) {
        super(strength, initiative, x, y, world);
    }

    @Override
    public void action(World world) {
        incrementAge();

        List<int[]> directions = getShuffledOffsets();

        for (int[] dir : directions) {
            int newX = getX() + dir[0] * getRange();
            int newY = getY() + dir[1] * getRange();

            if (!world.isValidPosition(newX, newY)) continue;

            Organism target = world.getOrganismAt(newX, newY);

            if (target == null || !target.isAlive()) {
                world.moveOrganism(this, getX(), getY(), newX, newY);
                return;
            } else {
                collision(world, target);
                return;
            }
        }
    }

    @Override
    public void collision(World world, Organism other) {

        if (this.handleSpecialCollision(world, other) || other.handleSpecialCollision(world, this)) {
            return;
        }

        if (this.getSpecies().equals(other.getSpecies())) {
            this.handleBreeding(world, other);
            return;
        }

        this.defaultCollision(world, other);

        if (other instanceof Plant) {
            System.out.println(getSpecies() + " is consuming " + other.getSpecies() + ".");
            other.collision(world, this);
        }
    }

    protected void handleBreeding(World world, Organism other) {
        if (this.getSpecies().equals("Human")) return;

        System.out.println(getSpecies() + " encounters another " + getSpecies() + " for breeding.");

        Point birthSpot = this.findEmptyField(world);
        if (birthSpot.x == -1) {
            birthSpot = other.findEmptyField(world);
        }

        if (birthSpot.x != -1) {
            Animal child = createNewAnimal(birthSpot.x, birthSpot.y, world);
            world.addOrganism(child);
            System.out.println(getSpecies() + " successfully bred at (" + birthSpot.x + ", " + birthSpot.y + ").");
        } else {
            System.out.println("Breeding failed: no space available.");
        }
    }

    protected List<int[]> getShuffledOffsets() {
        List<int[]> offsets = new ArrayList<>();
        offsets.add(new int[]{0, -1});
        offsets.add(new int[]{0, 1});
        offsets.add(new int[]{-1, 0});
        offsets.add(new int[]{1, 0});
        Collections.shuffle(offsets);
        return offsets;
    }

    public int getRange() {
        return 1;
    }

    protected abstract Animal createNewAnimal(int x, int y, World world);
}

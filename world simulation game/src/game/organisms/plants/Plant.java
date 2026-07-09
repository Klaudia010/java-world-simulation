package game.organisms.plants;

import game.World;
import game.organisms.Organism;

import java.awt.*;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

public abstract class Plant extends Organism {
    private static final double SPREAD_PROBABILITY = 0.05;
    protected char symbol;
    private static final Random random = new Random();

    public Plant(int strength, int initiative, int x, int y, World world) {
        super(strength, initiative, x, y, world);
    }

    @Override
    public void action(World world) {
        incrementAge();
        spread(world);
    }

    protected void spread(World world) {
        if (random.nextDouble() < SPREAD_PROBABILITY) {
            Point target = findEmptyField(world);
            if (target.x != -1) {
                Plant newPlant = createNewPlant(target.x, target.y, world);
                world.addOrganism(newPlant);
            }
        }
    }

    @Override
    public void collision(World world, Organism other) {
        this.setAlive(false);
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public String getSpecies() {
        return "Plant";
    }

    protected abstract Plant createNewPlant(int x, int y, World world);
}

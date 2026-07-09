package game.organisms.animals;

import game.World;
import game.organisms.Organism;

import java.awt.*;
import java.util.List;

public class Human extends Animal {
    private static final int DEFAULT_STRENGTH = 5;
    private static final int DEFAULT_INITIATIVE = 4;
    private static final int ABILITY_DURATION = 5;
    private static final int ABILITY_COOLDOWN = 5;

    private int directionX = 0;
    private int directionY = 0;

    private boolean abilityActive = false;
    private int abilityDuration = 0;
    private int cooldown = 0;

    public Human(int x, int y, World world) {
        super(DEFAULT_STRENGTH, DEFAULT_INITIATIVE, x, y, world);
    }

    @Override
    public void action(World world) {
        incrementAge();

        if (abilityActive) {
            abilityDuration--;
            if (abilityDuration == 0) {
                abilityActive = false;
                cooldown = ABILITY_COOLDOWN;
            }
        } else if (cooldown > 0) {
            cooldown--;
        }

        if (directionX == 0 && directionY == 0) return;

        int newX = x + directionX;
        int newY = y + directionY;

        directionX = 0;
        directionY = 0;

        if (!world.isValidPosition(newX, newY)) return;

        Organism target = world.getOrganismAt(newX, newY);

        if (target == null || !target.isAlive()) {
            world.moveOrganism(this, x, y, newX, newY);

            if (abilityActive) {
                destroyAdjacent(world);
            }
        } else {
            collision(world, target);
        }
    }

    @Override
    public void collision(World world, Organism other) {
        super.collision(world, other);
    }
    public void handleKey(int keyCode) {
        switch (keyCode) {
            case 37: directionX = -1; directionY = 0; break;
            case 38: directionX = 0; directionY = -1; break;
            case 39: directionX = 1; directionY = 0; break;
            case 40: directionX = 0; directionY = 1; break;
            case 32: activateAbility(); break;
        }
    }

    public void activateAbility() {
        if (!abilityActive && cooldown == 0) {
            abilityActive = true;
            abilityDuration = ABILITY_DURATION;
            System.out.println("Human's special ability activated!");
        } else if (cooldown > 0) {
            System.out.println("Ability not ready. Cooldown remaining: " + cooldown);
        }
    }

    private void destroyAdjacent(World world) {
        List<int[]> offsets = getShuffledOffsets();
        for (int[] offset : offsets) {
            int adjX = x + offset[0];
            int adjY = y + offset[1];

            if (world.isValidPosition(adjX, adjY)) {
                Organism organism = world.getOrganismAt(adjX, adjY);
                if (organism != null && organism.isAlive()) {
                    System.out.println("Purification destroyed " + organism.getSpecies() + " at (" + adjX + ", " + adjY + ")");
                    organism.setAlive(false);
                    world.removeOrganismAt(adjX, adjY);
                }
            }
        }
    }

    @Override
    public char getSymbol() {
        return 'H';
    }

    @Override
    public String getSpecies() {
        return "Human";
    }

    @Override
    protected Animal createNewAnimal(int x, int y, World world) {
        return new Human(x, y, world); // Humans don't breed in this simulation
    }
}

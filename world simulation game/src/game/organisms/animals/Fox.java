package game.organisms.animals;

import game.World;
import game.organisms.Organism;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Fox extends Animal {

    public Fox(int x, int y, World world) {
        super(3, 7, x, y, world);
    }

    @Override
    public void action(World world) {
        incrementAge();

        List<int[]> directions = getShuffledOffsets();

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (!world.isValidPosition(newX, newY)) continue;

            Organism target = world.getOrganismAt(newX, newY);

            // Skip stronger organisms
            if (target != null && target.getStrength() > this.getStrength()) {
                System.out.println("Fox avoided stronger " + target.getSpecies() + " at (" + newX + ", " + newY + ")");
                continue;
            }

            if (target == null || !target.isAlive()) {
                world.moveOrganism(this, x, y, newX, newY);
            } else {
                collision(world, target);
            }
            return;
        }
    }

    @Override
    public char getSymbol() {
        return 'F';
    }

    @Override
    public String getSpecies() {
        return "Fox";
    }

    @Override
    protected Animal createNewAnimal(int x, int y, World world) {
        return new Fox(x, y, world);
    }
}

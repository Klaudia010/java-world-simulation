package game.organisms.plants;

import game.World;
import game.organisms.Organism;

import java.awt.*;

public class Hogweed extends Plant {

    public Hogweed(int x, int y, World world) {
        super(10, 0, x, y, world); // strength = 10, initiative = 0
    }

    @Override
    public void action(World world) {
        incrementAge();

        // Kill surrounding animals
        int[][] directions = {
                {0, -1}, {0, 1}, {-1, 0}, {1, 0}
        };

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (!world.isValidPosition(newX, newY)) continue;

            Organism org = world.getOrganismAt(newX, newY);
            if (org != null && org.isAlive()
                    && org instanceof game.organisms.animals.Animal
                    ) {
                org.setAlive(false);
                world.removeOrganismAt(newX, newY);
                System.out.println("Hogweed killed " + org.getSpecies() + " at (" + newX + ", " + newY + ")");
            }
        }
        spread(world);
    }

    @Override
    public void collision(World world, Organism attacker) {
        attacker.setAlive(false);
        this.setAlive(false);
    }

    @Override
    public char getSymbol() {
        return '!';
    }

    @Override
    public String getSpecies() {
        return "Hogweed";
    }

    @Override
    protected Plant createNewPlant(int x, int y, World world) {
        return new Hogweed(x, y, world);
    }
}

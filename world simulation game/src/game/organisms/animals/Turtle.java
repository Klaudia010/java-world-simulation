package game.organisms.animals;

import game.World;
import game.organisms.Organism;

public class Turtle extends Animal {

    public Turtle(int x, int y, World world) {
        super(2, 1, x, y, world);
    }

    @Override
    public void action(World world) {
        incrementAge();

        // 75% chance to skip turn
        if (Math.random() < 0.75) {
            System.out.println("Turtle stays in place this turn at (" + x + ", " + y + ")");
            return;
        }

        super.action(world);
    }

    @Override
    public boolean handleSpecialCollision(World world, Organism attacker) {
        if (attacker.getStrength() < 5) {
            System.out.println("Turtle reflected attack from " + attacker.getSpecies() + " with strength " + attacker.getStrength());
            return true;
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return 'T';
    }

    @Override
    public String getSpecies() {
        return "Turtle";
    }

    @Override
    protected Animal createNewAnimal(int x, int y, World world) {
        return new Turtle(x, y, world);
    }
}

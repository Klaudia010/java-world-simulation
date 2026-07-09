package game.organisms.animals;

import game.World;
import game.organisms.Organism;

import java.awt.*;
import java.util.List;

public class Antelope extends Animal {

    public Antelope(int x, int y, World world) {
        super(4, 4, x, y, world); // strength = 4, initiative = 4
    }

    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public boolean handleSpecialCollision(World world, Organism attacker) {
        // 50% chance to escape
        if (Math.random() < 0.5) {
            Point escapeSpot = findEmptyField(world);
            if (escapeSpot.x != -1) {
                world.moveOrganism(this, getX(), getY(), escapeSpot.x, escapeSpot.y);
                System.out.println("Antelope escaped from " + attacker.getSpecies() +
                        " to (" + escapeSpot.x + ", " + escapeSpot.y + ")");
                return true; // Successfully escaped
            } else {
                System.out.println("Antelope tried to escape from " + attacker.getSpecies() +
                        " but found no free spot.");
            }
        } else {
            System.out.println("Antelope failed to escape from " + attacker.getSpecies());
        }
        return false; // Failed to escape
    }


    @Override
    public char getSymbol() {
        return 'A';
    }

    @Override
    public String getSpecies() {
        return "Antelope";
    }

    @Override
    protected Animal createNewAnimal(int x, int y, World world) {
        return new Antelope(x, y, world);
    }
}

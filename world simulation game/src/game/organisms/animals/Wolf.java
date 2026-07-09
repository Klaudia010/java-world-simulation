package game.organisms.animals;

import game.World;

public class Wolf extends Animal {

    public Wolf(int x, int y, World world) {
        super(9, 5, x, y, world);
    }

    @Override
    public char getSymbol() {
        return 'W';
    }

    @Override
    public String getSpecies() {
        return "Wolf";
    }

    @Override
    protected Animal createNewAnimal(int x, int y, World world) {
        return new Wolf(x, y, world);
    }
}

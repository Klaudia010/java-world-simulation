package game.organisms.animals;

import game.World;

public class Sheep extends Animal {

    public Sheep(int x, int y, World world) {
        super(4, 4, x, y, world);
    }

    @Override
    public char getSymbol() {
        return 'S';
    }

    @Override
    public String getSpecies() {
        return "Sheep";
    }

    @Override
    protected Animal createNewAnimal(int x, int y, World world) {
        return new Sheep(x, y, world);
    }
}

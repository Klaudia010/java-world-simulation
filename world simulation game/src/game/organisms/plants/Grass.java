package game.organisms.plants;

import game.World;

public class Grass extends Plant {

    public Grass(int x, int y, World world) {
        super(0, 0, x, y, world);
    }

    @Override
    public char getSymbol() {
        return ',';
    }

    @Override
    public String getSpecies() {
        return "Grass";
    }

    @Override
    protected Plant createNewPlant(int x, int y, World world) {
        return new Grass(x, y, world);
    }
}

package game.organisms.plants;

import game.World;

import java.awt.*;

public class SowThistle extends Plant {

    public SowThistle(int x, int y, World world) {
        super(0, 0, x, y, world);
    }

    @Override
    public void action(World world) {
        incrementAge();

        for (int i = 0; i < 3; i++) {
            spread(world);
        }
    }

    @Override
    public char getSymbol() {
        return '*';
    }

    @Override
    public String getSpecies() {
        return "SowThistle";
    }

    @Override
    protected Plant createNewPlant(int x, int y, World world) {
        return new SowThistle(x, y, world);
    }
}

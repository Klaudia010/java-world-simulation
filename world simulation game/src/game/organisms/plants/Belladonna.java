package game.organisms.plants;

import game.World;
import game.organisms.Organism;

public class Belladonna extends Plant {

    public Belladonna(int x, int y, World world) {
        super(99, 0, x, y, world);
    }

    @Override
    public void collision(World world, Organism attacker) {
        attacker.setAlive(false);
        this.setAlive(false);
    }

    @Override
    public char getSymbol() {
        return '#';
    }

    @Override
    public String getSpecies() {
        return "Belladonna";
    }

    @Override
    protected Plant createNewPlant(int x, int y, World world) {
        return new Belladonna(x, y, world);
    }
}

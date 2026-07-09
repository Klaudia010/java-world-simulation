package game.organisms.plants;

import game.World;
import game.organisms.Organism;

public class Guarana extends Plant {

    public Guarana(int x, int y, World world) {
        super(0, 0, x, y, world);
    }

    @Override
    public void collision(World world, Organism attacker) {
        attacker.setStrength(attacker.getStrength() + 3);
        this.setAlive(false);
        System.out.println(attacker.getSpecies() + " ate Guarana and gained +3 strength! New strength: " + attacker.getStrength());
    }

    @Override
    public char getSymbol() {
        return '$';
    }

    @Override
    public String getSpecies() {
        return "Guarana";
    }

    @Override
    protected Plant createNewPlant(int x, int y, World world) {
        return new Guarana(x, y, world);
    }
}

package game;

import game.organisms.animals.*;
import game.organisms.plants.*;

public class GameInitializer {
    public static void initializeGame(World world) {
        world.addOrganism(new Human(5, 5, world));
        world.addOrganism(new Wolf(2, 2, world));
        world.addOrganism(new Sheep(4, 4, world));
        world.addOrganism(new Fox(8, 8, world));
        world.addOrganism(new Turtle(1, 15, world));
        world.addOrganism(new Antelope(17, 3, world));

        world.addOrganism(new Grass(6, 6, world));
        world.addOrganism(new Guarana(3, 13, world));
        world.addOrganism(new Belladonna(11, 2, world));
        world.addOrganism(new SowThistle(9, 9, world));
        world.addOrganism(new Hogweed(7, 14, world));
    }
}

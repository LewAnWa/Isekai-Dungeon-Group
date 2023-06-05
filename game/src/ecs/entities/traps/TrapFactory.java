package ecs.entities.traps;

import ecs.entities.Entity;
import level.elements.ILevel;
import tools.Point;

public class TrapFactory {

    public static Entity generateTraps(int playerLevel, Point playerPos, ILevel currentLevel) {
        Trap trap = null;
        trap = new Warhead(playerPos, currentLevel);
/*
        do {
            switch ((int) (Math.random() * 3)) {
                case 0 -> trap = new Warhead(playerPos, currentLevel);

                case 1 -> {
                    if (playerLevel >= 2) {
                        trap = new Teleporter(playerPos, currentLevel);
                    }
                }
                case 2 -> {
                    if (playerLevel >= 5) {
                        trap = new Ritual(playerPos, currentLevel);
                    }
                }
                default -> trap = new Warhead(playerPos, currentLevel);
            }


        } while (trap == null);*/

        return trap;
    }
}

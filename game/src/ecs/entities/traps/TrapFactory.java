package ecs.entities.traps;

import ecs.entities.Entity;
import level.elements.ILevel;
import tools.Point;

public class TrapFactory {

    public static Entity generateTraps(int playerLevel, Point playerPos, ILevel currentLevel){
        Trap trap = null;
        trap = new Warhead(playerPos, currentLevel);

        return trap;
    }
}

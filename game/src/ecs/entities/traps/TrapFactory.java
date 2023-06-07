package ecs.entities.traps;

import ecs.entities.Entity;
import level.elements.ILevel;
import tools.Point;

/** A class which chooses the trap to be generated */
public class TrapFactory {

    /**
     * Generates a random trap relative to the player's level.
     *
     * @param playerLevel current level of the player.
     * @param playerPos the current position of the player in the level.
     * @param currentLevel the generated map.
     * @return A random trap.
     */
    public static Entity generateTraps(int playerLevel, Point playerPos, ILevel currentLevel) {
        Trap trap = null;

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
        } while (trap == null);

        return trap;
    }
}

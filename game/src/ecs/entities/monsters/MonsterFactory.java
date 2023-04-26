package ecs.entities.monsters;

import ecs.entities.Entity;
import level.elements.ILevel;
import tools.Point;

public class MonsterFactory {

    /**
     * Generates a random monster with random stats that fluctuate relative to the player's level.
     * @param playerLevel current level of the player.
     * @param playerPos the current position of the player in the level.
     * @param currentLevel the generated map.
     * @return a random monster.
     */
    public static Entity generateMonster(int playerLevel, Point playerPos, ILevel currentLevel) {
        int random = (int)(Math.random() * playerLevel); // random is for the type of monster
        int flux = (int)(Math.random() * (playerLevel * 2)); // flux is for the fluctuation of their stats

        return switch (random) {
            case 0 -> new Skeleton(0.12f, flux, playerPos, currentLevel);
            case 1 -> new Bat(0.09f, flux, playerPos, currentLevel);
            case 2 -> new Necromancer(0.1f, flux, playerPos, currentLevel);
            default -> new Skeleton(0.12f, flux, playerPos, currentLevel);
        };
    }
}

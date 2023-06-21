package ecs.entities.monsters;

import ecs.entities.Entity;
import level.elements.ILevel;
import tools.Point;

/**
 * Creates a random type of monster depending on herolevel, playerposition and the current level
 * layout.
 */
public class MonsterFactory {

    /**
     * Generates a random monster with random stats that fluctuate relative to the player's level.
     *
     * @param playerLevel current level of the player.
     * @param playerPos the current position of the player in the level.
     * @param currentLevel the generated map.
     * @return a random monster.
     */
    public static Entity generateMonster(int playerLevel, Point playerPos, ILevel currentLevel) {
        int flux =
                (int)
                        (Math.random()
                                * (playerLevel * 2)); // flux is for the fluctuation of their stats
        Monster monster = null;

        do {
            switch ((int) (Math.random() * 3)) {
                case 0 -> {
                    monster = new Skeleton(0.12f, flux, playerPos, currentLevel);
                }
                case 1 -> {
                    if (playerLevel >= 2) {
                        monster = new Bat(0.09f, flux, playerPos, currentLevel);
                    }
                }
                case 2 -> {
                    if (playerLevel >= 5) {
                        monster = new Necromancer(0.1f, flux, playerPos, currentLevel);
                    }
                }
                default -> {
                    monster = new Skeleton(0.10f, flux, playerPos, currentLevel);
                }
            }
            ;
        } while (monster == null);

        return monster;
    }

    /**
     * Generates a new Mimic Monster
     *
     * @param playerLevel current level of the player.
     * @param playerPos the current position of the player in the level.
     * @param currentLevel the generated map.
     * @return a mimic
     */
    public static Entity spawnMimic(int playerLevel, Point playerPos, ILevel currentLevel) {
        int flux = (int) (Math.random() * (playerLevel * 2));
        Monster mimic = new Mimic(0.12f, flux, playerPos, currentLevel);
        return mimic;
    }

    /**
     * Generates the Boss Monster of this Game
     *
     * @param playerLevel current level of the player.
     * @param playerPos the current position of the player in the level.
     * @param currentLevel the generated map.
     * @return a Boss
     */
    public static Entity spawnBoss(int playerLevel, Point playerPos, ILevel currentLevel) {
        int flux = (int) (Math.random() * (playerLevel * 2));
        return new Boss(0.12f, flux);
    }

    /**
     * Generates one Necromancer
     *
     * @param playerLevel current level of the player.
     * @param playerPos the current position of the player in the level.
     * @param currentLevel the generated map.
     * @return one Necromancer
     */
    public static Entity spawnNecromancer(int playerLevel, Point playerPos, ILevel currentLevel) {
        int flux = (int) (Math.random() * (playerLevel * 2));
        return new Necromancer(0.1f, flux, playerPos, currentLevel);
    }
}

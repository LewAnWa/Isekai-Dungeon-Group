package ecs.entities.monsters;

import ecs.entities.Entity;
import level.elements.ILevel;

public class MonsterFactory {

    public static Entity generateMonster(int playerLevel, ILevel currentLevel) {
        int random = (int)(Math.random() * playerLevel);

        return switch (random) {
            case 0 -> new Skeleton(0.3f, currentLevel);
            case 1 -> new Bat(0.1f, currentLevel);
            case 2 -> new Necromancer(0.3f, currentLevel);
            default -> new Skeleton(0.2f, currentLevel);
        };
    }
}


// Test

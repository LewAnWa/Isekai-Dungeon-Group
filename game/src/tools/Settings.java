package tools;

import level.tools.LevelSize;

/**
 * The Settings class contains settings that the player can set in the settings menu.
 * These settings can be accessed and changed.
 */
public class Settings {

    /**
     * Allow the DrawingSystem and LevelAPI to use the LightSourceComponent of an Entity.
     * This will result in extra computations.
     */
    public static boolean allowDynamicLighting = false;

    /**
     * Dictates how many monster entities are allowed to be generated on a new level.
     */
    public static int setMaxMonsterAmount = 20;

    public static final int MINIMUM_MONSTER_AMOUNT = 5;
    public static final int MAXIMUM_MONSTER_AMOUNT = 30;

    /**
     * Dictates the size a dungeon floor is allowed to be generated as.
     */
    public static LevelSize levelSize = LevelSize.SMALL;
}

package ecs.entities.traps;

import ecs.components.PositionComponent;
import ecs.entities.Entity;
import level.elements.ILevel;
import tools.Point;

/**
 * The Trap is an entity which makes the progress in the dungeon harder.
 * There are three different Traps in the game.
 */
public abstract class Trap extends Entity {

    /**
     * Default constructor for the trap
     *
     * @param playerPos The position of the player in the level.
     * @param currentLevel The current map.
     */
    public Trap(Point playerPos, ILevel currentLevel) {
        super(true);
        setUpPositionComponent(playerPos, currentLevel);
    }

    /**
     * Sets ups the position component for generated trap.
     * Won't be spawned to close to the hero.
     *
     * @param playerPos The position of the player in the level.
     * @param currentLevel The current map.
     */
    protected void setUpPositionComponent(Point playerPos, ILevel currentLevel) {
        Point randomPoint;

        do {
            randomPoint = currentLevel.getRandomFloorTile().getCoordinateAsPoint();
        } while (Point.calculateDistance(playerPos, randomPoint) < 3);

        new PositionComponent(this, randomPoint);
    }

    /**
     *  Implements the behaviour of the AnimationComponent of a trap.
     *  Will be specified when implemented in subclasses.
     */
    protected abstract void setupAnimationComponent();

    /**
     *  Implements the behaviour of the HitboxComponent of a trap.
     *  Will be specified when implemented in subclasses.
     */
    protected abstract void setHitboxComponent();


}

package ecs.entities;

import ecs.components.LightSourceComponent;
import ecs.components.PositionComponent;
import starter.Game;
import tools.Point;

/**
 * An entity acting as a light source. It does not have any animations or a hitbox. It only has a
 * position and a light source component.
 */
public class LightSource extends Entity {

    /**
     * Sets up a light source at a random point in the level.
     *
     * @param lightRadius The range of the light.
     */
    public LightSource(float lightRadius) {
        setupPositionComponent(Game.currentLevel.getRandomFloorTile().getCoordinateAsPoint());
        setupLightSourceComponent(lightRadius);
    }

    /**
     * Sets up a light source at a given point in the level.
     *
     * @param lightRadius the range of the light.
     * @param point the position in the level.
     */
    public LightSource(float lightRadius, Point point) {
        setupPositionComponent(point);
        setupLightSourceComponent(lightRadius);
    }

    private void setupLightSourceComponent(float lightRadius) {
        new LightSourceComponent(this, lightRadius);
    }

    private void setupPositionComponent(Point point) {
        new PositionComponent(this, point);
    }
}

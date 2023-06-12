package ecs.entities;

import ecs.components.LightSourceComponent;
import ecs.components.PositionComponent;
import starter.Game;

/**
 * An entity acting as a light source. It does not have any animations or a hitbox.
 * It only has a position and a light source component.
 */
public class LightSource extends Entity {

    /**
     * Sets up a light source at a random point in the level.
     * @param lightRadius The range of the light.
     */
    public LightSource(float lightRadius) {
        setupPositionComponent();
        setupLightSourceComponent(lightRadius);
    }

    private void setupLightSourceComponent(float lightRadius) {
        new LightSourceComponent(this, lightRadius);
    }

    private void setupPositionComponent() {
        new PositionComponent(this, Game.currentLevel.getRandomFloorTile().getCoordinateAsPoint());
    }
}

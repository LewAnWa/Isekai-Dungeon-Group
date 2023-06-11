package ecs.entities;

import ecs.components.PositionComponent;
import starter.Game;

public class LightSource extends Entity {

    private final float lightRadius;

    public LightSource(float lightRadius) {
        this.lightRadius = lightRadius;
        setupPositionComponent();
    }

    private void setupPositionComponent() {
        new PositionComponent(this, Game.currentLevel.getRandomFloorTile().getCoordinateAsPoint());
    }

    public float getLightRadius() {
        return lightRadius;
    }
}

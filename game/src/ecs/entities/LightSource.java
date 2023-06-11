package ecs.entities;

import ecs.components.LightSourceComponent;
import ecs.components.PositionComponent;
import starter.Game;

public class LightSource extends Entity {

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

package ecs.entities.traps;

import ecs.components.HitboxComponent;
import ecs.components.PositionComponent;
import ecs.entities.monsters.Monster;
import level.elements.ILevel;
import starter.Game;
import tools.Point;

public class Teleporter extends Trap {

    private boolean active = true;

    public Teleporter(Point playerPos, ILevel currentLevel) {
        super(playerPos, currentLevel);

        pathToIdle = "teleporter/teleport_active";

        setupAnimationComponent();
        setHitboxComponent();

    }

    @Override
    protected void setupAnimationComponent() {

    }

    @Override
    protected void setHitboxComponent() {
        new HitboxComponent(this,
            (you, other, direction) -> {
                if (!(other instanceof Monster)) {
                    other.getComponent(PositionComponent.class)
                        .ifPresent(
                            pC -> {
                                PositionComponent positionComponent = (PositionComponent) pC;
                                positionComponent.setPosition(Game.currentLevel.getRandomFloorTile().getCoordinateAsPoint());
                            });
                }
            }, (you, other, direction) -> System.out.print(""));
    }
}

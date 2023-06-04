package ecs.entities.traps;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.damage.Damage;
import ecs.entities.Entity;
import graphic.Animation;
import level.elements.ILevel;
import tools.Point;


public abstract class Trap extends Entity {

    protected String pathToIdle;
    private Damage damage;

    public Trap(Point playerPos, ILevel currentLevel) {
        super();

        setUpPositionComponent(playerPos, currentLevel);
    }

    protected void setUpPositionComponent(Point playerPos, ILevel currentLevel) {
        Point randomPoint;

        do {
            randomPoint = currentLevel.getRandomFloorTile().getCoordinateAsPoint();
        } while (Point.calculateDistance(playerPos, randomPoint) < 3);

        new PositionComponent(this, randomPoint);
    }

    protected void setupAnimationComponent() {
        Animation idle = AnimationBuilder.buildAnimation(pathToIdle);
        new AnimationComponent(this, idle);
    }

    protected abstract void setHitboxComponent();

}

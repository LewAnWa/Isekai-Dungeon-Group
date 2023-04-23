package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.idle.RadiusWalk;
import ecs.entities.Entity;
import graphic.Animation;
import level.elements.ILevel;

public abstract class Monster extends Entity {

    private final float xSpeed;
    private final float ySpeed;

    protected String pathToIdleLeft = "knight/idleLeft";
    protected String pathToIdleRight = "knight/idleRight";
    protected String pathToRunLeft = "knight/runLeft";
    protected String pathToRunRight = "knight/runRight";

    public Monster(float movementSpeed, ILevel currentLevel) {
        super();

        xSpeed = ySpeed = movementSpeed;

        new PositionComponent(this, currentLevel.getRandomFloorTile().getCoordinateAsPoint());
    }

    protected void setUpHealthComponent(int maxHealthPoints) {
        HealthComponent healthComponent = new HealthComponent(this);
        healthComponent.setMaximalHealthpoints(maxHealthPoints);
    }

    protected void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    protected void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    protected void setupHitboxComponent() {
        new HitboxComponent(
            this,
            (you, other, direction) -> System.out.println("monsterCollisionEnter"),
            (you, other, direction) -> System.out.println("monsterCollisionLeave"));
    }
}

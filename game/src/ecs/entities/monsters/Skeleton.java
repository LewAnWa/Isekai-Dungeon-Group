package ecs.entities.monsters;

import level.elements.ILevel;

public class Skeleton extends Monster {

    public Skeleton(float movementSpeed, ILevel currentLevel) {
        super(movementSpeed, currentLevel);

        pathToIdleLeft = "monster/skeleton/idleLeft";
        pathToIdleRight = "monster/skeleton/idleRight";
        pathToRunLeft = "monster/skeleton/runLeft";
        pathToRunRight = "monster/skeleton/runRight";

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(20);
    }
}

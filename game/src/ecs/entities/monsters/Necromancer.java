package ecs.entities.monsters;

import ecs.components.HealthComponent;
import level.elements.ILevel;

public class Necromancer extends Monster {

    public Necromancer(float movementSpeed, ILevel currentLevel) {
        super(movementSpeed, currentLevel);

        pathToIdleLeft = "monster/necromancer/idleLeft";
        pathToIdleRight = "monster/necromancer/idleRight";
        pathToRunLeft = "monster/necromancer/runLeft";
        pathToRunRight = "monster/necromancer/runRight";

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(15);
    }
}

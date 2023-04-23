package ecs.entities.monsters;

import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.transition.RangeTransition;
import level.elements.ILevel;

public class Bat extends Monster {

    public Bat(float movementSpeed, ILevel currentLevel) {
        super(movementSpeed, currentLevel);

        pathToIdleLeft = "monster/bat/idleLeft";
        pathToIdleRight = "monster/bat/idleRight";
        pathToRunLeft = "monster/bat/runLeft";
        pathToRunRight = "monster/bat/runRight";

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(10);
        setUpAIComponent();
    }

    protected void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        aiComponent.setFightAI(new CollideAI(1f));
        aiComponent.setTransitionAI(new RangeTransition(100f));
    }
}

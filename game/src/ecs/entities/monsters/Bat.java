package ecs.entities.monsters;

import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.transition.RangeTransition;

public class Bat extends Monster {

    /**
     * Constructor for the Bat.
     *
     * @param movementSpeed the speed of the Monster.
     * @param flux the possible fluctuation of the variables.
     */
    public Bat(float movementSpeed, int flux) {
        super(movementSpeed, 7+flux);

        pathToIdleLeft = "monster/bat/idleLeft";
        pathToIdleRight = "monster/bat/idleRight";
        pathToRunLeft = "monster/bat/runLeft";
        pathToRunRight = "monster/bat/runRight";

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(10 + flux);
        setUpAIComponent();
        setUpDamageComponent(2 + flux);
    }

    private void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        aiComponent.setFightAI(new CollideAI(1f));
        aiComponent.setTransitionAI(new RangeTransition(100f));
        // has no IdleAI, because the bat always knows where the player is
    }
}

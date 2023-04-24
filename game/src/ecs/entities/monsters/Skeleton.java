package ecs.entities.monsters;

import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.PatrouilleWalk;
import ecs.components.ai.transition.RangeTransition;

public class Skeleton extends Monster {

    public Skeleton(float movementSpeed, int flux) {
        super(movementSpeed, 5+flux);

        pathToIdleLeft = "monster/skeleton/idleLeft";
        pathToIdleRight = "monster/skeleton/idleRight";
        pathToRunLeft = "monster/skeleton/runLeft";
        pathToRunRight = "monster/skeleton/runRight";

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(20 + flux);
        setUpAIComponent();
        setUpDamageComponent(5 + flux);
    }

    private void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        aiComponent.setFightAI(new CollideAI(1f));
        aiComponent.setTransitionAI(new RangeTransition(10f));
        aiComponent.setIdleAI(new PatrouilleWalk(40f, 3, 3000, PatrouilleWalk.MODE.BACK_AND_FORTH));
    }
}

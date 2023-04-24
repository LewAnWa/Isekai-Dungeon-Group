package ecs.entities.monsters;

import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.ai.transition.RangeTransition;

public class Necromancer extends Monster {

    public Necromancer(float movementSpeed, int flux) {
        super(movementSpeed, 10+flux);

        pathToIdleLeft = "monster/necromancer/idleLeft";
        pathToIdleRight = "monster/necromancer/idleRight";
        pathToRunLeft = "monster/necromancer/runLeft";
        pathToRunRight = "monster/necromancer/runRight";

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(15 + flux);
        setUpAIComponent();
        setUpDamageComponent(8 + flux);
    }

    private void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        aiComponent.setFightAI(new CollideAI(1f));
        aiComponent.setTransitionAI(new RangeTransition(7f));
        aiComponent.setIdleAI(new RadiusWalk(30f, 10));
    }
}

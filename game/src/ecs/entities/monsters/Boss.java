package ecs.entities.monsters;

import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.BossAiPhase1;
import ecs.components.ai.idle.WalkToHero;
import ecs.components.ai.transition.RangeTransition;
import level.elements.ILevel;
import tools.Point;

public class Boss extends Monster {

    public Boss(float movementSpeed, Point playerPos, ILevel currentLevel) {
        super(movementSpeed, 7 , playerPos, currentLevel);

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(100);
        setUpAIComponent();

    }

    private void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        //aiComponent.setFightAI(new BossAiPhase1());
        aiComponent.setTransitionAI(new RangeTransition(2f));
        aiComponent.setIdleAI(new WalkToHero());
    }
}

package ecs.entities.monsters;

import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.ai.transition.RangeTransition;
import level.elements.ILevel;
import tools.Point;

public class Necromancer extends Monster {

    /**
     * Creates a new Necromancer
     *
     * @param movementSpeed the speed of the Monster.
     * @param flux the possible fluctuation of the variables.
     * @param playerPos the position of the player in the level.
     * @param currentLevel the current map.
     */
    public Necromancer(float movementSpeed, int flux, Point playerPos, ILevel currentLevel) {
        super(movementSpeed, 10 + flux, playerPos, currentLevel);

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

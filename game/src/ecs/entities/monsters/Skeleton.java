package ecs.entities.monsters;

import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.PatrouilleWalk;
import ecs.components.ai.transition.RangeTransition;
import level.elements.ILevel;
import tools.Point;

/** the skeleton is a basic and easy to kill Enemy in the dungeon */
public class Skeleton extends Monster {

    /**
     * Creates a new Skeleton
     *
     * @param movementSpeed the speed of the Monster.
     * @param flux the possible fluctuation of the variables.
     * @param playerPos the position of the player in the level.
     * @param currentLevel the current map.
     */
    public Skeleton(float movementSpeed, int flux, Point playerPos, ILevel currentLevel) {
        super(movementSpeed, 5 + flux, playerPos, currentLevel);

        pathToIdleLeftNormal = "monster/skeleton/idleLeft";
        pathToIdleRightNormal = "monster/skeleton/idleRight";
        pathToRunLeftNormal = "monster/skeleton/runLeft";
        pathToRunRightNormal = "monster/skeleton/runRight";

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
        aiComponent.setTransitionAI(new RangeTransition(7f));
        aiComponent.setIdleAI(new PatrouilleWalk(40f, 3, 3000, PatrouilleWalk.MODE.BACK_AND_FORTH));
    }
}

package ecs.entities.monsters;

import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.WalkToHero;
import ecs.components.ai.transition.RangeTransition;
import level.elements.ILevel;
import tools.Point;

/** the Bat is an Enemy which always flys to the Heros location*/
public class Bat extends Monster {

    /**
     * Creates a new Bat
     *
     * @param movementSpeed the speed of the Monster.
     * @param flux the possible fluctuation of the variables.
     * @param playerPos the position of the player in the level.
     * @param currentLevel the current map.
     */
    public Bat(float movementSpeed, int flux, Point playerPos, ILevel currentLevel) {
        super(movementSpeed, 7 + flux, playerPos, currentLevel);

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
        aiComponent.setFightAI(new CollideAI(3f));
        aiComponent.setTransitionAI(new RangeTransition(2f));
        aiComponent.setIdleAI(new WalkToHero());
    }
}

package ecs.entities.monsters;

import ecs.components.PositionComponent;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.fight.MeleeAI;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.ai.transition.RangeTransition;
import ecs.components.skill.FireballSkill;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import level.elements.ILevel;
import starter.Game;
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

        setupSkillComponent();

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(15 + flux);
        setUpAIComponent();
        setUpDamageComponent(8 + flux);
    }

    /* Sets up the Skill that the Necromancer will use */
    private void setupSkillComponent() {
        SkillComponent skillComp = new SkillComponent(this);
        skill = new Skill(new FireballSkill(
            () -> ((PositionComponent) Game.getHero().flatMap(hero -> hero.getComponent(PositionComponent.class)).orElseThrow()).getPosition(),
            this
        ), 5);
        skillComp.addSkill(skill);
    }

    /* Sets up the AIComponent of the Necromancer */
    private void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        aiComponent.setFightAI(new MeleeAI(5f, skill));
        aiComponent.setTransitionAI(new RangeTransition(5f));
        aiComponent.setIdleAI(new RadiusWalk(30f, 10));
    }
}

package ecs.entities.monsters;

import ecs.components.PositionComponent;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.BossAiPhase1;
import ecs.components.ai.idle.Fireballing;
import ecs.components.ai.idle.WalkToHero;
import ecs.components.ai.transition.RangeTransition;
import ecs.components.ai.transition.WalkToHeroTransition;
import ecs.components.skill.FireballSkill;
import ecs.components.skill.SchwertstichSkill;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import level.elements.ILevel;
import starter.Game;
import tools.Point;

public class Boss extends Monster {

    public Boss(float movementSpeed, Point playerPos, ILevel currentLevel) {
        super(movementSpeed, 50 , playerPos, currentLevel);

        pathToIdleLeft = "monster/ogre/idleLeft";
        pathToIdleRight = "monster/ogre/idleRight";
        pathToRunLeft = "monster/ogre/runLeft";
        pathToRunRight = "monster/ogre/runRight";

        setupSkillComponent();

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(100);
        setUpAIComponent();
        setUpDamageComponent(0);
    }

    private void setupSkillComponent() {
        SkillComponent skillComp = new SkillComponent(this);
        skill = new Skill(new FireballSkill(
            () -> ((PositionComponent) Game.getHero().flatMap(hero -> hero.getComponent(PositionComponent.class)).orElseThrow()).getPosition(),
            this
        ), 2);
        skillComp.addSkill(skill);

        skill2 = new Skill(new SchwertstichSkill(
            () -> ((PositionComponent) Game.getHero().flatMap(hero -> hero.getComponent(PositionComponent.class)).orElseThrow()).getPosition(),
            this
        ), 2);
        skillComp.addSkill(skill2);
    }

    private void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        aiComponent.setFightAI(new BossAiPhase1(2f, skill2));
        aiComponent.setTransitionAI(new WalkToHeroTransition(2f));
        aiComponent.setIdleAI(new Fireballing(skill));
    }
}

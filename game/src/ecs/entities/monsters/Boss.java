package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.HealthComponent;
import ecs.components.IOnDeathFunction;
import ecs.components.IOnHealthPercentage;
import ecs.components.PositionComponent;
import ecs.components.ai.AIComponent;

import ecs.components.ai.fight.BossAiPhase2;
import ecs.components.ai.fight.MeleeAI;
import ecs.components.ai.idle.Fireballing;
import ecs.components.ai.idle.JumpAI;
import ecs.components.ai.transition.RangeTransition;
import ecs.components.ai.transition.WalkToHeroTransition;
import ecs.components.skill.FireballSkill;
import ecs.components.skill.SchwertstichSkill;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import graphic.Animation;
import level.elements.ILevel;
import starter.Game;
import tools.Point;

public class Boss extends Monster {

    public Boss(float movementSpeed, Point playerPos, ILevel currentLevel) {
        super(movementSpeed, 50);

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
        setUpPositionComponent();
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
        ), 1);
        skillComp.addSkill(skill2);
    }

    private void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        aiComponent.setFightAI(new MeleeAI(2f, skill2));
        aiComponent.setTransitionAI(new WalkToHeroTransition(2f));
        aiComponent.setIdleAI(new Fireballing(skill));
    }

    @Override
    protected void setUpHealthComponent(int maxHealthPoints) {
        HealthComponent healthComponent = new HealthComponent(this);
        healthComponent.setMaximalHealthpoints(maxHealthPoints);
        healthComponent.setCurrentHealthpoints(maxHealthPoints);

        // A die- and hit-animation is required, else the game starts to bug out
        Animation deathAnim = AnimationBuilder.buildAnimation(pathToDeathAnim);
        healthComponent.setDieAnimation(deathAnim);
        healthComponent.setGetHitAnimation(deathAnim);
        healthComponent.setOnDeath(
            new IOnDeathFunction() {
                @Override
                public void onDeath(Entity entity) {
                    dropLoot(entity);
                }
            });
        // entity -> new AnimationComponent(entity, deathAnim)); // does nothing
        healthComponent.setOnHealthPercentage(
            new IOnHealthPercentage() {
                @Override
                public void onHealthPercentage(Entity entity) {
                    phase2(entity);
                }
            });
    }

    private void phase2(Entity entity) {
        entity.getComponent(AIComponent.class).
            ifPresent(aIComponent -> {
                    AIComponent aIC = (AIComponent) aIComponent;
                    aIC.setFightAI(new BossAiPhase2(2f, skill));
                    aIC.setTransitionAI(new RangeTransition(2f));
                    aIC.setIdleAI(new JumpAI(skill));
                });
    }

    public void setUpPositionComponent(){
        new PositionComponent(this, new Point(7,7));
    }
}

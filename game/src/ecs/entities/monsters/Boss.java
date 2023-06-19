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
import ecs.items.Apfel;
import ecs.items.ItemData;
import graphic.Animation;
import starter.Game;
import tools.Point;

import java.util.logging.Logger;

/** This is the Boss Monster for the Dungeon. The Hero encounters the Boss after reaching lvl 10*/
public class Boss extends Monster {
    private Logger logger = Logger.getLogger("Boss Logger");

    /**
     * Creates a new Boss Monster
     *
     * @param movementSpeed the speed of the Monster.
     * @param flux the possible fluctuation of the variables.
     */
    public Boss(float movementSpeed, int flux) {
        super(movementSpeed, 50 + flux);

        pathToIdleLeft = "monster/ogre/idleLeft";
        pathToIdleRight = "monster/ogre/idleRight";
        pathToRunLeft = "monster/ogre/runLeft";
        pathToRunRight = "monster/ogre/runRight";

        setupSkillComponent();

        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(150+(flux*2));
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
                    dropApfel(entity);
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
        logger.info("Boss HP: " + maxHealthPoints);
        System.out.println("Boss HP: " + maxHealthPoints);
    }

    private void phase2(Entity entity) {
        entity.getComponent(AIComponent.class).
            ifPresent(aIComponent -> {
                    AIComponent aIC = (AIComponent) aIComponent;
                    aIC.setFightAI(new BossAiPhase2(2f, skill2));
                    aIC.setTransitionAI(new RangeTransition(2f));
                    aIC.setIdleAI(new JumpAI());
                });

        Game.spawnNecromancer();
        Game.spawnNecromancer();
    }

    private void setUpPositionComponent(){
        new PositionComponent(this, new Point(7,7));
    }

    private void dropApfel(Entity entity){
        ItemData droppedItem1 = new Apfel();
        ItemData droppedItem2 = new Apfel();

        PositionComponent psC =
            (PositionComponent) entity.getComponent(PositionComponent.class).get();

        droppedItem1.triggerDrop(entity, psC.getPosition());
        droppedItem2.triggerDrop(entity, psC.getPosition());
    }


}

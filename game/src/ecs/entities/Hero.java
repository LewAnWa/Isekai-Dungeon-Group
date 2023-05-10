package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import ecs.components.xp.XPComponent;
import graphic.Animation;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes .
 */
public class Hero extends Entity {

    private final int fireballCoolDown = 5;
    private final int frostBoltCoolDown = 4;
    private final int dashCoolDown = 1;
    private final int schwertStichCooldown = 1;
    private final int bumerangCooldown = 1;
    private final int magicArrowCooldown = 1;
    private final float xSpeed = 0.3f;
    private final float ySpeed = 0.3f;

    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private final String pathToHit = "knight/hit/";
    private final String pathToDeathAnim = "deathAnimation/";
    private Skill firstSkill;
    private Skill secondSkill;
    private Skill thirdSkill;
    private Skill fourthSkill;
    private Skill fifthSkill;
    private Skill sixthSkill;

    /**
     * Entity with Components
     */
    public Hero() {
        super();
        new PositionComponent(this);
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setupXPComponent();
        setupHealthComponent();
        PlayableComponent pc = new PlayableComponent(this);
        setupFireballSkill();
        setupFrostBoltSkill();
        setupDashSkill();
        setupSchwertstichSkill();
        setupBumerangSkill();
        setupMagicArrowSkill();

        //TODO: Abilities come first AND ONLY THEN the SkillComponent !!
        setupSkillComponent();
        pc.setSkillSlot1(firstSkill);
        pc.setSkillSlot2(secondSkill);
        pc.setSkillSlot3(thirdSkill);
        pc.setSkillSlot4(fourthSkill);
        pc.setSkillSlot5(fifthSkill);
        pc.setSkillSlot6(sixthSkill);
        setupManaComponent();
        setupStaminaComponent();
    }

    private void setupHealthComponent() {
        HealthComponent comp = new HealthComponent(this);
        comp.setMaximalHealthpoints(100);
        comp.setCurrentHealthpoints(comp.getMaximalHealthpoints());
        comp.setGetHitAnimation(AnimationBuilder.buildAnimation(pathToHit));
        comp.setDieAnimation(AnimationBuilder.buildAnimation(pathToDeathAnim));
    }

    private void setupStaminaComponent() {
        new StaminaComponent(this, 20);
    }

    private void setupManaComponent() {
        new ManaComponent(this, 20);
    }

    private void setupSkillComponent() {
        SkillComponent skillComponent = new SkillComponent(this);
        skillComponent.addSkill(firstSkill);
        skillComponent.addSkill(secondSkill);
        skillComponent.addSkill(thirdSkill);
        skillComponent.addSkill(fourthSkill);
        skillComponent.addSkill(fifthSkill);
        skillComponent.addSkill(sixthSkill);
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupFireballSkill() {
        firstSkill =
            new Skill(
                new FireballSkill(SkillTools::getCursorPositionAsPoint, this), fireballCoolDown);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
            this,
            (you, other, direction) -> System.out.println("heroCollisionEnter"),
            (you, other, direction) -> System.out.println("heroCollisionLeave"));
    }

    private void setupFrostBoltSkill() {
        secondSkill =
            new Skill(
                new FrostBoltSkill(SkillTools::getCursorPositionAsPoint), frostBoltCoolDown);
    }

    private void setupXPComponent() {
        XPComponent xpcomponent = new XPComponent(this);
        xpcomponent.setCurrentLevel(1);
    }

    private void setupDashSkill() {
        thirdSkill =
            new Skill(
                new DashSkill(), dashCoolDown);
    }

    private void setupSchwertstichSkill() {
        fourthSkill =
            new Skill(
                new SchwertstichSkill(SkillTools::getCursorPositionAsPoint, this), schwertStichCooldown);
    }

    private void setupBumerangSkill() {
        fifthSkill =
            new Skill(
                new BumerangSkill(SkillTools::getCursorPositionAsPoint, this), bumerangCooldown);
    }

    private void setupMagicArrowSkill() {
        sixthSkill = new Skill(new MagicArrowSkill(SkillTools::getCursorPositionAsPoint, this), magicArrowCooldown);
    }


}

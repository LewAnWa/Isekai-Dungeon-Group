package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import ecs.components.xp.ILevelUp;
import ecs.components.xp.XPComponent;
import graphic.Animation;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes .
 */
public class Hero extends Entity implements ILevelUp {

    private final int fireballCoolDown = 5;
    private final int frostBoltCoolDown = 4;
    private final int dashCoolDown = 1;
    private final float xSpeed = 0.3f;
    private final float ySpeed = 0.3f;

    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private Skill firstSkill;
    private Skill secondSkill;
    private Skill thirdSkill;

    /** Entity with Components */
    public Hero() {
        super();
        new PositionComponent(this);
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setupXPComponent();
        PlayableComponent pc = new PlayableComponent(this);
        setupFireballSkill();
        setupFrostBoltSkill();
        setupDashSkill();
        setupSkillComponent();
        pc.setSkillSlot1(firstSkill);
        pc.setSkillSlot2(secondSkill);
        pc.setSkillSlot3(thirdSkill);
        setupManaComponent();
        setupStaminaComponent();
    }

    /**
     * This is supposed to increase the hero's maxHP, maxMana and maxStamina.
     * Time wasted here: 4 hours
     * (Please add to the time wasted, if you are trying to fix this)
     *
     * @param nexLevel is the new level of the entity
     */
    @Override
    public void onLevelUp(long nexLevel) {
        System.out.println("LEVEL UP");

        this.getComponent(HealthComponent.class).ifPresent(component -> {
            HealthComponent comp = (HealthComponent) component;
            comp.setMaximalHealthpoints(comp.getMaximalHealthpoints() + 20);
            }
        );

        this.getComponent(ManaComponent.class).ifPresent(component -> {
                ManaComponent comp = (ManaComponent) component;
                comp.setMaximalManaPoints(comp.getMaximalManaPoints() + 20);
            }
        );

        this.getComponent(StaminaComponent.class).ifPresent(component -> {
                StaminaComponent comp = (StaminaComponent) component;
                comp.setMaxStamina(comp.getMaxStamina() + 20);
            }
        );
    }

    private void setupStaminaComponent() {
        new StaminaComponent(this, 20);
    }

    private void setupManaComponent() {
        new ManaComponent(this, 20);
    }

    private void setupSkillComponent(){
        SkillComponent skillComponent = new SkillComponent(this);
        skillComponent.addSkill(firstSkill);
        skillComponent.addSkill(secondSkill);
        skillComponent.addSkill(thirdSkill);
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
}

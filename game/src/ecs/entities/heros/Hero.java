package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import ecs.components.xp.XPComponent;
import ecs.entities.Entity;
import graphic.Animation;

/**
 * The Hero is the player character. It's an entity in the ECS.
 * This class helps to set up the hero with
 * all its base components and attributes.
 */
public abstract class Hero extends Entity {

    protected PlayableComponent playableComponent;
    protected SkillComponent skillComponent;
    protected Skill firstSkill;
    protected Skill secondSkill;
    protected Skill thirdSkill;

    protected boolean isVisible = false;

    /** Entity with Components */
    public Hero(int healthPoints, int staminaPoints, String pathToHit) {
        super();
        setupPositionComponent();
        setupHitboxComponent();
        setupXPComponent();
        setupHealthComponent(healthPoints, pathToHit);
        setupStaminaComponent(staminaPoints);
        setupInventoryComponent();
        setupSkillComponent();
        setupPlayableComponent();
    }

    private void setupPositionComponent() {
        new PositionComponent(this);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
            this,
            (you, other, direction) -> System.out.print(""),
            (you, other, direction) -> System.out.print(""));
    }

    private void setupXPComponent() {
        XPComponent xpcomponent = new XPComponent(this);
        xpcomponent.setCurrentLevel(1);
    }

    private void setupHealthComponent(int healthPoints, String pathToHit) {
        HealthComponent comp = new HealthComponent(this);
        comp.setMaximalHealthpoints(healthPoints);
        comp.setCurrentHealthpoints(comp.getMaximalHealthpoints());
        comp.setGetHitAnimation(AnimationBuilder.buildAnimation(pathToHit));
        comp.setDieAnimation(AnimationBuilder.buildAnimation("deathAnimation/"));
    }

    private void setupStaminaComponent(int staminaPoints) {
        new StaminaComponent(this, staminaPoints);
    }

    private void setupInventoryComponent() {
        new InventoryComponent(this, 9);
    }

    private void setupSkillComponent() {
        skillComponent = new SkillComponent(this);
    }

    private void setupPlayableComponent() {
        playableComponent = new PlayableComponent(this);
    }
}

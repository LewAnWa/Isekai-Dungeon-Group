package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.ManaComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import graphic.Animation;

/**
 * The Mage is a playable character which has the following skills:
 * - Fireball
 * - Frostbolt
 * - Teleport
 * <p>
 * This character class is the only one to own a ManaComponent.
 */
public class Mage extends Hero {

    private static final String pathToIdleRight = "character/hero/mage/idleRight";
    private static final String pathToIdleLeft = "character/hero/mage/idleLeft";
    private static final String pathToRunLeft = "character/hero/mage/runLeft";
    private static final String pathToRunRight = "character/hero/mage/runRight";
    private static final String pathToHit = "character/hero/mage/hit";

    public Mage() {
        super(80, 15, pathToHit);
        setupAnimationComponent();
        setupManaComponent();
        setupVelocityComponent();
        setupSkills();

        pathToUITexture = pathToIdleRight + "/wizzard_m_idle_anim_f0.png";
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupManaComponent() {
        new ManaComponent(this, 80);
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        float ySpeed = 0.3f;
        float xSpeed = 0.3f;
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupSkills() {
        // Fireball
        firstSkill = new Skill(
            new FireballSkill(SkillTools::getCursorPositionAsPoint, this),
            3,
            FireballSkill.pathToTextureUI);

        playableComponent.setSkillSlot1(firstSkill);
        skillComponent.addSkill(firstSkill);

        // Frostbolt
        secondSkill = new Skill(
            new FrostBoltSkill(SkillTools::getCursorPositionAsPoint),
            3,
            FrostBoltSkill.pathToTextureUI);

        playableComponent.setSkillSlot2(secondSkill);
        skillComponent.addSkill(secondSkill);

        // Teleport
        thirdSkill = new Skill(
            new TeleportSkill(5),
            5,
            TeleportSkill.pathToTextureUI);

        playableComponent.setSkillSlot3(thirdSkill);
        skillComponent.addSkill(thirdSkill);
    }

    @Override
    public String toString() {
        return "Mage";
    }
}

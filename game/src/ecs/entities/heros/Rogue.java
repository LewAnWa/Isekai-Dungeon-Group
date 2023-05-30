package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import graphic.Animation;

/**
 * The Rogue is a playable character which has the following skills:
 * - Sword stab
 * - Invisibility
 * - Dash
 */
public class Rogue extends Hero {

    private static final String pathToIdleRight = "character/hero/rogue/idleRight";
    private static final String pathToIdleLeft = "character/hero/rogue/idleLeft";
    private static final String pathToRunLeft = "character/hero/rogue/runLeft";
    private static final String pathToRunRight = "character/hero/rogue/runRight";
    private static final String pathToHit = "character/hero/rogue/hit";

    public Rogue() {
        super(100, 80, pathToHit);
        setupAnimationComponent();
        setupVelocityComponent();
        setupSkills();

        pathToUITexture = pathToIdleRight + "/lizard_m_idle_anim_f0.png";
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        float ySpeed = 0.3f;
        float xSpeed = 0.3f;
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupSkills() {
        // Sword
        firstSkill = new Skill(
            new SchwertstichSkill(SkillTools::getCursorPositionAsPoint, this),
            1,
            SchwertstichSkill.pathToTextureUI);

        playableComponent.setSkillSlot1(firstSkill);
        skillComponent.addSkill(firstSkill);

        // Invisibility
        secondSkill = new Skill(
            new InvisibilitySkill(1),
            1,
            InvisibilitySkill.pathToTextureUI);

        playableComponent.setSkillSlot2(secondSkill);
        skillComponent.addSkill(secondSkill);

        // Dash
        thirdSkill = new Skill(
            new DashSkill(),
            1,
            DashSkill.pathToTextures);

        playableComponent.setSkillSlot3(thirdSkill);
        skillComponent.addSkill(thirdSkill);
    }

    @Override
    public String toString() {
        return "Rogue";
    }
}

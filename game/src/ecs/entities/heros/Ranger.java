package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import graphic.Animation;

public class Ranger extends Hero {

    private static final String pathToIdleRight = "character/hero/ranger/idleRight";
    private static final String pathToIdleLeft = "character/hero/ranger/idleLeft";
    private static final String pathToRunLeft = "character/hero/ranger/runLeft";
    private static final String pathToRunRight = "character/hero/ranger/runRight";
    private static final String pathToHit = "character/hero/ranger/hit";

    public Ranger() {
        super(100, 60, pathToHit);
        setupAnimationComponent();
        setupVelocityComponent();
        setupSkills();

        pathToUITexture = pathToIdleRight + "/elf_f_idle_anim_f0.png";
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
        // magic Arrow
        firstSkill = new Skill(
            new MagicArrowSkill(SkillTools::getCursorPositionAsPoint, this),
            1,
            MagicArrowSkill.pathToTextureUI);

        playableComponent.setSkillSlot1(firstSkill);
        skillComponent.addSkill(firstSkill);

        // Boomerang
        secondSkill = new Skill(
            new BumerangSkill(SkillTools::getCursorPositionAsPoint, this),
            2,
            BumerangSkill.pathToTextureUI);

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
        return "Ranger";
    }
}

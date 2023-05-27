package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.ManaComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import graphic.Animation;

public class Mage extends Hero {

    private static final String pathToIdleRight = "hero/mage/idleRight";
    private static final String pathToIdleLeft = "hero/mage/idleLeft";
    private static final String pathToRunLeft = "hero/mage/runLeft";
    private static final String pathToRunRight = "hero/mage/runRight";
    private static final String pathToHit = "hero/mage/hit";

    public Mage() {
        super(80, 15, pathToHit);
        setupAnimationComponent();
        setupManaComponent();
        setupVelocityComponent();
        setupSkills();
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
        // TODO: IMPLEMENT
    }
}

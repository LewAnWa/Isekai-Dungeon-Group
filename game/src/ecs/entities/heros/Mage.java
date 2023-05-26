package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.ManaComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import graphic.Animation;

public class Mage extends Hero {

    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private final String pathToHit = "knight/hit/";

    public Mage() {
        super(80, 15, "knight/hit/");
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
            4);

        playableComponent.setSkillSlot1(firstSkill);
        skillComponent.addSkill(firstSkill);

        // Frostbolt
        secondSkill = new Skill(
            new FrostBoltSkill(SkillTools::getCursorPositionAsPoint),
            4);

        playableComponent.setSkillSlot2(secondSkill);
        skillComponent.addSkill(secondSkill);

        // Teleport
        // TODO: IMPLEMENT
    }
}

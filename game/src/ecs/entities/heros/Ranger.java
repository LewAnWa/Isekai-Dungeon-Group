package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import graphic.Animation;

public class Ranger extends Hero {

    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private final String pathToHit = "knight/hit/";

    public Ranger() {
        super(100, 60);
        setupAnimationComponent();
        setupVelocityComponent();
        setupSkills();

        getComponent(HealthComponent.class).ifPresent(component -> {
            ((HealthComponent) component).setDieAnimation(AnimationBuilder.buildAnimation(pathToHit));
        });
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
            1);

        playableComponent.setSkillSlot1(firstSkill);
        skillComponent.addSkill(firstSkill);

        // boomerang
        secondSkill = new Skill(
            new BumerangSkill(SkillTools::getCursorPositionAsPoint, this),
            2);

        playableComponent.setSkillSlot2(secondSkill);
        skillComponent.addSkill(secondSkill);

        // Dash
        thirdSkill = new Skill(new DashSkill(), 1);

        playableComponent.setSkillSlot3(thirdSkill);
        skillComponent.addSkill(thirdSkill);
    }
}

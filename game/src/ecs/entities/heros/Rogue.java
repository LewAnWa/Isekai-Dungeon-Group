package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.DashSkill;
import ecs.components.skill.SchwertstichSkill;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillTools;
import graphic.Animation;

public class Rogue extends Hero {

    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private final String pathToHit = "knight/hit/";

    public Rogue() {
        super(100, 80);
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
        // Sword
        firstSkill = new Skill(
            new SchwertstichSkill(SkillTools::getCursorPositionAsPoint, this),
            1);

        playableComponent.setSkillSlot1(firstSkill);
        skillComponent.addSkill(firstSkill);

        // Invisibility
        // TODO: IMPLEMENT

        // Dash
        thirdSkill = new Skill(new DashSkill(), 1);

        playableComponent.setSkillSlot3(thirdSkill);
        skillComponent.addSkill(thirdSkill);
    }
}

package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.DashSkill;
import ecs.components.skill.SchwertstichSkill;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillTools;
import graphic.Animation;

public class Rogue extends Hero {

    private static final String pathToIdleRight = "hero/rogue/idleRight";
    private static final String pathToIdleLeft = "hero/rogue/idleLeft";
    private static final String pathToRunLeft = "hero/rogue/runLeft";
    private static final String pathToRunRight = "hero/rogue/runRight";
    private static final String pathToHit = "hero/rogue/hit";

    public Rogue() {
        super(100, 80, pathToHit);
        setupAnimationComponent();
        setupVelocityComponent();
        setupSkills();
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
        // TODO: IMPLEMENT

        // Dash
        thirdSkill = new Skill(
            new DashSkill(),
            1,
            DashSkill.pathToTextures);

        playableComponent.setSkillSlot3(thirdSkill);
        skillComponent.addSkill(thirdSkill);
    }
}
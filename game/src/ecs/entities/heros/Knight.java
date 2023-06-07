package ecs.entities.heros;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.DashSkill;
import ecs.components.skill.SchwertstichSkill;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillTools;
import graphic.Animation;

/** The Knight is a playable character which has the following skills: - Sword stab - Dash */
public class Knight extends Hero {

    private static final String pathToIdleRight = "character/hero/knight/idleRight";
    private static final String pathToIdleLeft = "character/hero/knight/idleLeft";
    private static final String pathToRunLeft = "character/hero/knight/runLeft";
    private static final String pathToRunRight = "character/hero/knight/runRight";
    private static final String pathToHit = "character/hero/knight/hit";

    /** Default constructor for a Knight Hero */
    public Knight() {
        super(130, 40, pathToHit);
        setupAnimationComponent();
        setupVelocityComponent();
        setupSkills();

        pathToUITexture = pathToIdleRight + "/knight_m_idle_anim_f0.png";
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
        firstSkill =
                new Skill(
                        new SchwertstichSkill(SkillTools::getCursorPositionAsPoint, this),
                        0.5f,
                        SchwertstichSkill.pathToTextureUI);

        playableComponent.setSkillSlot1(firstSkill);
        skillComponent.addSkill(firstSkill);

        // Dash
        thirdSkill = new Skill(new DashSkill(), 1, DashSkill.pathToTextures);

        playableComponent.setSkillSlot3(thirdSkill);
        skillComponent.addSkill(thirdSkill);
    }

    @Override
    public String toString() {
        return "Knight";
    }
}

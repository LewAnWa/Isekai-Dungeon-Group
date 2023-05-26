package ecs.components.skill;

public class DashSkill extends MovementSkill {

    public static final String pathToTextures = "skills/dash/";

    /**
     * This Skill is a MovementSkill It can be used to Dash into the currently headed direction The
     * Velocity the Hero will receive while dashing can be set here
     */
    public DashSkill() {
        super(1f, 5);
    }
}

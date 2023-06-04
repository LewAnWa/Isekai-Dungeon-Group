package ecs.components.skill;

public class DashSkill extends MovementSkill {

    /**
     * This Skill is a MovementSkill It can be used to Dash into the currently headed direction The
     * Velocity the Entity will receive while dashing can be set here
     *
     * @param entitySpeed the base speed of the entity.
     */
    public DashSkill(float entitySpeed) {
        super(entitySpeed, 5);
    }

    /**
     * This Skill is a MovementSkill It can be used to Dash into the currently headed direction The
     * Velocity the Entity will receive while dashing can be set here
     */
    public DashSkill() {
        super(0.3f, 5);
    }
}

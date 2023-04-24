package ecs.components.skill;

import ecs.entities.Entity;

public abstract class MovementSkill implements ISkillFunction{

    private float heroSpeed;
    private float dashDuration;

    public MovementSkill (float heroSpeed, float dashDuration){
        this.heroSpeed = heroSpeed;
        this.dashDuration = dashDuration;
    }
    @Override
    public void execute(Entity entity) {

    }
}

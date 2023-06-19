package ecs.components.ai.idle;


import ecs.components.ai.AITools;
import ecs.components.skill.Skill;
import ecs.entities.Entity;


public class Fireballing implements IIdleAI {

    private Skill fireballSkill;


    public Fireballing(Skill fireballSkill) {
        this.fireballSkill = fireballSkill;
    }

    @Override
    public void idle(Entity entity) {
        if (!AITools.playerInRange(entity, 4f)) {
            fireballSkill.execute(entity);
        }
    }
}

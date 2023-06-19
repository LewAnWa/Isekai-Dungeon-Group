package ecs.components.ai.fight;

import ecs.components.ai.AITools;
import ecs.components.skill.Skill;
import ecs.entities.Entity;

public class BossAiPhase1 implements IFightAI{

    private Skill fightSkill;
    private float attackRangeSwitch;

    public BossAiPhase1(float attackRangeSwitch, Skill fightSkill){
        this.attackRangeSwitch = attackRangeSwitch;
        this.fightSkill = fightSkill;
    }

    @Override
    public void fight(Entity entity) {
        //Logik Bosskampf

        if(!AITools.playerInRange(entity, attackRangeSwitch));
    }
}

package ecs.components.ai.fight;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.ai.AITools;
import ecs.components.skill.Skill;
import ecs.entities.Entity;
import level.elements.tile.Tile;
import tools.Constants;

public class BossAiPhase1 implements IFightAI {

    private Skill meleeSkill;
    private float attackRangeMelee;
    private final int delay = Constants.FRAME_RATE;
    private int timeSinceLastUpdate = 0;
    private GraphPath<Tile> path;

    public BossAiPhase1(float attackRangeMelee, Skill meleeSkill) {
        this.attackRangeMelee = attackRangeMelee;
        this.meleeSkill = meleeSkill;
    }

    @Override
    public void fight(Entity entity) {
        if (AITools.playerInRange(entity, attackRangeMelee)) {
            meleeSkill.execute(entity);
        } else {
            if (timeSinceLastUpdate >= delay) {
                path = AITools.calculatePathToHero(entity);
                timeSinceLastUpdate = -1;
            }
            timeSinceLastUpdate++;
            AITools.move(entity, path);
        }
    }
}


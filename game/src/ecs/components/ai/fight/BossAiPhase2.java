package ecs.components.ai.fight;

import ecs.components.ai.AITools;
import ecs.components.skill.Skill;
import ecs.entities.Entity;
import java.util.Timer;
import java.util.TimerTask;

/** the fightbehavior of the boss monster after reaching 50% health */
public class BossAiPhase2 implements IFightAI {
    private final float attackRange;
    private final Skill meleeSkill;

    /**
     * constructor for the BossAiPhase2
     *
     * @param attackRange range of the used skill
     * @param meleeSkill skill used to attack the hero
     */
    public BossAiPhase2(float attackRange, Skill meleeSkill) {
        this.attackRange = attackRange;
        this.meleeSkill = meleeSkill;
    }

    @Override
    public void fight(Entity entity) {
        if (AITools.playerInRange(entity, attackRange)) {
            Timer timer = new Timer();

            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            meleeSkill.execute(entity);
                            timer.cancel();
                        }
                    },
                    1000);
        }
    }
}

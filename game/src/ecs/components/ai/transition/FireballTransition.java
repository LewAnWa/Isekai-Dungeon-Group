package ecs.components.ai.transition;

import ecs.components.ai.AITools;
import ecs.components.skill.Skill;
import ecs.entities.Entity;

import java.util.Timer;
import java.util.TimerTask;

public class FireballTransition implements ITransition {

    private final float range;
    private Skill fireball;
    private int fbCounter=0;

    /**
     * Switches to combat mode when the player is within range of the entity.
     *
     * @param range Range of the entity.
     */
    public FireballTransition(float range, Skill fireball) {
        this.range = range;
        this.fireball = fireball;
    }

    @Override
    public boolean isInFightMode(Entity entity) {
        return false;
    }
}

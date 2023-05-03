package ecs.components.skill;

import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import tools.Point;

public class FireballSkill extends DamageProjectileSkill {

    /**
     * The constructor for the fireball.
     * @param targetSelection preferably the cursor position.
     * @param user The Entity that shot the fireball. Can be null.
     */
    public FireballSkill(ITargetSelection targetSelection, Entity user) {
        super(
            "skills/fireball/fireBall_Down/",
            0.5f,
            new Damage(1, DamageType.FIRE, user),
            new Point(10, 10),
            targetSelection,
            5f);
    }
}

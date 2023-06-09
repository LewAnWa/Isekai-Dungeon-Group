package ecs.components.skill;

import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import tools.Point;

public class FireballSkill extends DamageProjectileSkill {

    public static final String pathToTextureUI = "skills/fireball/down/fireBall_Down4.png";

    /**
     * The constructor for the fireball.
     *
     * @param targetSelection preferably the cursor position.
     * @param user The Entity that shot the fireball. Can be null.
     */
    public FireballSkill(ITargetSelection targetSelection, Entity user) {
        super(
                "skills/fireball/",
                0.5f,
                new Damage(18, DamageType.FIRE, user),
                new Point(1, 1),
                targetSelection,
                5f);
    }
}

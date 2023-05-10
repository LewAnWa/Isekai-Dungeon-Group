package ecs.components.skill;

import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import tools.Point;

public class SchwertstichSkill extends DamageProjectileSkill {

    /**
     * The constructor for the schwertstich
     * @param targetSelection
     * @param user The Entity that used the sword
     */
    public SchwertstichSkill(ITargetSelection targetSelection, Entity user){
        super(
            "skills/schwertstich/",
            0.5f,
            new Damage(15, DamageType.PHYSICAL, user),
            new Point(11,11),
            targetSelection,
            1f);
    }
}

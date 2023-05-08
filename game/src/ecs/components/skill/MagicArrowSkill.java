package ecs.components.skill;

import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import tools.Point;

public class MagicArrowSkill extends DamageProjectileSkill{

    public MagicArrowSkill (ITargetSelection targetSelection, Entity user) {
        super(
            "skills/arrow/",
            0.5f,
            new Damage(2, DamageType.FIRE, user),
            new Point(10, 10),
            targetSelection,
            1000f);
    }

    @Override
    protected Damage calculateDmg(){
        dmgCalcTime = System.currentTimeMillis() - dmgCalcTime;
        return new Damage((int) (projectileDamage.damageAmount()*dmgCalcTime), projectileDamage.damageType(), projectileDamage.cause() );
    }
}

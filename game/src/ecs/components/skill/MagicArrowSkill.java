package ecs.components.skill;

import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import tools.Point;

public class MagicArrowSkill extends DamageProjectileSkill {

    public static final String pathToTextureUI = "skills/arrow/right/arrow_Right1.png";

    /**
     * The constructor for the Bumerang
     *
     * @param targetSelection preferably the cursor position.
     * @param user The Entity that used the sword
     */
    public MagicArrowSkill(ITargetSelection targetSelection, Entity user) {
        super(
            "skills/arrow/",
                0.5f,
                new Damage(15, DamageType.MAGIC, user),
                new Point(1, 1),
                targetSelection,
                1000f);
    }

    @Override
    protected Damage calculateDmg() {
        dmgCalcTime = (System.currentTimeMillis() - dmgCalcTime) / 100;
        return new Damage(
                (int) (projectileDamage.damageAmount() * dmgCalcTime),
                projectileDamage.damageType(),
                projectileDamage.cause());
    }
}

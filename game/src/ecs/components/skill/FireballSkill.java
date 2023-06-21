package ecs.components.skill;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.collision.ICollide;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import graphic.Animation;
import starter.Game;
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
                10f);
    }

    @Override
    public void execute(Entity entity) {
        Entity projectile = new Entity(true);

        dmgCalcTime = System.currentTimeMillis();

        PositionComponent epc =
                (PositionComponent)
                        entity.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        new PositionComponent(projectile, epc.getPosition());

        Point aimedOn = selectionFunction.selectTargetPoint();
        Point targetPoint =
                SkillTools.calculateLastPositionInRange(
                        epc.getPosition(), aimedOn, projectileRange);

        Animation animation =
                AnimationBuilder.buildAnimation(animationHelper(targetPoint, entity), 1);
        new AnimationComponent(projectile, animation);

        Point velocity =
                SkillTools.calculateVelocity(epc.getPosition(), targetPoint, projectileSpeed);
        VelocityComponent vc =
                new VelocityComponent(projectile, velocity.x, velocity.y, animation, animation);
        new ProjectileComponent(projectile, epc.getPosition(), targetPoint);
        ICollide collide =
                (a, b, from) -> {
                    if (b != entity && !b.isIgnorable()) {
                        b.getComponent(HealthComponent.class)
                                .ifPresent(
                                        hc -> {
                                            ((HealthComponent) hc).receiveHit(calculateDmg());
                                            Game.removeEntity(projectile);
                                        });
                        b.getComponent(PositionComponent.class)
                                .ifPresent(
                                        bpc -> {
                                            PositionComponent bComp = (PositionComponent) bpc;
                                            knockBack(epc, bComp);
                                        });
                    }
                };

        new HitboxComponent(
                projectile, new Point(0.25f, 0.25f), projectileHitboxSize, collide, null);

        new LightSourceComponent(projectile, 4.5f);
    }
}

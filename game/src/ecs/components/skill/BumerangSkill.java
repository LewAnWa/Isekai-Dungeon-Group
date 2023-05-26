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

public class BumerangSkill extends DamageProjectileSkill {

    public static final String pathToTextures = "skills/boomerrang/";

    /**
     * The constructor for the Bumerang
     *
     * @param targetSelection preferably the cursor position.
     * @param user The Entity that used the sword
     */
    public BumerangSkill(ITargetSelection targetSelection, Entity user) {
        super(
                pathToTextures,
                0.5f,
                new Damage(13, DamageType.PHYSICAL, user),
                new Point(10, 10),
                targetSelection,
                5f);
    }

    /*
    This specific execute is used when the boomerang is being shot for the first time.
     */
    @Override
    public void execute(Entity entity) {
        Entity projectile = new Entity(true, entity);

        dmgCalcTime = System.currentTimeMillis();

        PositionComponent epc =
                (PositionComponent)
                        entity.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        new PositionComponent(projectile, epc.getPosition());

        Animation animation = AnimationBuilder.buildAnimation(pathToTexturesOfProjectile);
        new AnimationComponent(projectile, animation);

        Point aimedOn = selectionFunction.selectTargetPoint();
        Point targetPoint =
                SkillTools.calculateLastPositionInRange(
                        epc.getPosition(), aimedOn, projectileRange);
        Point velocity =
                SkillTools.calculateVelocity(epc.getPosition(), targetPoint, projectileSpeed);
        VelocityComponent vc =
                new VelocityComponent(projectile, velocity.x, velocity.y, animation, animation);

        new ProjectileComponent(projectile, epc.getPosition(), targetPoint);

        ICollide collide =
                (a, b, from) -> {
                    if (b != entity) {
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
    }

    /**
     * A specific execute for when the boomerang has to be shot back to the hero.
     *
     * @param entity the original boomerang.
     * @param reachedMiddlePoint set this to true, else the boomerang will jump back and forth
     *     forever.
     */
    public void execute(Entity entity, boolean reachedMiddlePoint) {
        Entity projectile = new Entity(true, entity);
        projectile.reachedMiddlePoint = reachedMiddlePoint;

        dmgCalcTime = System.currentTimeMillis();

        PositionComponent epc =
                (PositionComponent)
                        entity.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        new PositionComponent(projectile, epc.getPosition());

        Animation animation = AnimationBuilder.buildAnimation(pathToTexturesOfProjectile);
        new AnimationComponent(projectile, animation);

        Point aimedOn = selectionFunction.selectTargetPoint();
        Point targetPoint =
                SkillTools.calculateLastPositionInRange(
                        epc.getPosition(), aimedOn, projectileRange);
        Point velocity =
                SkillTools.calculateVelocity(epc.getPosition(), targetPoint, projectileSpeed);
        VelocityComponent vc =
                new VelocityComponent(projectile, velocity.x, velocity.y, animation, animation);

        new ProjectileComponent(projectile, epc.getPosition(), targetPoint);

        ICollide collide =
                (a, b, from) -> {
                    if (b != entity.getUser()) {
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
    }
}

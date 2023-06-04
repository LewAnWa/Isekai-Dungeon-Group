package ecs.components.skill;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.collision.ICollide;
import ecs.damage.Damage;
import ecs.entities.Entity;
import graphic.Animation;
import starter.Game;
import tools.Point;

public abstract class DamageProjectileSkill implements ISkillFunction {

    protected long dmgCalcTime;
    protected String pathToTexturesOfProjectile;
    protected float projectileSpeed;
    protected float projectileRange;
    protected Damage projectileDamage;
    protected Point projectileHitboxSize;
    protected ITargetSelection selectionFunction;

    public DamageProjectileSkill(
            String pathToTexturesOfProjectile,
            float projectileSpeed,
            Damage projectileDamage,
            Point projectileHitboxSize,
            ITargetSelection selectionFunction,
            float projectileRange) {
        this.pathToTexturesOfProjectile = pathToTexturesOfProjectile;
        this.projectileDamage = projectileDamage;
        this.projectileSpeed = projectileSpeed;
        this.projectileRange = projectileRange;
        this.projectileHitboxSize = projectileHitboxSize;
        this.selectionFunction = selectionFunction;
    }

    @Override
    public void execute(Entity entity) {
        Entity projectile = new Entity();

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
     * calculates the DMG that will be caused by the projectile
     *
     * @return dmg value without further modification
     */
    protected Damage calculateDmg() {
        return projectileDamage;
    }

    /**
     * calculates the new Position of an Enemy after hitting it with a projectile
     *
     * @param epc position of the Hero
     * @param bComp position of the Enemy
     */
    protected void knockBack(PositionComponent epc, PositionComponent bComp) {
        float xwert = epc.getPosition().x - bComp.getPosition().x;
        float ywert = epc.getPosition().y - bComp.getPosition().y;

        Point newPoint;

        if (xwert > 0 && ywert < 0) { // rechts oberhalb
            if (Math.abs(xwert) > Math.abs(ywert)) { // weiter rechts als oberhalb
                newPoint = new Point(bComp.getPosition().x - 1f, bComp.getPosition().y);
                if (Game.currentLevel.getTileAt(newPoint.toCoordinate()).isAccessible()) {
                    bComp.setPosition(newPoint); // knock back nach links
                }
            } else {
                newPoint = new Point(bComp.getPosition().x, bComp.getPosition().y + 1);
                if (Game.currentLevel.getTileAt(newPoint.toCoordinate()).isAccessible()) {
                    bComp.setPosition(newPoint);
                }
            }

        } else if (xwert > 0 && ywert > 0) { // rechts unterhalb
            if (Math.abs(xwert) > Math.abs(ywert)) { // weiter rechts als unterhalb
                newPoint = new Point(bComp.getPosition().x - 1f, bComp.getPosition().y);
                if (Game.currentLevel.getTileAt(newPoint.toCoordinate()).isAccessible()) {
                    bComp.setPosition(newPoint);
                }
            } else {
                newPoint = new Point(bComp.getPosition().x, bComp.getPosition().y - 1f);
                if (Game.currentLevel.getTileAt(newPoint.toCoordinate()).isAccessible()) {
                    bComp.setPosition(newPoint);
                }
            }

        } else if (xwert < 0 && ywert < 0) { // links oberhalb
            if (Math.abs(xwert) > Math.abs(ywert)) { // weiter links als oberhalb
                newPoint = new Point(bComp.getPosition().x + 1f, bComp.getPosition().y);
                if (Game.currentLevel.getTileAt(newPoint.toCoordinate()).isAccessible()) {
                    bComp.setPosition(newPoint);
                }

            } else {
                newPoint = new Point(bComp.getPosition().x, bComp.getPosition().y + 1f);
                if (Game.currentLevel.getTileAt(newPoint.toCoordinate()).isAccessible()) {
                    bComp.setPosition(newPoint);
                }
            }

        } else if (xwert < 0 && ywert > 0) { // links unterhalb
            if (Math.abs(xwert) > Math.abs(ywert)) { // weiter links als unterhalb
                newPoint = new Point(bComp.getPosition().x + 1f, bComp.getPosition().y);
                if (Game.currentLevel.getTileAt(newPoint.toCoordinate()).isAccessible()) {
                    bComp.setPosition(newPoint);
                }

            } else {
                newPoint = new Point(bComp.getPosition().x, bComp.getPosition().y - 1f);
                if (Game.currentLevel.getTileAt(newPoint.toCoordinate()).isAccessible()) {
                    bComp.setPosition(newPoint);
                }
            }
        }
    }

    /**
     * Helps to choose the right path to textures of projectile (left, right, up, down)
     *
     * @param targetDirection preferably the cursor position.
     * @param entity preferably the Hero
     * @return the new path to textures of projectile
     */
    protected String animationHelper(Point targetDirection, Entity entity) {
        PositionComponent epc =
                (PositionComponent)
                        entity.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));

        float xwert = epc.getPosition().x - targetDirection.x;
        float ywert = epc.getPosition().y - targetDirection.y;
        if (xwert > 0 && ywert < 0) { // rechts oberhalb
            if (Math.abs(xwert) > Math.abs(ywert)) { // weiter rechts als oberhalb
                return pathToTexturesOfProjectile.concat("left/");
            } else {
                return pathToTexturesOfProjectile.concat("up/");
            }

        } else if (xwert > 0 && ywert > 0) { // rechts unterhalb
            if (Math.abs(xwert) > Math.abs(ywert)) {
                return pathToTexturesOfProjectile.concat("left/"); // weiter rechts als unterhalb
            } else {
                return pathToTexturesOfProjectile.concat("down/");
            }
        } else if (xwert < 0 && ywert < 0) { // links oberhalb
            if (Math.abs(xwert) > Math.abs(ywert)) {
                return pathToTexturesOfProjectile.concat("right/"); // weiter links als oberhalb
            } else {
                return pathToTexturesOfProjectile.concat("up/");
            }

        } else if (xwert < 0 && ywert > 0) { // links unterhalb
            if (Math.abs(xwert) > Math.abs(ywert)) {
                return pathToTexturesOfProjectile.concat("right/"); // weiter links als unterhalb
            } else {
                return pathToTexturesOfProjectile.concat("down/");
            }
        }
        return pathToTexturesOfProjectile;
    }
}

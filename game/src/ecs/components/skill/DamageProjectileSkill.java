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

        Animation animation = AnimationBuilder.buildAnimation(animationHelper(targetPoint, entity), 1);
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

                        /* // FIXME: THIS DOES NOT WORK, BECAUSE NORTH, EAST, SOUTH AND WEST ARE ALWAYS RELATIVE AND ARE NOT CONSTANTLY THE SAME!!!
                        b.getComponent(PositionComponent.class)
                                .ifPresent(
                                        pc -> {
                                            PositionComponent comp = (PositionComponent) pc;

                                            switch (from) {
                                                case N -> {comp.setPosition(new Point(comp.getPosition().x + 1f, comp.getPosition().y));
                                                    System.out.println("incoming north");}
                                                // knock back to WEST
                                                case E -> {comp.setPosition(new Point(comp.getPosition().x, comp.getPosition().y - 1f));
                                                    System.out.println("incoming east");}
                                                // knock back to NORTH
                                                case S -> {comp.setPosition(new Point(comp.getPosition().x - 1f, comp.getPosition().y));
                                                    System.out.println("incoming south");}
                                                // knock back to EAST
                                                case W -> {comp.setPosition(new Point(comp.getPosition().x, comp.getPosition().y + 1f));
                                                    System.out.println("incoming west");}
                                            }
                                        }
                                );
                        */
                    }
                };

        new HitboxComponent(
                projectile, new Point(0.25f, 0.25f), projectileHitboxSize, collide, null);
    }

    protected Damage calculateDmg(){
        return projectileDamage;
    }

    protected String animationHelper(Point targetDirection, Entity entity){
        System.out.println("ich funktioniere");
        PositionComponent epc =
            (PositionComponent)
                entity.getComponent(PositionComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("PositionComponent"));

        float xwert = epc.getPosition().x - targetDirection.x;
        float ywert = epc.getPosition().y - targetDirection.y;
        if (xwert > 0 && ywert < 0 ){ //rechts oberhalb vom monster
            if (Math.abs(xwert) > Math.abs(ywert)){ //weiter rechts als oberhalb --> left animation
                return pathToTexturesOfProjectile.concat("left/");
            }else {
                return pathToTexturesOfProjectile.concat("up/");
            }

        } else if (xwert > 0 && ywert > 0) { //rechts unterhalb des Monster
            if (Math.abs(xwert) > Math.abs(ywert)){ //weiter rechts als unterhalb
                return pathToTexturesOfProjectile.concat("left/"); //weriter rechts als unterhalb
            }else {
                return pathToTexturesOfProjectile.concat("down/");
            }
        } else if (xwert < 0 && ywert < 0) { //links oberhalb
            if (Math.abs(xwert) > Math.abs(ywert)){
                return pathToTexturesOfProjectile.concat("right/"); //weiter links als oberhalb
            }else {
                return pathToTexturesOfProjectile.concat("up/");
            }

        } else if (xwert < 0 && ywert > 0) { //links unterhalb
            if (Math.abs(xwert) > Math.abs(ywert)){
                return pathToTexturesOfProjectile.concat("right/"); //weiter links als unterhalb
            }else {
                return pathToTexturesOfProjectile.concat("down/");
            }

        }
        System.out.println(pathToTexturesOfProjectile);
        return  pathToTexturesOfProjectile;
    }
}

package ecs.components.skill;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.collision.ICollide;
import ecs.entities.Entity;
import graphic.Animation;
import starter.Game;
import tools.Point;

public abstract class ImpairingProjectileSkill implements ISkillFunction{
    private String pathToTexturesOfProjectile;
    private float projectileSpeed;

    private float projectileRange;
    private float projectileInfluence; //beeinflussung der Gegner mittels slow etc.
    private Point projectileHitboxSize;
    private int manaCost;

    private ITargetSelection selectionFunction;

    /**
     * @param pathToTexturesOfProjectile shows where to find the used textures for the Skill
     * @param projectileSpeed the velocity of the projectile
     * @param projectileInfluence the new lower velocity the hit entity will receive
     * @param projectileHitboxSize the hitbox size of the projectile
     * @param selectionFunction the point that is aimed for with the skill
     * @param projectileRange the range of the projectile
     * @param manaCost the mana needed to cast the Skill
     */

    public ImpairingProjectileSkill(
            String pathToTexturesOfProjectile,
            float projectileSpeed,
            float projectileInfluence,
            Point projectileHitboxSize,
            ITargetSelection selectionFunction,
            float projectileRange,
            int manaCost) {
        this.pathToTexturesOfProjectile = pathToTexturesOfProjectile;
        this.projectileInfluence = projectileInfluence;
        this.projectileSpeed = projectileSpeed;
        this.projectileRange = projectileRange;
        this.projectileHitboxSize = projectileHitboxSize;
        this.selectionFunction = selectionFunction;
        this.manaCost = manaCost;
    }

    /**
     * USING THE SKILL:
     * Create a new Entity for the Forstbolt when the ability is used.
     * this way, every Frostbolt projectile acts as an Entity
     * Create the needed Components for the Entity to make it functional in game
     * If the Frostbolt hits an enemy, the entity it collided with will receive reduced movement speed (Velocity)
     * afterward the new created projectile Entity will be deleted
     */

    @Override
    public void execute(Entity entity) {
        // get the ManaComponent of the Entity that want to execute this Skill
        entity.getComponent(ManaComponent.class).ifPresent(component -> {
            ManaComponent comp = (ManaComponent) component;

            // if enough Mana is present, then execute the Skill
            if (comp.getCurrentManaPoints() - manaCost >= 0) {
                // reduce the ManaPoints by the cost
                comp.setCurrentManaPoints(comp.getCurrentManaPoints() - manaCost);
                System.out.println(comp.getCurrentManaPoints()+"/"+comp.getMaximalManaPoints()+" MANA");

                Entity projectile = new Entity();

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

                Animation animation = AnimationBuilder.buildAnimation(animationHelper(targetPoint, entity));
                new AnimationComponent(projectile, animation);

                Point velocity =
                    SkillTools.calculateVelocity(epc.getPosition(), targetPoint, projectileSpeed);
                VelocityComponent vc =
                    new VelocityComponent(projectile, velocity.x, velocity.y, animation, animation);
                new ProjectileComponent(projectile, epc.getPosition(), targetPoint);

                ICollide collide =
                    (a, b, from) -> {
                        if (b != entity) {
                            b.getComponent(VelocityComponent.class)
                                .ifPresent(
                                    evc -> { //evc --> enemy velocity component
                                        ((VelocityComponent) evc).setXVelocity(projectileInfluence);
                                        ((VelocityComponent) evc).setYVelocity(projectileInfluence);
                                        Game.removeEntity(projectile);
                                    });
                        }
                    };

                new HitboxComponent(
                    projectile, new Point(0.25f, 0.25f), projectileHitboxSize, collide, null);


            }
            else {
                System.out.println("NO MANA???");
            }
        });
    }

    /**
     * Helps to choose the right path to textures of projectile (left, right, up, down)
     * @param targetDirection preferably the cursor position.
     * @param entity preferably the Hero
     * @return the new path to textures of projectile
     */
    protected String animationHelper(Point targetDirection, Entity entity){
        System.out.println("ich funktioniere");
        PositionComponent epc =
            (PositionComponent)
                entity.getComponent(PositionComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("PositionComponent"));

        float xwert = epc.getPosition().x - targetDirection.x;
        float ywert = epc.getPosition().y - targetDirection.y;
        if (xwert > 0 && ywert < 0 ){ //rechts oberhalb
            if (Math.abs(xwert) > Math.abs(ywert)){ //weiter rechts als oberhalb
                return pathToTexturesOfProjectile.concat("left/");
            }else {
                return pathToTexturesOfProjectile.concat("up/");
            }

        } else if (xwert > 0 && ywert > 0) { //rechts unterhalb
            if (Math.abs(xwert) > Math.abs(ywert)){
                return pathToTexturesOfProjectile.concat("left/"); //weiter rechts als unterhalb
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
        return  pathToTexturesOfProjectile;
    }
}

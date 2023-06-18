package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.skill.Skill;
import ecs.components.xp.XPComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;
import graphic.Animation;
import level.elements.ILevel;
import starter.Game;
import tools.Point;

/**
 * The Monster is an entity to encounter and fight against in the dungeon. There are several
 * different monster in the game.
 */
public abstract class Monster extends Entity {

    private final float xSpeed;
    private final float ySpeed;
    protected Skill skill;
    protected String pathToIdleLeft;
    protected String pathToIdleRight;
    protected String pathToRunLeft;
    protected String pathToRunRight;
    protected String pathToDeathAnim = "deathAnimation/";
    private Damage damage;

    /**
     * Default constructor for the monster.
     *
     * @param movementSpeed the speed of the monster.
     * @param lootAmount the amount of loot that should be dropped on death.
     * @param playerPos the position of the player in the level.
     * @param currentLevel the current map.
     */
    public Monster(float movementSpeed, long lootAmount, Point playerPos, ILevel currentLevel) {
        super();

        xSpeed = ySpeed = movementSpeed;

        setUpPositionComponent(playerPos, currentLevel);
        setupXPComponent(lootAmount);
    }

    protected void setUpDamageComponent(int damageAmount) {
        damage = new Damage(damageAmount, DamageType.PHYSICAL, this);
    }

    // Sets up the positionComponent of the Monster with a random point, which has a minimum
    // distance to the player
    protected void setUpPositionComponent(Point playerPos, ILevel currentLevel) {
        Point randomPoint;

        do {
            randomPoint = currentLevel.getRandomFloorTile().getCoordinateAsPoint();
        } while (Point.calculateDistance(playerPos, randomPoint) < 3);

        new PositionComponent(this, randomPoint);
    }

    protected void setupXPComponent(long lootAmount) {
        XPComponent xpComponent = new XPComponent(this);
        xpComponent.setLootXP(lootAmount);
    }

    protected void setUpHealthComponent(int maxHealthPoints) {
        HealthComponent healthComponent = new HealthComponent(this);
        healthComponent.setMaximalHealthpoints(maxHealthPoints);
        healthComponent.setCurrentHealthpoints(maxHealthPoints);

        // A die- and hit-animation is required, else the game starts to bug out
        Animation deathAnim = AnimationBuilder.buildAnimation(pathToDeathAnim);
        healthComponent.setDieAnimation(deathAnim);
        healthComponent.setGetHitAnimation(deathAnim);
        healthComponent.setOnDeath(
                new IOnDeathFunction() {
                    @Override
                    public void onDeath(Entity entity) {
                        dropLoot(entity);
                    }
                });
        // entity -> new AnimationComponent(entity, deathAnim)); // does nothing
    }

    protected void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    protected void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    protected void setupHitboxComponent() {
        new HitboxComponent(
                this,
                // on enter
                (you, other, direction) -> {
                    Game.getHero()
                            .ifPresent(
                                    hero -> {
                                        if (other == hero) {
                                            hero.getComponent(HealthComponent.class)
                                                    .ifPresent(
                                                            component -> {
                                                                ((HealthComponent) component)
                                                                        .receiveHit(damage);
                                                            });
                                        }
                                    });
                },
                (you, other, direction) -> System.out.print(""));
    }

    /**
     * Uses the ItemDataGenerator to drop an item on the Entitys location
     *
     * @param entity ideally the entity that has died
     */
    public void dropLoot(Entity entity) {
        ItemData droppedItem = new ItemDataGenerator().generateItemData();

        PositionComponent psC =
                (PositionComponent) entity.getComponent(PositionComponent.class).get();

        droppedItem.triggerDrop(entity, psC.getPosition());
    }
}

package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.xp.XPComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import graphic.Animation;

public abstract class Monster extends Entity{

    private final float xSpeed;
    private final float ySpeed;

    protected String pathToIdleLeft;
    protected String pathToIdleRight;
    protected String pathToRunLeft;
    protected String pathToRunRight;
    protected String pathToDeathAnim = "monster/deathAnimation";

    /**
     * Default constructor for the monster.
     *
     * @param movementSpeed the speed of the monster.
     * @param lootAmount the amount of loot that should be dropped on death.
     */
    public Monster(float movementSpeed, long lootAmount) {
        super();

        xSpeed = ySpeed = movementSpeed;

        setUpPositionComponent();
        setUpXPSystem(lootAmount);
    }

    protected void setUpDamageComponent(int damageAmount) {
        new Damage(damageAmount, DamageType.PHYSICAL, null);
    }

    protected void setUpPositionComponent() {
        new PositionComponent(this);
    }

    protected void setUpXPSystem(long lootAmount) {
        XPComponent xpComponent = new XPComponent(this);
        xpComponent.setLootXP(lootAmount);
    }

    protected void setUpHealthComponent(int maxHealthPoints) {
        HealthComponent healthComponent = new HealthComponent(this);
        healthComponent.setMaximalHealthpoints(maxHealthPoints);

        // A die- and hit-animation is required, else the game starts to bug out
        Animation deathAnim = AnimationBuilder.buildAnimation(pathToDeathAnim);
        healthComponent.setDieAnimation(deathAnim);
        healthComponent.setGetHitAnimation(deathAnim);
        healthComponent.setOnDeath(entity -> new AnimationComponent(entity, deathAnim)); // does nothing
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
            (you, other, direction) -> System.out.println("monsterCollisionEnter"),
            (you, other, direction) -> System.out.println("monsterCollisionLeave"));
    }
}

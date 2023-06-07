package ecs.entities.monsters;

import dslToGame.AnimationBuilder;
import ecs.components.HealthComponent;
import ecs.components.IOnDeathFunction;
import ecs.components.PositionComponent;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.ai.transition.RangeTransition;
import ecs.entities.Chest;
import ecs.entities.Entity;
import graphic.Animation;
import level.elements.ILevel;
import tools.Point;

/** The Mimic is an Enemy camouflaged as a Chest, it will attack the Hero after trying to loot it */
public class Mimic extends Monster {

    /**
     * Creates a new Mimic
     *
     * @param movementSpeed the speed of the Monster.
     * @param flux the possible fluctuation of the variables.
     * @param chestPos the position of the chest in the level.
     * @param currentLevel the current map.
     */
    public Mimic(float movementSpeed, int flux, Point chestPos, ILevel currentLevel) {
        super(movementSpeed, 0, chestPos, currentLevel);

        pathToIdleLeft = "monster/mimic/idleLeft";
        pathToIdleRight = "monster/mimic/idleRight";
        pathToRunLeft = "monster/mimic/runLeft";
        pathToRunRight = "monster/mimic/runRight";

        setUpPositionComponent(chestPos);
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setUpHealthComponent(20 + flux);
        setUpDamageComponent(5 + flux);
        setUpAIComponent();
    }

    @Override
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
                        spawnRewardChest(entity);
                    }
                });
        // entity -> new AnimationComponent(entity, deathAnim)); // does nothing
    }

    private void setUpPositionComponent(Point chestPos) {
        new PositionComponent(this, chestPos);
    }

    private void setUpAIComponent() {
        AIComponent aiComponent = new AIComponent(this);
        aiComponent.setFightAI(new CollideAI(1f));
        aiComponent.setTransitionAI(new RangeTransition(7f));
        aiComponent.setIdleAI(new RadiusWalk(30f, 2));
    }

    private void spawnRewardChest(Entity mimic) {
        PositionComponent psC =
                (PositionComponent) mimic.getComponent(PositionComponent.class).get();

        Chest.createMimicLootChest(psC.getPosition());
    }
}

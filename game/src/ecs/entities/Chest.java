package ecs.entities;

import ecs.components.*;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;
import graphic.Animation;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import level.tools.LevelElement;
import starter.Game;
import tools.Point;

/** A chest is an entity which drops loot when opened. */
public class Chest extends Entity {

    public static final float defaultInteractionRadius = 1f;
    public static final List<String> DEFAULT_CLOSED_ANIMATION_FRAMES =
            List.of("objects/treasurechest/chest_full_open_anim_f0.png");
    public static final List<String> DEFAULT_OPENING_ANIMATION_FRAMES =
            List.of(
                    "objects/treasurechest/chest_full_open_anim_f0.png",
                    "objects/treasurechest/chest_full_open_anim_f1.png",
                    "objects/treasurechest/chest_full_open_anim_f2.png",
                    "objects/treasurechest/chest_empty_open_anim_f2.png");

    public static final List<String> DEFAULT_CLOSED_MIMIC_ANIMATION_FRAMES =
            List.of("character/monster/mimic/camouflage/chest_mimic_f0.png");

    /**
     * small Generator which uses the Item#ITEM_REGISTER
     *
     * @return a configured Chest
     */
    public static Chest createNewChest() {
        Random random = new Random();
        ItemDataGenerator itemDataGenerator = new ItemDataGenerator();

        List<ItemData> itemData =
                IntStream.range(0, random.nextInt(1, 3))
                        .mapToObj(i -> itemDataGenerator.generateItemData())
                        .toList();
        return new Chest(
                itemData,
                Game.currentLevel.getRandomTile(LevelElement.FLOOR).getCoordinate().toPoint());
    }

    /**
     * generates the chest after killing a mimic chest
     *
     * @param mimicPos position where the mimic has been killed
     * @return a configured chest
     */
    public static Chest createMimicLootChest(Point mimicPos) {
        Random random = new Random();
        ItemDataGenerator itemDataGenerator = new ItemDataGenerator();

        List<ItemData> itemData =
                IntStream.range(0, random.nextInt(1, 3))
                        .mapToObj(i -> itemDataGenerator.generateItemData())
                        .toList();
        return new Chest(itemData, mimicPos);
    }

    /**
     * generates a false chest
     *
     * @return a chest that spawns a mimic on interaction
     */
    public static Chest createMonsterChest() {
        return new Chest(
                Game.currentLevel.getRandomTile(LevelElement.FLOOR).getCoordinate().toPoint(),
                DEFAULT_CLOSED_MIMIC_ANIMATION_FRAMES);
    }

    /**
     * Creates a new Chest which drops the given items on interaction
     *
     * @param itemData which the chest is supposed to drop
     * @param position the position where the chest is placed
     */
    public Chest(List<ItemData> itemData, Point position) {
        new PositionComponent(this, position);
        InventoryComponent ic = new InventoryComponent(this, itemData.size());
        itemData.forEach(ic::addItem);
        new InteractionComponent(this, defaultInteractionRadius, false, this::dropItems);
        AnimationComponent ac =
                new AnimationComponent(
                        this,
                        new Animation(DEFAULT_CLOSED_ANIMATION_FRAMES, 100, false),
                        new Animation(DEFAULT_OPENING_ANIMATION_FRAMES, 100, false));
        Game.setChest(this);
    }

    /**
     * Creates a new MONSTER-CHEST which spawns a mimic on interaction
     *
     * @param position the position where the chest is placed
     * @param animation slightly different animation than normal, to alert a mindful player
     */
    public Chest(Point position, List<String> animation) {
        new PositionComponent(this, position);
        new InteractionComponent(this, defaultInteractionRadius, false, this::spawnMimic);
        AnimationComponent ac =
                new AnimationComponent(
                        this,
                        new Animation(animation, 100, false),
                        new Animation(DEFAULT_OPENING_ANIMATION_FRAMES, 100, false));
        Game.setChest(this);
    }

    /** Decides whether a normal- or monster-chest will be created and creates only one of them */
    public static void spawnChest() {
        boolean thisIsAMimic = new Random().nextBoolean();

        if (thisIsAMimic) {
            createMonsterChest();
        } else {
            createNewChest();
        }
    }

    private void dropItems(Entity entity) {
        InventoryComponent inventoryComponent =
                entity.getComponent(InventoryComponent.class)
                        .map(InventoryComponent.class::cast)
                        .orElseThrow(
                                () ->
                                        createMissingComponentException(
                                                InventoryComponent.class.getName(), entity));
        PositionComponent positionComponent =
                entity.getComponent(PositionComponent.class)
                        .map(PositionComponent.class::cast)
                        .orElseThrow(
                                () ->
                                        createMissingComponentException(
                                                PositionComponent.class.getName(), entity));
        List<ItemData> itemData = inventoryComponent.getItems();
        double count = itemData.size();

        IntStream.range(0, itemData.size())
                .forEach(
                        index ->
                                itemData.get(index)
                                        .triggerDrop(
                                                entity,
                                                calculateDropPosition(
                                                        positionComponent, index / count)));
        entity.getComponent(AnimationComponent.class)
                .map(AnimationComponent.class::cast)
                .ifPresent(x -> x.setCurrentAnimation(x.getIdleRight()));
    }

    private void spawnMimic(Entity entity) {
        Game.spawnMimicEnemy();
        Game.removeEntity(entity);
    }

    /**
     * small Helper to determine the Position of the dropped item simple circle drop
     *
     * @param positionComponent The PositionComponent of the Chest
     * @param radian of the current Item
     * @return a Point in a unit Vector around the Chest
     */
    private static Point calculateDropPosition(PositionComponent positionComponent, double radian) {
        return new Point(
                (float) Math.cos(radian * Math.PI) + positionComponent.getPosition().x,
                (float) Math.sin(radian * Math.PI) + positionComponent.getPosition().y);
    }

    /**
     * Helper to create a MissingComponentException with a bit more information
     *
     * @param Component the name of the Component which is missing
     * @param e the Entity which did miss the Component
     * @return the newly created Exception
     */
    private static MissingComponentException createMissingComponentException(
            String Component, Entity e) {
        return new MissingComponentException(
                Component
                        + " missing in "
                        + Chest.class.getName()
                        + " in Entity "
                        + e.getClass().getName());
    }
}

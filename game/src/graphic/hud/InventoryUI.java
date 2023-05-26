package graphic.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.components.*;
import ecs.components.xp.XPComponent;
import ecs.entities.heros.Hero;
import ecs.items.Bag;
import ecs.items.ItemData;
import ecs.items.Schuh;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import starter.Game;
import tools.Point;

public class InventoryUI<T extends Actor> extends ScreenController<T> {

    public final Logger logger = Logger.getLogger("InventoryUI Logger");

    private ScreenText healthDisplay, manaDisplay, staminaDisplay, xpDisplay;
    private HealthComponent healthComp;
    private ManaComponent manaComp;
    private StaminaComponent staminaComp;
    private XPComponent xpComp;
    private InventoryComponent inventoryComp;
    private PositionComponent posComp;
    private ScreenImage inventory, skill1, skill2, skill3, skill4;
    private boolean visible = false;
    private boolean listUpdated = false;
    private List<Node> itemsList = new ArrayList<>();
    private int pointer = 0;
    private ScreenImage screenPointer;

    private static Point skillSlot1 = new Point(237, 295);
    private static Point skillSlot2 = new Point(302, 295);
    private static Point skillSlot3 = new Point(365, 285);
    private static Point skillSlot4 = new Point(432, 288);

    public InventoryUI(Hero hero) {
        super(new SpriteBatch());
        assignComponents(hero);
        buildInventory();
        buildSkillOverview();

        screenPointer = new ScreenImage("hud/selected.png", new Point(-100, -100));
        screenPointer.scaleBy(1.5f);
        add((T) screenPointer);

        hideScreen();
    }

    /**
     * Assigns the needed components from the hero to the local components to be worked with.
     *
     * @param hero The Hero whose stats should be displayed on the UI.
     */
    public void assignComponents(Hero hero) {
        hero.getComponent(HealthComponent.class)
                .ifPresent(
                        component -> {
                            logger.log(new LogRecord(Level.INFO, "HealthComponent detected!"));
                            healthComp = (HealthComponent) component;
                        });
        hero.getComponent(ManaComponent.class)
                .ifPresent(
                        component -> {
                            logger.log(new LogRecord(Level.INFO, "ManaComponent detected!"));
                            manaComp = (ManaComponent) component;
                        });
        hero.getComponent(StaminaComponent.class)
                .ifPresent(
                        component -> {
                            logger.log(new LogRecord(Level.INFO, "StaminaComponent detected!"));
                            staminaComp = (StaminaComponent) component;
                        });
        hero.getComponent(XPComponent.class)
                .ifPresent(
                        component -> {
                            logger.log(new LogRecord(Level.INFO, "XPComponent detected!"));
                            xpComp = (XPComponent) component;
                        });
        hero.getComponent(InventoryComponent.class)
                .ifPresent(
                        component -> {
                            logger.log(new LogRecord(Level.INFO, "InventoryComponent detected!"));
                            inventoryComp = (InventoryComponent) component;
                        });
        hero.getComponent(PositionComponent.class)
                .ifPresent(
                        component -> {
                            logger.log(new LogRecord(Level.INFO, "PositionComponent detected!"));
                            posComp = (PositionComponent) component;
                        });
    }

    /** Makes the screen invisible */
    public void hideScreen() {
        this.forEach((Actor s) -> s.setVisible(false));
        visible = false;
        listUpdated = false;
        itemsList = new ArrayList<>();
        pointer = 0;
    }

    /** Makes the screen visible */
    public void showScreen() {
        this.forEach((Actor s) -> s.setVisible(true));
        visible = true;
    }

    /** Updates the UIs information like the hero's healthPoints and more. */
    public void updateUI() {
        healthDisplay.setText("HEALTH: " + healthComp);
        manaDisplay.setText("MANA: " + manaComp);
        staminaDisplay.setText("STAMINA: " + staminaComp);
        xpDisplay.setText(String.valueOf(xpComp));

        if (visible && !listUpdated) listItems();

        if (visible && !itemsList.isEmpty()) {
            screenPointer.setPosition(
                    itemsList.get(pointer).screenPosition.x,
                    itemsList.get(pointer).screenPosition.y);

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                if (pointer > 0) pointer--;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                if (pointer < itemsList.size() - 1) pointer++;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (itemsList.get(pointer).item instanceof Bag
                        || itemsList.get(pointer).item instanceof Schuh) {
                    // do nothing with bag and shoes
                } else {

                    if (itemsList.get(pointer).bag != null) { // item that should be used from bag
                        Bag bagInQuestion =
                                inventoryComp
                                        .getBags()
                                        .get( // get all bags
                                                inventoryComp
                                                        .getBags()
                                                        .indexOf( // get index of the bag in
                                                                // question
                                                                itemsList.get(pointer)
                                                                        .bag // the bag in question
                                                                ));

                        bagInQuestion
                                .getItems()
                                .get( // get the item
                                        bagInQuestion
                                                .getItems()
                                                .indexOf(
                                                        itemsList.get(pointer)
                                                                .item) // get index of said item
                                        )
                                .triggerUse(inventoryComp.getEntity()); // use item

                        bagInQuestion.removeItem(itemsList.get(pointer).item); // remove item
                    } else { // item that should be used from inventory
                        itemsList.get(pointer).item.triggerUse(inventoryComp.getEntity());
                        inventoryComp.removeItem(itemsList.get(pointer).item);
                    }

                    remove((T) itemsList.get(pointer).image);
                    pointer = 0;
                    redraw();
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DEL)) {

                if (itemsList.get(pointer).bag != null) { // item that should be removed from bag
                    inventoryComp
                            .getBags()
                            .get( // get all bags
                                    inventoryComp
                                            .getBags()
                                            .indexOf( // get index of the bag in question
                                                    itemsList.get(pointer)
                                                            .bag // the bag in question
                                                    ))
                            .removeItem(itemsList.get(pointer).item); // remove the item in bag
                    itemsList
                            .get(pointer)
                            .item
                            .triggerDrop(
                                    inventoryComp.getEntity(), calculateDropPosition(posComp, 0));
                } else { // item that should be removed from inventory
                    if (itemsList.get(pointer).item instanceof Bag
                            && ((Bag) itemsList.get(pointer).item).isEmpty()) {
                        // removes the bag only if it is empty
                        inventoryComp.removeItem(itemsList.get(pointer).item);
                        itemsList
                                .get(pointer)
                                .item
                                .triggerDrop(
                                        inventoryComp.getEntity(),
                                        calculateDropPosition(posComp, 0));
                    } else if (itemsList.get(pointer).item instanceof Bag) {
                        // do nothing
                    } else { // remove the item out of the inventory
                        inventoryComp.removeItem(itemsList.get(pointer).item);
                        itemsList
                                .get(pointer)
                                .item
                                .triggerDrop(
                                        inventoryComp.getEntity(),
                                        calculateDropPosition(posComp, 0));
                    }
                }

                remove((T) itemsList.get(pointer).image);
                pointer = 0;
                redraw();
            }
        } else {
            screenPointer.setPosition(-100, -100);
        }
    }

    /**
     * small Helper to determine the Position of the dropped item simple circle drop
     *
     * @param positionComponent The PositionComponent of the Chest
     * @param radian of the current Item
     * @return a Point in a unit Vector around the Chest
     */
    private static Point calculateDropPosition(PositionComponent positionComponent, double radian) {
        Point dropPoint;

        do {
            dropPoint =
                    new Point(
                            (float) Math.cos(radian * Math.PI) + positionComponent.getPosition().x,
                            (float) Math.sin(radian * Math.PI) + positionComponent.getPosition().y);

            radian += 0.01;
        } while (!Game.currentLevel.getTileAt(dropPoint.toCoordinate()).isAccessible());

        return dropPoint;
    }

    // Used to redraw the whole InventoryUI
    private void redraw() {
        this.forEach((Actor s) -> s.remove());
        buildInventory();
        buildSkillOverview();
        screenPointer = new ScreenImage("hud/selected.png", new Point(-100, -100));
        screenPointer.scaleBy(1.5f);
        add((T) screenPointer);
        listItems();
    }

    // Used to list the items in the hero's inventory. This needs the inventoryComp to be not NULL
    // !!
    private void listItems() {
        if (inventoryComp == null) {
            logger.log(new LogRecord(Level.WARNING, "NO INVENTORY COMPONENT FOUND!"));
            return;
        }
        itemsList = new ArrayList<>();
        List<ItemData> items = inventoryComp.getItems();
        int depth = 0;
        Point positionOnScreen;

        for (int i = 0; i < items.size(); i++) {
            positionOnScreen = new Point(25 + (i * 60), 210);
            ScreenImage slot =
                    new ScreenImage(
                            items.get(i).getInventoryTexture().getNextAnimationTexturePath(),
                            positionOnScreen);

            slot.scaleBy(1.5f);

            add((T) slot);
            itemsList.add(new Node(positionOnScreen, items.get(i), slot));

            if (items.get(i) instanceof Bag) {
                depth++;
                listBag((Bag) items.get(i), depth);
            }
        }

        listUpdated = true;
    }

    // Lists all items of a given bag on the screen.
    private void listBag(Bag bag, int depth) {
        List<ItemData> items = bag.getItems();
        Point positionOnScreen;

        for (int i = 0; i < items.size(); i++) {
            positionOnScreen = new Point(25 + (i * 60), 210 - (depth * 50));
            ScreenImage slot =
                    new ScreenImage(
                            items.get(i).getInventoryTexture().getNextAnimationTexturePath(),
                            positionOnScreen);

            slot.scaleBy(1.5f);

            add((T) slot);
            itemsList.add(new Node(positionOnScreen, items.get(i), bag, slot));
        }
    }

    // Builds the inventory's icons and texts
    private void buildInventory() {
        inventory = new ScreenImage("hud/inventory.png", new Point(10, 10));
        inventory.scaleBy(-1.1f);
        add((T) inventory);

        ScreenImage hero =
                new ScreenImage(
                        "character/knight/idleRight/knight_m_idle_anim_f0.png", new Point(50, 320));
        hero.scaleBy(4f);
        add((T) hero);

        ScreenText classType = new ScreenText("Knight:", new Point(180, 440), 1f);
        classType.setColor(Color.BLACK);
        add((T) classType);

        xpDisplay = new ScreenText("XP", new Point(400, 440), 1f);
        xpDisplay.setColor(Color.BLACK);
        add((T) xpDisplay);

        healthDisplay = new ScreenText("HEALTH", new Point(180, 400), 1f);
        healthDisplay.setColor(Color.BLACK);
        add((T) healthDisplay);

        manaDisplay = new ScreenText("HEALTH", new Point(180, 380), 1f);
        manaDisplay.setColor(Color.BLACK);
        add((T) manaDisplay);

        staminaDisplay = new ScreenText("HEALTH", new Point(180, 360), 1f);
        staminaDisplay.setColor(Color.BLACK);
        add((T) staminaDisplay);

        ScreenText skillText = new ScreenText("Skills:", new Point(180, 320), 1f);
        skillText.setColor(Color.BLACK);
        add((T) skillText);
    }

    // Builds the icons of the four main Skills.
    private void buildSkillOverview() {
        skill1 = new ScreenImage("skills/fireball/down/fireBall_Down1.png", skillSlot1);
        skill1.scaleBy(1.45f);
        add((T) skill1);

        skill2 = new ScreenImage("skills/frostbolt/down/frostBolt_Down1.png", skillSlot2);
        skill2.scaleBy(1.45f);
        add((T) skill2);

        skill3 = new ScreenImage("skills/schwertstich/right/schwert_Right4.png", skillSlot3);
        skill3.scaleBy(1.45f);
        add((T) skill3);

        skill4 = new ScreenImage("skills/dash/dash.png", skillSlot4);
        skill4.scaleBy(1.45f);
        add((T) skill4);
    }

    /*
    A local Node used for the itemLists.
    It contains the following information:
    - the item
    - the items ScreenImage
    - the position of the ScreenImage in the Inventory
    - the item's bag, if it is in one.
     */
    private class Node {

        private Point screenPosition;
        private ItemData item;
        private ScreenImage image;
        private ItemData bag = null;

        private Node(Point screenPosition, ItemData item, ScreenImage image) {
            this.screenPosition = screenPosition;
            this.item = item;
            this.image = image;
        }

        private Node(Point screenPosition, ItemData item, ItemData bag, ScreenImage image) {
            this.screenPosition = screenPosition;
            this.item = item;
            this.image = image;
            this.bag = bag;
        }
    }
}

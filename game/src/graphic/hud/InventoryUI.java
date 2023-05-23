package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.components.ManaComponent;
import ecs.components.StaminaComponent;
import ecs.components.xp.XPComponent;
import ecs.entities.Hero;
import ecs.items.Bag;
import ecs.items.ItemData;
import tools.Point;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class InventoryUI<T extends Actor> extends ScreenController<T> {

    public final Logger logger = Logger.getLogger("InventoryUI Logger");

    private ScreenText healthDisplay, manaDisplay, staminaDisplay, xpDisplay;
    private HealthComponent healthComp;
    private ManaComponent manaComp;
    private StaminaComponent staminaComp;
    private XPComponent xpComp;
    private InventoryComponent inventoryComp;
    private ScreenImage inventory, skill1, skill2, skill3, skill4;
    private boolean visible = false;
    private boolean listUpdated = false;
    private static List<ItemData> items;

    private static Point skillSlot1 = new Point(237, 295);
    private static Point skillSlot2 = new Point(302, 295);
    private static Point skillSlot3 = new Point(365, 285);
    private static Point skillSlot4 = new Point(432, 288);

    public InventoryUI(Hero hero) {
        super(new SpriteBatch());
        assignComponents(hero);
        buildInventory();
        buildSkillOverview();
        hideScreen();
    }

    /**
     * Assigns the needed components from the hero to the local components to be worked with.
     * @param hero The Hero whose stats should be displayed on the UI.
     */
    public void assignComponents(Hero hero) {
        hero.getComponent(HealthComponent.class).ifPresent(component -> {
            logger.log(new LogRecord(Level.INFO, "HealthComponent detected!"));
            healthComp = (HealthComponent) component;
        });
        hero.getComponent(ManaComponent.class).ifPresent(component -> {
            logger.log(new LogRecord(Level.INFO, "ManaComponent detected!"));
            manaComp = (ManaComponent) component;
        });
        hero.getComponent(StaminaComponent.class).ifPresent(component -> {
            logger.log(new LogRecord(Level.INFO, "StaminaComponent detected!"));
            staminaComp = (StaminaComponent) component;
        });
        hero.getComponent(XPComponent.class).ifPresent(component -> {
            logger.log(new LogRecord(Level.INFO, "XPComponent detected!"));
            xpComp = (XPComponent) component;
        });
        hero.getComponent(InventoryComponent.class).ifPresent(component -> {
            logger.log(new LogRecord(Level.INFO, "InventoryComponent detected!"));
            inventoryComp = (InventoryComponent) component;
        });
    }

    /** Makes the screen invisible */
    public void hideScreen() {
        this.forEach((Actor s) -> s.setVisible(false));
        visible = false;
        listUpdated = false;
    }

    /** Makes the screen visible */
    public void showScreen() {
        this.forEach((Actor s) -> s.setVisible(true));
        visible = true;
    }

    /**
     * Updates the UIs information like the hero's healthPoints and more.
     */
    public void updateUI() {
        healthDisplay.setText("HEALTH: " + healthComp);
        manaDisplay.setText("MANA: " + manaComp);
        staminaDisplay.setText("STAMINA: " + staminaComp);
        xpDisplay.setText(String.valueOf(xpComp));

        if (visible && !listUpdated) listItems();
    }

    // Used to list the items in the hero's inventory. This needs the inventoryComp to be not NULL !!
    private void listItems() {
        if (inventoryComp == null) {
            logger.log(new LogRecord(Level.WARNING, "NO INVENTORY COMPONENT FOUND!"));
            return;
        }

        items = inventoryComp.getItems();
        int depth = 0;

        // FIXME: THis does not work

        /*
        NOTE:

        I tried it with actionListener and buttons (as you see implemented now), but it just does not register
        the user clicking the button. THis framework makes me puke. You can undo the commentated lines (139, 172)
        and out comment the following lines that follow with add((T) button). It looks like it is supped to, but as said,
        the buttons do not work and I cannot use ScreenButton, because WTF IS A SKIN?!
         */


        for (int i = 0; i < items.size(); i++) {
            ScreenImage slot = new ScreenImage(
                items.get(i).getInventoryTexture().getNextAnimationTexturePath(),
                new Point(25 + (i*60), 210));

            slot.scaleBy(1.5f);

            ScreenButton button = new ScreenButton(items.get(i).getItemName(),
                new Point(25 + (i * 60), 210),
                new TextButtonListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("I WORK");
                    }
                });

            // add((T) slot);
            add((T) button);

            if (items.get(i) instanceof Bag) {
                depth++;
                listBag((Bag) items.get(i), depth);
            }
        }

        listUpdated = true;
    }

    private void listBag(Bag bag, int depth) {
        List<ItemData> items = bag.getItems();

        for (int i = 0; i < items.size(); i++) {
            ScreenImage slot = new ScreenImage(
                items.get(i).getInventoryTexture().getNextAnimationTexturePath(),
                new Point(25 + (i*60), 210 - (depth*50)));

            slot.scaleBy(1.5f);

            ScreenButton button = new ScreenButton(items.get(i).getItemName(),
                new Point(25 + (i * 60), 210),
                new TextButtonListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("I WORK");
                    }
                });

            // add((T) slot);
            add((T) button);
        }
    }

    // Builds the inventory's icons and texts
    private void buildInventory() {
        inventory = new ScreenImage("hud/inventory.png", new Point(10,10));
        inventory.scaleBy(-1.1f);
        add((T) inventory);

        ScreenImage hero = new ScreenImage("character/knight/idleRight/knight_m_idle_anim_f0.png", new Point(50, 320));
        hero.scaleBy(4f);
        add((T) hero);

        ScreenText classType = new ScreenText("Knight:", new Point(180, 440), 1f);
        classType.setColor(Color.BLACK);
        add((T) classType);

        xpDisplay = new ScreenText("XP", new Point(400, 440), 1f);
        xpDisplay.setColor(Color.BLACK);
        add((T) xpDisplay);

        healthDisplay = new ScreenText("HEALTH", new Point(180,400), 1f);
        healthDisplay.setColor(Color.BLACK);
        add((T) healthDisplay);

        manaDisplay = new ScreenText("HEALTH", new Point(180,380), 1f);
        manaDisplay.setColor(Color.BLACK);
        add((T) manaDisplay);

        staminaDisplay = new ScreenText("HEALTH", new Point(180,360), 1f);
        staminaDisplay.setColor(Color.BLACK);
        add((T) staminaDisplay);

        ScreenText skillText = new ScreenText("Skills:", new Point(180,320), 1f);
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
}

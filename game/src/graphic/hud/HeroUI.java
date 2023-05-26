package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.components.ManaComponent;
import ecs.components.StaminaComponent;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.heros.Hero;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import tools.Constants;
import tools.Point;

public class HeroUI<T extends Actor> extends ScreenController<T> {

    public final Logger logger = Logger.getLogger("HeroUI Logger");

    private ScreenText healthDisplay, manaDisplay, staminaDisplay;
    private HealthComponent healthComp;
    private ManaComponent manaComp;
    private StaminaComponent staminaComp;
    private SkillComponent skillComp;
    private final Color fontColor = Color.ORANGE;
    private ScreenImage skill1, skill2, skill3;

    /**
     * Builds the HeroUI.
     *
     * @param hero Needs the hero to later assign his components to be displayed.
     */
    public HeroUI(Hero hero) {
        super(new SpriteBatch());
        assignComponents(hero);
        buildInfoText();
        buildSkillOverview();
        showScreen();
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
        hero.getComponent(SkillComponent.class)
                .ifPresent(
                        component -> {
                            logger.log(new LogRecord(Level.INFO, "SkillComponent detected!"));
                            skillComp = (SkillComponent) component;
                        });
    }

    /** Updates the UIs information like the hero's healthPoints and more. */
    public void updateUI() {
        healthDisplay.setText("HEALTH: " + healthComp);
        manaDisplay.setText("MANA: " + manaComp);
        staminaDisplay.setText("STAMINA: " + staminaComp);

        // check for each skill if the coolDown still applies. If yes, show the skill, else make it
        // invisible.
        // TODO: For some reason the skillSet is not in order as the skills where added. Find out
        // why! (Skills in random order)
        if (((Skill) skillComp.getSkillSet().toArray()[0]).isOnCoolDown()) { // first skill
            skill1.setVisible(false);
        } else {
            skill1.setVisible(true);
        }
        if (((Skill) skillComp.getSkillSet().toArray()[1]).isOnCoolDown()) { // second skill
            skill2.setVisible(false);
        } else {
            skill2.setVisible(true);
        }
        /*
        if (((Skill) skillComp.getSkillSet().toArray()[2]).isOnCoolDown()) { // third skill
            skill3.setVisible(false);
        } else {
            skill3.setVisible(true);
        }

         */

    }

    /** Makes the screen invisible */
    public void hideScreen() {
        this.forEach((Actor s) -> s.setVisible(false));
    }

    /** Makes the screen visible */
    public void showScreen() {
        this.forEach((Actor s) -> s.setVisible(true));
    }

    // Builds the icons of the four main Skills.
    private void buildSkillOverview() {
        skill1 = new ScreenImage("skills/fireball/down/fireBall_Down1.png", new Point(0, 5));
        skill1.scaleBy(1.1f);
        add((T) skill1);

        skill2 = new ScreenImage("skills/frostbolt/down/frostBolt_Down1.png", new Point(40, 5));
        skill2.scaleBy(1.1f);
        add((T) skill2);

        skill3 = new ScreenImage("skills/schwertstich/right/schwert_Right4.png", new Point(80, 0));
        skill3.scaleBy(1.1f);
        add((T) skill3);
    }

    // Builds the info texts to display health, mana and stamina.
    private void buildInfoText() {
        healthDisplay =
                new ScreenText(
                        "HEALTH:",
                        new Point(5, Constants.WINDOW_HEIGHT - 20),
                        2,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(fontColor)
                                .build());
        add((T) healthDisplay);

        manaDisplay =
                new ScreenText(
                        "MANA:",
                        new Point(
                                (float) Constants.WINDOW_WIDTH / 2 - 50,
                                Constants.WINDOW_HEIGHT - 20),
                        2,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(fontColor)
                                .build());
        add((T) manaDisplay);

        staminaDisplay =
                new ScreenText(
                        "STAMINA:",
                        new Point(Constants.WINDOW_WIDTH - 140, Constants.WINDOW_HEIGHT - 20),
                        2,
                        new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                                .setFontcolor(fontColor)
                                .build());
        add((T) staminaDisplay);
    }
}

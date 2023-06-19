package graphic.hud.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import graphic.hud.ScreenImage;
import graphic.hud.ScreenText;
import java.util.logging.Level;
import java.util.logging.Logger;
import level.tools.LevelSize;
import tools.Point;
import tools.Settings;

/**
 * A UI that allows the user to change the dungeon and visual settings. This class helps outsource
 * elements of the MainMenuUI into its own category.
 *
 * @param <T> An element that can be drawn on the screen.
 */
public class SettingsMenu<T extends Actor> extends ScreenController<T> {

    public final Logger logger = Logger.getLogger("SettingsMenu Logger");
    private final MainMenuUI<T> parent;

    private final Point option1 = new Point(170, 250);
    private final Point option2 = new Point(170, 200);
    private final Point option3 = new Point(170, 150);
    private final Point option4 = new Point(170, 100);

    private boolean isFocused = false;
    private ScreenText setting1, setting2, setting3, setting4;
    private ScreenImage menuArrow;
    private int pointer = 0;

    /** Standard constructor. It builds the settings menu. */
    public SettingsMenu(MainMenuUI<T> parent) {
        super(new SpriteBatch());
        this.parent = parent;

        setupSettings();
        setupMenuArrow();

        hideScreen();
    }

    /** Hides all elements of this menu and tells its parent to be visible again. */
    public void hideScreen() {
        this.forEach((Actor s) -> s.setVisible(false));
        isFocused = false;
        parent.setFocused();
    }

    /**
     * Makes all elements of this menu visible and tells its parent, that it is currently in focus.
     */
    public void showScreen() {
        this.forEach((Actor s) -> s.setVisible(true));
        isFocused = true;
    }

    @Override
    public void update() {
        super.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) this.hideScreen();

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            pointer = pointer - 1 % 4;
            if (pointer < 0) pointer += 4;
            changePointerPos();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            pointer = (pointer + 1) % 4;
            changePointerPos();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (pointer == 0) {
                if (Settings.levelSize == LevelSize.SMALL) Settings.levelSize = LevelSize.LARGE;
                else if (Settings.levelSize == LevelSize.LARGE)
                    Settings.levelSize = LevelSize.MEDIUM;
                else Settings.levelSize = LevelSize.SMALL;
            } else if (pointer == 1) {
                Settings.setMaxMonsterAmount =
                        Settings.setMaxMonsterAmount > Settings.MINIMUM_MONSTER_AMOUNT
                                ? Settings.setMaxMonsterAmount - 1
                                : Settings.MAXIMUM_MONSTER_AMOUNT;
            } else if (pointer == 2) {
                Settings.allowDynamicLighting = !Settings.allowDynamicLighting;
            } else Settings.potatoMode = !Settings.potatoMode;

            updateSettings();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (pointer == 0) {
                if (Settings.levelSize == LevelSize.SMALL) Settings.levelSize = LevelSize.MEDIUM;
                else if (Settings.levelSize == LevelSize.MEDIUM)
                    Settings.levelSize = LevelSize.LARGE;
                else Settings.levelSize = LevelSize.SMALL;
            } else if (pointer == 1) {
                Settings.setMaxMonsterAmount =
                        Settings.setMaxMonsterAmount < Settings.MAXIMUM_MONSTER_AMOUNT
                                ? Settings.setMaxMonsterAmount + 1
                                : Settings.MINIMUM_MONSTER_AMOUNT;
            } else if (pointer == 2) {
                Settings.allowDynamicLighting = !Settings.allowDynamicLighting;
            } else Settings.potatoMode = !Settings.potatoMode;

            updateSettings();
        }
    }

    private void setupMenuArrow() {
        menuArrow = new ScreenImage("hud/arrow.png", option1);
        menuArrow.scaleBy(-1f);
        add((T) menuArrow);
        logger.log(Level.FINE, "menuArrow-ScreenImage loaded.");
    }

    private void setupSettings() {
        setting1 = new ScreenText("LEVEL SIZE: " + Settings.levelSize, new Point(200, 250), 1f);
        setting1.setColor(Color.WHITE);
        setting1.setFontScale(1.5f);
        add((T) setting1);

        setting2 =
                new ScreenText(
                        "MAX MONSTER: " + Settings.setMaxMonsterAmount, new Point(200, 200), 1f);
        setting2.setColor(Color.WHITE);
        setting2.setFontScale(1.5f);
        add((T) setting2);

        setting3 =
                new ScreenText(
                        "ALLOW DYNAMIC LIGHTING: " + Settings.allowDynamicLighting,
                        new Point(200, 150),
                        1f);
        setting3.setColor(Color.WHITE);
        setting3.setFontScale(1.5f);
        add((T) setting3);

        setting4 = new ScreenText("POTATO MODE: " + Settings.potatoMode, new Point(200, 100), 1f);
        setting4.setColor(Color.WHITE);
        setting4.setFontScale(1.5f);
        add((T) setting4);

        logger.log(Level.FINE, "settings initialized.");
    }

    private void changePointerPos() {
        if (pointer == 0) menuArrow.setPosition(option1.x, option1.y);
        else if (pointer == 1) menuArrow.setPosition(option2.x, option2.y);
        else if (pointer == 2) menuArrow.setPosition(option3.x, option3.y);
        else menuArrow.setPosition(option4.x, option4.y);
    }

    private void updateSettings() {
        setting1.setText("LEVEL SIZE: " + Settings.levelSize);
        setting2.setText("MAX MONSTER: " + Settings.setMaxMonsterAmount);
        setting3.setText("ALLOW DYNAMIC LIGHTING: " + Settings.allowDynamicLighting);
        setting4.setText("POTATO MODE: " + Settings.potatoMode);
    }

    // -------------------- GETTER -------------------- //
    public boolean isFocused() {
        return isFocused;
    }
}

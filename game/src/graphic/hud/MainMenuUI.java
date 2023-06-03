package graphic.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.entities.heros.*;
import level.tools.LevelSize;
import starter.Game;
import tools.Constants;
import tools.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The MainMenuUI handles everything that is shown at the start.
 * It confines the player to the bounds set, thus eliminating the
 * possibility of errors or false inputs by the player.
 * <p>
 * This class can be later modified to house logic like loading game states
 * or even having a settings menu for sounds and music.
 *
 * @param <T> An element that can be drawn on the screen.
 * @author Kirill Kuhn
 * @version 2.0
 */
public class MainMenuUI<T extends Actor> extends ScreenController<T> {

    private final Logger logger = Logger.getLogger("MainMenuUI");

    private ScreenImage logo, characterCard, menuArrow;
    private ScreenText newGame, settings, characterDescription, info, chooseCharacter;
    private final List<CharacterNode> characters = new ArrayList<>();
    private int pointer = 0;
    private final Point characterIconPosition = new Point(70, 40);
    private final Point menuOption1 = new Point(235,140);
    private final Point menuOption2 = new Point(190,90);
    private LevelSize levelSize = LevelSize.SMALL;
    private int maxMonster = 10;
    private float fadeCounter = 0;

    /**
     * Creates a main menu. NOTE: ACTORS CAN ONLY BE HIDDEN BY DELETING THE INSTANCE!
     */
    public MainMenuUI() {
        super(new SpriteBatch());
        setupMainMenu();
        setupCharScreen();
    }

    /**
     * Listens for keystrokes and updates the UI accordingly.
     * Should the title screen be invisible, it then will then initialize
     * the character screen.
     * <p>
     * It also handles the logic for changing the character icon and description
     * relative to what the user has selected now. After selection, it gives the
     * Game class the selected character.
     */
    public void updateUI() {
        if (fadeCounter > 0) {
            fadeCounter -= 0.005f;
            info.setColor(255,255,255,fadeCounter);
        }

        if (!logo.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) { // final decision in character screen
            if (characters.get(0).character.isVisible()) {
                Game.setHero(new Knight());
                Game.setHeroType(0);
            } else if (characters.get(1).character.isVisible()) {
                Game.setHero(new Mage());
                Game.setHeroType(1);
            } else if (characters.get(2).character.isVisible()) {
                Game.setHero(new Ranger());
                Game.setHeroType(2);
            } else if (characters.get(3).character.isVisible()) {
                Game.setHero(new Rogue());
                Game.setHeroType(3);
            }
            Game.makeCharacterSet();
            Game.setMaxMonster(maxMonster);
            Game.setLevelSize(levelSize);
        }

        if (logo.isVisible()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) { // change to character screen if title screen exited
                if (menuArrow.getX() == menuOption1.x) { // go to character menu
                    // MAIN MENU ELEMENTS
                    logo.setVisible(false);
                    newGame.setVisible(false);
                    settings.setVisible(false);
                    menuArrow.setVisible(false);

                    // CHARACTER MENU ELEMENTS
                    chooseCharacter.setVisible(true);
                    characterCard.setVisible(true);
                    characterDescription.setVisible(true);
                    characters.get(pointer).character.setVisible(true);
                }
                else if (menuArrow.getX() == menuOption2.x) {
                    toggleSettings();
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                if (menuArrow.getX() == menuOption1.x) menuArrow.setPosition(menuOption2.x, menuOption2.y);
                else menuArrow.setPosition(menuOption1.x, menuOption1.y);
            }
        }

        if (!logo.isVisible()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) { // iterate down in the character screen
                characters.get(pointer).character.setVisible(false);
                pointer++;
                if (pointer >= characters.size()) pointer = 0;
                characters.get(pointer).character.setVisible(true);
                characterDescription.setText(characters.get(pointer).characterDescription);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) { // iterate up in the character screen
                characters.get(pointer).character.setVisible(false);
                pointer--;
                if (pointer < 0) pointer = characters.size() - 1;
                characters.get(pointer).character.setVisible(true);
                characterDescription.setText(characters.get(pointer).characterDescription);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                // MAIN MENU ELEMENTS
                logo.setVisible(true);
                newGame.setVisible(true);
                settings.setVisible(true);
                menuArrow.setVisible(true);

                // CHARACTER MENU ELEMENTS
                chooseCharacter.setVisible(false);
                characterCard.setVisible(false);
                characterDescription.setVisible(false);
                characters.get(pointer).character.setVisible(false);
            }
        }
    }

    private void toggleSettings() {
        if (levelSize == LevelSize.SMALL) {
            levelSize = LevelSize.MEDIUM;
            maxMonster = 15;
            info.setText("LevelSize = MEDIUM (maxMonster: 15)");
            logger.fine("Level size set to medium (maxMonster: 15)");
        } else if (levelSize == LevelSize.MEDIUM) {
            levelSize = LevelSize.LARGE;
            maxMonster = 20;
            info.setText("LevelSize = LARGE (maxMonster: 20) !!! STRONG CPU REQUIRED");
            logger.fine("Level size set to large (maxMonster: 10)");
        } else {
            levelSize = LevelSize.SMALL;
            maxMonster = 10;
            info.setText("LevelSize = SMALL (maxMonster: 10)");
            logger.fine("Level size set to small (maxMonster: 20)");
        }
        fadeCounter = 1;
    }

    /*
    Sets up the mainMenu containing the title screen.
     */
    private void setupMainMenu() {
        ScreenImage background = new ScreenImage(
            "hud/main_background.png",
            new Point(0, 0));
        background.scaleBy(-1.2f);
        add((T) background);
        logger.log(Level.FINE, "Background loaded.");

        logo = new ScreenImage(
            "logo/game_logo.png",
            new Point((float) Constants.WINDOW_WIDTH / 8, Constants.WINDOW_HEIGHT - 140));
        logo.scaleBy(-1.5f);
        add((T) logo);
        logger.log(Level.FINE, "Logo loaded.");

        menuArrow = new ScreenImage(
            "hud/arrow.png",
            menuOption1);
        menuArrow.scaleBy(-1f);
        add((T) menuArrow);
        logger.log(Level.FINE, "menuArrow-ScreenImage loaded.");

        newGame = new ScreenText(
            "NEW GAME",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 60, 140),
            1f
        );
        newGame.setColor(Color.WHITE);
        newGame.setFontScale(1.5f);
        add((T) newGame);
        logger.log(Level.FINE, "NEW_GAME-ScreenText initialized.");

        settings = new ScreenText(
            "CHANGE SETTINGS",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 105, 90),
            1f
        );
        settings.setColor(Color.WHITE);
        settings.setFontScale(1.5f);
        add((T) settings);
        logger.log(Level.FINE, "CHANGE_SETTINGS-ScreenText initialized.");

        info = new ScreenText(
            "SAMPLE",
            new Point(2, 2),
            1f);
        info.setColor(255, 255, 255, fadeCounter);
        add((T) info);
        logger.log(Level.FINE, "info-ScreenText initialized.");
    }

    /*
    Sets up the character screen.
     */
    private void setupCharScreen() {
        chooseCharacter = new ScreenText(
            "Choose a character:",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 100, Constants.WINDOW_HEIGHT - 120),
            1f);
        chooseCharacter.setColor(Color.WHITE);
        chooseCharacter.setFontScale(1.5f);
        chooseCharacter.setVisible(false);
        add((T) chooseCharacter);
        logger.log(Level.FINE, "chooseCharacter-ScreenText initialized.");

        characterCard = new ScreenImage(
            "hud/characterCard.png",
            new Point(0, 0));
        characterCard.scaleBy(-1.2f);
        characterCard.setVisible(false);
        add((T) characterCard);
        logger.log(Level.FINE, "CharacterCard loaded.");

        characters.add(new CharacterNode( // Knight
            new ScreenImage(
                "character/hero/knight/idleRight/knight_m_idle_anim_f0.png",
                characterIconPosition
            ),
            """
                Knight:

                Skills:  Sword
                            Dash

                HP:              130
                STAMINA:    40
                """
        ));
        characters.get(0).character.setVisible(false);
        add((T) characters.get(0).character);
        logger.log(Level.FINE, "Added Knight as character option.");

        characters.add(new CharacterNode( // Mage
            new ScreenImage(
                "character/hero/mage/idleRight/wizzard_m_idle_anim_f0.png",
                characterIconPosition
            ),
            """
                Mage:

                Skills:  Fireball
                            Frostbolt
                            Teleport

                HP:             80
                STAMINA:   15
                MANA:        80
                """
        ));
        characters.get(1).character.setVisible(false);
        add((T) characters.get(1).character);
        logger.log(Level.FINE, "Added Mage as character option.");

        characters.add(new CharacterNode( // Ranger
            new ScreenImage(
                "character/hero/ranger/idleRight/elf_f_idle_anim_f0.png",
                characterIconPosition
            ),
            """
                Ranger:

                Skills:   Arrow
                            Boomerang
                            Dash

                HP:             100
                STAMINA:   60
                """
        ));
        characters.get(2).character.setVisible(false);
        add((T) characters.get(2).character);
        logger.log(Level.FINE, "Added Ranger as character option.");

        characters.add(new CharacterNode( // Rogue
            new ScreenImage(
                "character/hero/rogue/idleRight/lizard_m_idle_anim_f0.png",
                characterIconPosition
            ),
            """
                Rogue:

                Skills:  Sword
                            Invisibility
                            Dash

                HP:             100
                STAMINA:   80
                """
        ));
        characters.get(3).character.setVisible(false);
        add((T) characters.get(3).character);
        logger.log(Level.FINE, "Added Rogue as character option.");

        characterDescription = new ScreenText(
            characters.get(pointer).characterDescription,
            new Point((float) Constants.WINDOW_WIDTH / 2 - 40, 100),
            1f
        );
        characterDescription.setColor(Color.BLACK);
        characterDescription.setFontScale(1.5f);
        characterDescription.setVisible(false);
        add((T) characterDescription);
        logger.log(Level.FINE, "Character Description ScreenText initialized.");

        characters.get(pointer).character.setVisible(false);
    }

    /*
        A private Node containing information of each character that can be picked from.
        It is used in the character screen.
         */
    private record CharacterNode(ScreenImage character, String characterDescription) {
        private CharacterNode(ScreenImage character, String characterDescription) {
            this.character = character;
            this.character.scaleBy(8f);
            this.character.setVisible(false);
            this.characterDescription = characterDescription;
        }
    }
}

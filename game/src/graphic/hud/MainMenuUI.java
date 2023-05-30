package graphic.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.entities.heros.*;
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
 * @version 1.0
 */
public class MainMenuUI<T extends Actor> extends ScreenController<T> {

    private final Logger logger = Logger.getLogger("MainMenuUI");

    private ScreenImage logo, characterCard;
    private ScreenText welcomeText, characterDescription;
    private List<Node> characters = new ArrayList<>();
    private int pointer = 0;
    private final Point characterIconPosition = new Point(70, 40);

    /**
     * Creates a main menu. NOTE: ACTORS CAN ONLY BE HIDDEN BY DELETING THE INSTANCE!
     */
    public MainMenuUI() {
        super(new SpriteBatch());
        setupMainMenu();
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
        if (!logo.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) { // final decision in character screen
            if (characters.get(0).character.isVisible()) {
                Game.setHero(new Knight());
                Game.setHeroType(0);
            }
            else if (characters.get(1).character.isVisible()) {
                Game.setHero(new Mage());
                Game.setHeroType(1);
            }
            else if (characters.get(2).character.isVisible()) {
                Game.setHero(new Ranger());
                Game.setHeroType(2);
            }
            else if (characters.get(3).character.isVisible()) {
                Game.setHero(new Rogue());
                Game.setHeroType(3);
            }
            Game.makeCharacterSet();
        }

        if (logo.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) { // change to character screen if title screen exited
            logo.setVisible(false);
            welcomeText.setVisible(false);
            setupCharScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !logo.isVisible()) { // iterate down in the character screen
            characters.get(pointer).character.setVisible(false);
            pointer++;
            if (pointer >= characters.size()) pointer = 0;
            characters.get(pointer).character.setVisible(true);
            characterDescription.setText(characters.get(pointer).characterDescription);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !logo.isVisible()) { // iterate up in the character screen
            characters.get(pointer).character.setVisible(false);
            pointer--;
            if (pointer < 0) pointer = characters.size() - 1;
            characters.get(pointer).character.setVisible(true);
            characterDescription.setText(characters.get(pointer).characterDescription);
        }
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

        welcomeText = new ScreenText(
            "Press [Enter] to start",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 100, 100),
            1f
        );
        welcomeText.setColor(Color.WHITE);
        welcomeText.setFontScale(1.5f);
        add((T) welcomeText);
        logger.log(Level.FINE, "Welcome-ScreenText initialized.");
    }

    /*
    Sets up the character screen.
     */
    private void setupCharScreen() {
        ScreenText screenText = new ScreenText(
            "Choose a character:",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 100, Constants.WINDOW_HEIGHT - 120),
            1f);
        screenText.setColor(Color.WHITE);
        screenText.setFontScale(1.5f);
        add((T) screenText);
        logger.log(Level.FINE, "ChooseChar-ScreenText initialized.");

        characterCard = new ScreenImage(
            "hud/characterCard.png",
            new Point(0, 0));
        characterCard.scaleBy(-1.2f);
        add((T) characterCard);
        logger.log(Level.FINE, "CharacterCard loaded.");

        characters.add(new Node( // Knight
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
        add((T) characters.get(0).character);
        logger.log(Level.FINE, "Added Knight as character option.");

        characters.add(new Node( // Mage
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
        add((T) characters.get(1).character);
        logger.log(Level.FINE, "Added Mage as character option.");

        characters.add(new Node( // Ranger
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
        add((T) characters.get(2).character);
        logger.log(Level.FINE, "Added Ranger as character option.");

        characters.add(new Node( // Rogue
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
        add((T) characters.get(3).character);
        logger.log(Level.FINE, "Added Rogue as character option.");

        characterDescription = new ScreenText(
            characters.get(pointer).characterDescription,
            new Point((float) Constants.WINDOW_WIDTH / 2 - 40,100),
            1f
        );
        characterDescription.setColor(Color.BLACK);
        characterDescription.setFontScale(1.5f);
        add((T) characterDescription);
        logger.log(Level.FINE, "Character Description ScreenText initialized.");

        characters.get(pointer).character.setVisible(true);
    }

    /*
    A private Node containing information of each character that can be picked from.
    It is used in the character screen.
     */
    private static class Node {
        private final ScreenImage character;
        private final String characterDescription;

        private Node(ScreenImage character, String characterDescription) {
            this.character = character;
            this.character.scaleBy(8f);
            this.character.setVisible(false);
            this.characterDescription = characterDescription;
        }
    }
}

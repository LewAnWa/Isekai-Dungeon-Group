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

public class MainMenuUI<T extends Actor> extends ScreenController<T> {

    private ScreenImage logo, characterCard;
    private ScreenText welcomeText, characterDescription;
    private List<Node> characters = new ArrayList<>();
    private int pointer = 0;
    private final Point characterIconPosition = new Point(70, 40);

    public MainMenuUI() {
        super(new SpriteBatch());
        setupMainMenu();
    }

    public void updateUI() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !logo.isVisible()) {
            if (characters.get(0).character.isVisible()) Game.setHero(new Knight());
            else if (characters.get(1).character.isVisible()) Game.setHero(new Mage());
            else if (characters.get(2).character.isVisible()) Game.setHero(new Ranger());
            else if (characters.get(3).character.isVisible()) Game.setHero(new Rogue());
            Game.makeCharacterSet();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            logo.setVisible(false);
            welcomeText.setVisible(false);
            setupCharScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !logo.isVisible()) {
            characters.get(pointer).character.setVisible(false);
            pointer++;
            if (pointer >= characters.size()) pointer = 0;
            characters.get(pointer).character.setVisible(true);
            characterDescription.setText(characters.get(pointer).characterDescription);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !logo.isVisible()) {
            characters.get(pointer).character.setVisible(false);
            pointer--;
            if (pointer < 0) pointer = characters.size() - 1;
            characters.get(pointer).character.setVisible(true);
            characterDescription.setText(characters.get(pointer).characterDescription);
        }
    }

    private void setupMainMenu() {
        ScreenImage background = new ScreenImage(
            "hud/main_background.png",
            new Point(0, 0));
        background.scaleBy(-1.2f);
        add((T) background);

        logo = new ScreenImage(
            "logo/game_logo.png",
            new Point((float) Constants.WINDOW_WIDTH / 8, Constants.WINDOW_HEIGHT - 140));
        logo.scaleBy(-1.5f);
        add((T) logo);

        welcomeText = new ScreenText(
            "Press [Enter] to start",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 100, 100),
            1f
        );
        welcomeText.setColor(Color.WHITE);
        welcomeText.setFontScale(1.5f);
        add((T) welcomeText);
    }

    private void setupCharScreen() {
        ScreenText screenText = new ScreenText(
            "Choose a character:",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 100, Constants.WINDOW_HEIGHT - 120),
            1f);
        screenText.setColor(Color.WHITE);
        screenText.setFontScale(1.5f);
        add((T) screenText);

        characterCard = new ScreenImage(
            "hud/characterCard.png",
            new Point(0, 0));
        characterCard.scaleBy(-1.2f);
        add((T) characterCard);

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

        characterDescription = new ScreenText(
            characters.get(pointer).characterDescription,
            new Point((float) Constants.WINDOW_WIDTH / 2 - 40,100),
            1f
        );
        characterDescription.setColor(Color.BLACK);
        characterDescription.setFontScale(1.5f);
        add((T) characterDescription);

        characters.get(pointer).character.setVisible(true);
    }

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

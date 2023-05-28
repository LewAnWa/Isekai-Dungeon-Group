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

public class MainMenuUI<T extends Actor> extends ScreenController<T> {

    private ScreenImage logo;
    private ScreenImage charCard1, charCard2, charCard3, charCard4;
    private ScreenText welcomeText;

    public MainMenuUI() {
        super(new SpriteBatch());
        setupMainScreen();
    }

    public void updateUI() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            logo.setVisible(false);
            welcomeText.setVisible(false);
            Game.setHero(new Knight());
            Game.setCharacterSet();
        }

        /*
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && !logo.isVisible()) {
            if (charCard1.isVisible()) Game.setHero(new Knight());
            else if (charCard2.isVisible()) Game.setHero(new Mage());
            else if (charCard2.isVisible()) Game.setHero(new Ranger());
            else if (charCard2.isVisible()) Game.setHero(new Rogue());
        }
         */
    }

    private void setupMainScreen() {
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

    }

    private static class Node {
        private ScreenImage characterCard;
        private ScreenText characterDescription;

        private Node() {

        }
    }
}

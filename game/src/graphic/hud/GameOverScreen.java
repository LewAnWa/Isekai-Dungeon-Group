package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import starter.Game;
import tools.Constants;
import tools.Point;

public class GameOverScreen<T extends Actor> extends ScreenController<T> {

    /**
     * Creates a GameOverScreen with two buttons, one for restart and the other for exiting the game.
     */
    public GameOverScreen(Game game) {
        super(new SpriteBatch());
        ScreenText screenText =
            new ScreenText(
                "YOU DIED",
                new Point(0, 0),
                3,
                new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                    .setFontcolor(Color.RED)
                    .build());
        screenText.setFontScale(3);
        screenText.setPosition(
            (Constants.WINDOW_WIDTH) / 2f - screenText.getWidth(),
            (Constants.WINDOW_HEIGHT) / 1.5f + screenText.getHeight(),
            Align.center | Align.bottom);

        add((T) screenText);
        add((T) buildExitButton());
        add((T) buildRestartButton(game));
        hideScreen();
    }

    /* instantiates the exit button */
    private ScreenButton buildExitButton() {
        return new ScreenButton(
            "EXIT",
            new Point(
                (Constants.WINDOW_WIDTH) / 3f,
                (Constants.WINDOW_HEIGHT) / 1.5f - 40),
            new TextButtonListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.exit(0);
                }
            });
    }

    /* instantiates the restart button */
    private ScreenButton buildRestartButton(Game game) {
        return new ScreenButton(
            "RESTART",
            new Point(
                (Constants.WINDOW_WIDTH) / 3f + 160,
                (Constants.WINDOW_HEIGHT) / 1.5f - 40),
            new TextButtonListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.doRestart();
                }
            });
    }

    /**
     * Makes the GameOverScreen invisible
     */
    public void hideScreen() {
        this.forEach((Actor s) -> s.setVisible(false));
    }

    /**
     * Makes the GameOverScreen visible
     */
    public void showScreen() {
        this.forEach((Actor s) -> s.setVisible(true));
    }
}

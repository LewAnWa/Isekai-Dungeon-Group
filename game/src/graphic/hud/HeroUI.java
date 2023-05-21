package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import controller.ScreenController;
import ecs.components.HealthComponent;
import ecs.components.ManaComponent;
import ecs.components.StaminaComponent;
import ecs.entities.Hero;
import tools.Constants;
import tools.Point;

public class HeroUI<T extends Actor> extends ScreenController<T> {

    private ScreenText healthDisplay, manaDisplay, staminaDisplay;
    private HealthComponent healthComp;
    private ManaComponent manaComp;
    private StaminaComponent staminaComp;
    private final Color fontColor = Color.ORANGE;

    public HeroUI(Hero hero) {
        super(new SpriteBatch());
        assignComponents(hero);
        buildInfoText();
        buildSkillOverview();
        showScreen();
    }

    public void assignComponents(Hero hero) {
        hero.getComponent(HealthComponent.class).ifPresent(component -> {
            healthComp = (HealthComponent) component;
        });
        hero.getComponent(ManaComponent.class).ifPresent(component -> {
            manaComp = (ManaComponent) component;
        });
        hero.getComponent(StaminaComponent.class).ifPresent(component -> {
            staminaComp = (StaminaComponent) component;
        });
    }

    public void updateUI() {
        healthDisplay.setText("HEALTH: " + healthComp);
        manaDisplay.setText("MANA: " + manaComp);
        staminaDisplay.setText("STAMINA: " + staminaComp);
    }

    /** Makes the GameOverScreen invisible */
    public void hideScreen() {
        this.forEach((Actor s) -> s.setVisible(false));
    }

    /** Makes the GameOverScreen visible */
    public void showScreen() {
        this.forEach((Actor s) -> s.setVisible(true));
    }

    private void buildSkillOverview() {
        
    }

    private void buildInfoText() {
        healthDisplay = new ScreenText(
            "HEALTH:",
            new Point(5, Constants.WINDOW_HEIGHT - 20),
            2,
            new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                .setFontcolor(fontColor)
                .build());
        add((T) healthDisplay);

        manaDisplay = new ScreenText(
            "MANA:",
            new Point((float) Constants.WINDOW_WIDTH / 2 - 40, Constants.WINDOW_HEIGHT - 20),
            2,
            new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                .setFontcolor(fontColor)
                .build());
        add((T) manaDisplay);

        staminaDisplay = new ScreenText(
            "STAMINA:",
            new Point(Constants.WINDOW_WIDTH - 130, Constants.WINDOW_HEIGHT - 20),
            2,
            new LabelStyleBuilder(FontBuilder.DEFAULT_FONT)
                .setFontcolor(fontColor)
                .build());
        add((T) staminaDisplay);
    }
}

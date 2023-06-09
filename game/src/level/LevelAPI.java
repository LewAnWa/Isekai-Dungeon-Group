package level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import graphic.Painter;
import graphic.PainterConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.generator.IGenerator;
import level.tools.DesignLabel;
import level.tools.LevelElement;
import level.tools.LevelSize;
import starter.Game;

/** Manages the level. */
public class LevelAPI {
    private final SpriteBatch batch;
    private final Painter painter;
    private final IOnLevelLoader onLevelLoader;
    private IGenerator gen;
    private ILevel currentLevel;
    private final Logger levelAPI_logger = Logger.getLogger(this.getClass().getName());

    /**
     * @param batch Batch on which to draw.
     * @param painter Who draws?
     * @param generator Level generator
     * @param onLevelLoader Object that implements the onLevelLoad method.
     */
    public LevelAPI(
            SpriteBatch batch,
            Painter painter,
            IGenerator generator,
            IOnLevelLoader onLevelLoader) {
        this.gen = generator;
        this.batch = batch;
        this.painter = painter;
        this.onLevelLoader = onLevelLoader;
    }

    /**
     * Load a new Level
     *
     * @param size The size that the level should have
     * @param label The design that the level should have
     */
    public void loadLevel(LevelSize size, DesignLabel label) {
        currentLevel = gen.getLevel(label, size);
        onLevelLoader.onLevelLoad();
        levelAPI_logger.info("A new level was loaded.");
    }

    /**
     * Load a new level with random size and the given desing
     *
     * @param designLabel The design that the level should have
     */
    public void loadLevel(DesignLabel designLabel) {
        loadLevel(LevelSize.randomSize(), designLabel);
    }

    /**
     * Load a new level with the given size and a random desing
     *
     * @param size wanted size of the level
     */
    public void loadLevel(LevelSize size) {
        loadLevel(size, DesignLabel.randomDesign());
    }

    /** Load a new level with random size and random design. */
    public void loadLevel() {
        loadLevel(LevelSize.randomSize(), DesignLabel.randomDesign());
    }

    /** Draw level */
    public void update() {
        drawLevel();
    }

    /**
     * @return The currently loaded level.
     */
    public ILevel getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Calculates a tile's distance and depending on that, the tile will be drawn or not, with its
     * alpha value being tweaked depending on its distance. This creates the effect of the player
     * carrying a torch.
     */
    protected void drawLevel() {
        Map<String, PainterConfig> mapping = new HashMap<>();

        Tile[][] layout = currentLevel.getLayout();
        float playerX, playerY;

        PositionComponent posComp =
                (PositionComponent)
                        Game.getHero()
                                .orElseThrow()
                                .getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () ->
                                                new MissingComponentException(
                                                        "Missing "
                                                                + PositionComponent.class.getName()
                                                                + " which is required for "
                                                                + LevelAPI.class.getName()));

        playerX = posComp.getPosition().x;
        playerY = posComp.getPosition().y;

        // The maximum range the player can see
        float maxRange = 8;

        // Iterate through all tiles in the level
        for (int i = 0; i < layout.length; i++) { // iterates through y-axis
            for (int j = 0; j < layout[0].length; j++) { // iterates through x-axis

                // Check if the tile is within the sight range of the player
                float dx = j - playerX;
                float dy = i - playerY;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance <= maxRange) { // if tile in distance
                    if (layout[i][j].getLevelElement() != LevelElement.SKIP) {
                        String texturePath = layout[i][j].getTexturePath();
                        if (!mapping.containsKey(texturePath)) {
                            mapping.put(texturePath, new PainterConfig(texturePath));
                        }

                        // Calculate the transparency based on the distance
                        float alpha = 1 - (distance / maxRange);

                        painter.draw(
                                layout[i][j].getCoordinate().toPoint(),
                                texturePath,
                                mapping.get(texturePath),
                                alpha);
                    }
                }
            }
        }
    }

    /**
     * @return The currently used Level-Generator
     */
    public IGenerator getGenerator() {
        return gen;
    }

    /**
     * Set the level generator
     *
     * @param generator new level generator
     */
    public void setGenerator(IGenerator generator) {
        gen = generator;
    }

    /**
     * Sets the current level to the given level and calls onLevelLoad().
     *
     * @param level The level to be set.
     */
    public void setLevel(ILevel level) {
        currentLevel = level;
        onLevelLoader.onLevelLoad();
    }
}

package level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ecs.components.LightSourceComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.LightSource;
import graphic.Painter;
import graphic.PainterConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.generator.IGenerator;
import level.tools.DesignLabel;
import level.tools.LevelElement;
import level.tools.LevelSize;
import starter.Game;
import tools.Point;

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

        PositionComponent playerPosComp =
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

        // The maximum range the player can see
        float maxRange = 7;

        // Iterate through all tiles in the level
        for (int y = 0; y < layout.length; y++) {
            for (int x = 0; x < layout[0].length; x++) {

                float alphaFromLightSource = checkIfLit(layout[y][x]);

                float distance = Point.calculateDistance(playerPosComp.getPosition(), layout[y][x].getCoordinateAsPoint());

                if (distance <= maxRange || alphaFromLightSource > 0) { // if tile in distance or is lit by lightSource
                    if (layout[y][x].getLevelElement() != LevelElement.SKIP) {
                        String texturePath = layout[y][x].getTexturePath();
                        if (!mapping.containsKey(texturePath)) {
                            mapping.put(texturePath, new PainterConfig(texturePath));
                        }

                        // Calculate the transparency based on the distance
                        float alpha = 1 - (distance / maxRange);

                        if (alpha < 0) alpha = 0;

                        float finalAlpha = alpha + alphaFromLightSource;
                        if (finalAlpha > 1) finalAlpha = 1;

                        painter.draw(
                                layout[y][x].getCoordinate().toPoint(),
                                texturePath,
                                mapping.get(texturePath),
                                finalAlpha);
                    }
                }
            }
        }
    }

    /*
    Checks if a given tile is lit by any entity acting as a lightSource.
    It sums up all lights hitting a tile and then returns the calculated alpha value.
     */
    private float checkIfLit(Tile tile) {
        // var is used to be able to access an object outside a lambda expression
        var reference = new Object() {
            float alpha = 0;
        };

        Game.getEntities().forEach(entity -> {
            entity.getComponent(LightSourceComponent.class).ifPresent(lsC -> {
                if (reference.alpha >= 1) {
                    return;
                }

                PositionComponent psC = (PositionComponent) entity.getComponent(PositionComponent.class).orElseThrow();

                float distance = Point.calculateDistance(tile.getCoordinateAsPoint(), psC.getPosition());

                if (distance <= ((LightSourceComponent) lsC).getLightRadius()) {
                    reference.alpha += 1 - (distance / ((LightSourceComponent) lsC).getLightRadius());
                }
            });
        });

        if (reference.alpha > 1) return 1;
        return reference.alpha;
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

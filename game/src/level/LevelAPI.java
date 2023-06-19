package level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ecs.components.LightSourceComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import graphic.Painter;
import graphic.PainterConfig;
import java.util.*;
import java.util.logging.Logger;
import level.elements.ILevel;
import level.elements.TileLevel;
import level.elements.tile.Tile;
import level.elements.tile.TileFactory;
import level.elements.tile.WallTile;
import level.generator.IGenerator;
import level.tools.Coordinate;
import level.tools.DesignLabel;
import level.tools.LevelElement;
import level.tools.LevelSize;
import starter.Game;
import tools.Point;
import tools.Settings;

/** Manages the level. */
public class LevelAPI {
    private final SpriteBatch batch;
    private final Painter painter;
    private final IOnLevelLoader onLevelLoader;
    private IGenerator gen;
    private ILevel currentLevel;
    private static List<Tile> drawList;
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

    public void loadBossLevel() {
        currentLevel = new TileLevel(generateBossLevel());
        onLevelLoader.onLevelLoad();
        levelAPI_logger.info("A new level was loaded.");
    }

    /*
    Very ugly way to generate a room.
     */
    private Tile[][] generateBossLevel() {
        LevelElement[][] layout = new LevelElement[15][15];

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (i == 0 || j == 0 || i == layout.length-1 || j == layout.length-1) layout[i][j] = LevelElement.WALL;
                else if (i == layout.length-3 && j == layout.length/2) layout[i][j] = LevelElement.EXIT;
                else layout[i][j] = LevelElement.FLOOR;
            }
        }

        return new TileLevel(layout, DesignLabel.DEFAULT).getLayout();
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
        if (Settings.potatoMode) drawFullLevel();
        else drawLevel();
    }

    /**
     * @return The currently loaded level.
     */
    public ILevel getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Draws the whole level, except for level elements that should be skipped. This is the basic
     * draw style. Useful for low level pcs.
     */
    protected void drawFullLevel() {
        Map<String, PainterConfig> mapping = new HashMap<>();

        Arrays.stream(currentLevel.getLayout())
                .forEach(
                        tiles -> {
                            Arrays.stream(tiles)
                                    .forEach(
                                            tile -> {
                                                if (tile.getLevelElement() != LevelElement.SKIP) {
                                                    String texturePath = tile.getTexturePath();
                                                    if (!mapping.containsKey(texturePath)) {
                                                        mapping.put(
                                                                texturePath,
                                                                new PainterConfig(texturePath));
                                                    }

                                                    painter.draw(
                                                            tile.getCoordinateAsPoint(),
                                                            texturePath,
                                                            mapping.get(texturePath));
                                                }
                                            });
                        });
    }

    /**
     * Draws the current level via ray casting. Every tile element that gets hit by the ray will be
     * saved in a list. Then it calculates the distance and draws the tile.
     */
    protected void drawLevel() {
        Map<String, PainterConfig> mapping = new HashMap<>();
        drawList = new ArrayList<>();

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

        castRays(playerPosComp, drawList);

        drawList.forEach(
                tile -> {
                    float distance =
                            Point.calculateDistance(
                                    playerPosComp.getPosition(), tile.getCoordinateAsPoint());

                    float alpha = 1 - (distance / Settings.PLAYER_LIGHT_RANGE);
                    float alphaFromOtherSource =
                            Settings.allowDynamicLighting ? checkIfLit(tile) : 0;
                    float finalAlpha = alpha + alphaFromOtherSource;

                    if (finalAlpha < 0) finalAlpha = 0;
                    if (finalAlpha > 1) finalAlpha = 1;

                    String texturePath = tile.getTexturePath();
                    if (!mapping.containsKey(texturePath)) {
                        mapping.put(texturePath, new PainterConfig(texturePath));
                    }

                    painter.draw(
                            tile.getCoordinateAsPoint(),
                            texturePath,
                            mapping.get(texturePath),
                            finalAlpha);
                });
    }

    /*
    Handles the ray casting. It calculates the radiant and based on that its offset, that the ray needs to traverse and
    gives it to the castRay()-method. It always includes the tile that the player is standing on.
     */
    private void castRays(PositionComponent position, List<Tile> drawList) {
        drawList.add(currentLevel.getTileAt(position.getPosition().toCoordinate()));

        for (int angle = 0; angle < 360; angle++) {
            float angleInRadians = (float) Math.toRadians(angle);
            float dirX = (float) Math.cos(angleInRadians);
            float dirY = (float) Math.sin(angleInRadians);

            castRay(position.getPosition(), dirX, dirY, drawList);
        }
    }

    /*
    Casts the actual ray. It takes small steps into the given x- and y-direction
    and adds the hit tile to the drawList if it does not yet contain the tile.
    It stops the process, when the ray has reached a wall, a skip tile or when
    a given tile position is out of bounds.
     */
    private void castRay(Point origin, float dirX, float dirY, List<Tile> drawList) {
        while (true) {
            Coordinate currentPos = new Point(origin.x + dirX, origin.y + dirY).toCoordinate();
            Tile currentTile = currentLevel.getTileAt(currentPos);

            if (currentTile == null || currentTile.getLevelElement() == LevelElement.SKIP) return;

            if (currentTile.getLevelElement() == LevelElement.WALL) {
                if (!drawList.contains(currentTile)) drawList.add(currentTile);
                return;
            }

            if (!drawList.contains(currentTile)) drawList.add(currentTile);

            dirX += dirX / 6;
            dirY += dirY / 6;
        }
    }

    /*
    Checks if a given tile is lit by any entity acting as a lightSource.
    It sums up all lights hitting a tile and then returns the calculated alpha value.
     */
    private float checkIfLit(Tile tile) {
        // var is used to be able to access an object outside a lambda expression
        var reference =
                new Object() {
                    float alpha = 0;
                };

        Game.getEntities()
                .forEach(
                        entity -> {
                            entity.getComponent(LightSourceComponent.class)
                                    .ifPresent(
                                            lsC -> {
                                                if (reference.alpha >= 1) {
                                                    return;
                                                }

                                                PositionComponent psC =
                                                        (PositionComponent)
                                                                entity.getComponent(
                                                                                PositionComponent
                                                                                        .class)
                                                                        .orElseThrow();

                                                float distance =
                                                        Point.calculateDistance(
                                                                tile.getCoordinateAsPoint(),
                                                                psC.getPosition());

                                                if (distance
                                                        <= ((LightSourceComponent) lsC)
                                                                .getLightRadius()) {
                                                    reference.alpha +=
                                                            1
                                                                    - (distance
                                                                            / ((LightSourceComponent)
                                                                                            lsC)
                                                                                    .getLightRadius());
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

    public static List<Tile> getDrawList() {
        return drawList;
    }
}

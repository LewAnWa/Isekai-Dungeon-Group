package starter;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static logging.LoggerConfig.initBaseLogger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import configuration.Configuration;
import configuration.KeyboardConfig;
import controller.AbstractController;
import controller.SystemController;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.skill.InvisibilitySkill;
import ecs.components.xp.XPComponent;
import ecs.entities.Chest;
import ecs.entities.Entity;
import ecs.entities.heros.*;
import ecs.entities.monsters.Boss;
import ecs.entities.monsters.MonsterFactory;
import ecs.entities.traps.TrapFactory;
import ecs.systems.*;
import graphic.DungeonCamera;
import graphic.Painter;
import graphic.hud.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import level.IOnLevelLoader;
import level.LevelAPI;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.generator.IGenerator;
import level.generator.postGeneration.WallGenerator;
import level.generator.randomwalk.RandomWalkGenerator;
import level.tools.DesignLabel;
import level.tools.LevelSize;
import tools.Constants;
import tools.Point;

/**
 * The heart of the framework. From here all strings are pulled.
 */
public class Game extends ScreenAdapter implements IOnLevelLoader {

    private DesignLabel LEVELDESIGN = DesignLabel.LUSH;
    private static LevelSize levelSize = LevelSize.SMALL;
    private static int maxMonster = 20;
    private int floor = 1;

    /**
     * The batch is necessary to draw ALL the stuff. Every object that uses draw need to know the
     * batch.
     */
    protected SpriteBatch batch;

    /**
     * Contains all Controller of the Dungeon
     */
    protected List<AbstractController<?>> controller;

    public static DungeonCamera camera;
    /**
     * Draws objects
     */
    protected Painter painter;

    protected LevelAPI levelAPI;
    /**
     * Generates the level
     */
    protected IGenerator generator;

    private boolean doSetup = true;
    private static boolean characterSet = false;
    private static boolean paused = false;

    /**
     * All entities that are currently active in the dungeon
     */
    private static final Set<Entity> entities = new HashSet<>();
    /**
     * All entities to be removed from the dungeon in the next frame
     */
    private static final Set<Entity> entitiesToRemove = new HashSet<>();
    /**
     * All entities to be added from the dungeon in the next frame
     */
    private static final Set<Entity> entitiesToAdd = new HashSet<>();

    /**
     * List of all Systems in the ECS
     */
    public static SystemController systems;

    public static ILevel currentLevel;
    private static PauseMenu<Actor> pauseMenu;
    private static MainMenuUI<Actor> mainMenuUI;
    private static GameOverScreen<Actor> gameOverScreen;
    private static HeroUI<Actor> heroUI;
    private static InventoryUI<Actor> inventoryUI;
    private static Entity hero;
    private static CharacterType heroType;
    private static Entity[] monsters;
    private static Entity chest;
    private static Entity[] traps;
    private Logger gameLogger;

    public static void main(String[] args) {
        // start the game
        try {
            Configuration.loadAndGetConfiguration("dungeon_config.json", KeyboardConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DesktopLauncher.run(new Game());
    }

    /**
     * Main game loop. Redraws the dungeon and calls the own implementation (beginFrame, endFrame
     * and onLevelLoad).
     *
     * @param delta Time since last loop.
     */
    @Override
    public void render(float delta) {
        if (!characterSet) setup();
        else {
            batch.setProjectionMatrix(camera.combined);
            frame();
            clearScreen();
            levelAPI.update();
            controller.forEach(AbstractController::update);
            camera.update();

            heroUI.updateUI();
            inventoryUI.updateUI();
        }
    }

    /**
     * Called once at the beginning of the game.
     */
    protected void setup() {
        if (doSetup) { // build up the game
            doSetup = false;
            controller = new ArrayList<>();
            setupCameras();
            painter = new Painter(batch, camera);
            generator = new RandomWalkGenerator();
            levelAPI = new LevelAPI(batch, painter, generator, this);
            initBaseLogger();
            gameLogger = Logger.getLogger(this.getClass().getName());
            systems = new SystemController();
            controller.add(systems);
            pauseMenu = new PauseMenu<>();
            controller.add(pauseMenu);
            mainMenuUI = new MainMenuUI<>();
            controller.add(mainMenuUI);
        }
        mainMenuUI.update();
        mainMenuUI.updateUI();
        if (characterSet) { // only do this if character is set

            controller.remove(mainMenuUI);
            mainMenuUI = null;

            heroUI = new HeroUI<>((Hero) hero);
            controller.add(heroUI);
            inventoryUI = new InventoryUI<>((Hero) hero);
            controller.add(inventoryUI);
            gameOverScreen = new GameOverScreen<>(this);
            controller.add(gameOverScreen);

            levelAPI =
                new LevelAPI(
                    batch, painter, new WallGenerator(new RandomWalkGenerator()), this);
            levelAPI.loadLevel(levelSize, LEVELDESIGN);
            createSystems();
        }
    }

    /**
     * Resets the game by overwriting the old hero with a new one, hiding the death screen and then
     * loading a new level.
     */
    public void doRestart() {
        switch (heroType) {
            default -> hero = new Knight();
            case MAGE -> hero = new Mage();
            case RANGER -> hero = new Ranger();
            case ROGUE -> hero = new Rogue();
        }

        controller.remove(heroUI);
        heroUI = new HeroUI<>((Hero) hero);
        controller.add(heroUI);
        controller.remove(inventoryUI);
        inventoryUI = new InventoryUI<>((Hero) hero);
        controller.add(inventoryUI);

        gameOverScreen.hideScreen();
        controller.remove(gameOverScreen);
        gameOverScreen = new GameOverScreen<>(this);
        controller.add(gameOverScreen);

        floor = 1;
        levelAPI.loadLevel(levelSize, LEVELDESIGN);
    }

    /**
     * Generates an array of Monsters
     */
    protected void generateMonsters() {
        int monsterAmount = Math.min(currentLevel.getFloorTiles().size() / 20, maxMonster);
        monsters = new Entity[monsterAmount];

        hero.getComponent(XPComponent.class)
            .ifPresent(
                component -> {
                    XPComponent comp = (XPComponent) component;

                    hero.getComponent(PositionComponent.class)
                        .ifPresent(
                            component1 -> {
                                PositionComponent posComp =
                                    (PositionComponent) component1;

                                for (int i = 0; i < monsters.length; i++) {
                                    monsters[i] =
                                        MonsterFactory.generateMonster(
                                            (int) comp.getCurrentLevel(),
                                            posComp.getPosition(),
                                            currentLevel);
                                }
                            });
                });
    }

    /**
     * spawns a chest in the Dungeon
     */
    protected void spawnChest() {
        Chest.spawnChest();
    }

    /**
     * spawns a Mimic Enemy in the Dungeon
     */
    public static void spawnMimicEnemy() {
        hero.getComponent(XPComponent.class)
            .ifPresent(
                component -> {
                    XPComponent heroComp = (XPComponent) component;

                    chest.getComponent(PositionComponent.class)
                        .ifPresent(
                            component1 -> {
                                PositionComponent chestComp =
                                    (PositionComponent) component1;

                                MonsterFactory.spawnMimic(
                                    (int) heroComp.getCurrentLevel(),
                                    chestComp.getPosition(),
                                    currentLevel);
                            });
                });
    }

    public static void spawnNecromancer(){
        hero.getComponent(XPComponent.class)
            .ifPresent(
                component -> {
                    XPComponent comp = (XPComponent) component;

                    hero.getComponent(PositionComponent.class)
                        .ifPresent(
                            component1 -> {
                                PositionComponent posComp =
                                    (PositionComponent) component1;
                                MonsterFactory.spawnNecromancer((int) comp.getCurrentLevel(),
                                    posComp.getPosition(),
                                    currentLevel);
                            });
                });
    }


    /**
     * Generates an arrays of traps depending on the current level size.
     */
    protected void generateTraps() {
        traps = new Entity[currentLevel.getFloorTiles().size() / 30];

        hero.getComponent(XPComponent.class)
            .ifPresent(
                component -> {
                    XPComponent comp = (XPComponent) component;

                    hero.getComponent(PositionComponent.class)
                        .ifPresent(
                            component1 -> {
                                PositionComponent posComp =
                                    (PositionComponent) component1;

                                for (int i = 0; i < traps.length; i++) {
                                    traps[i] =
                                        TrapFactory.generateTraps(
                                            (int) comp.getCurrentLevel(),
                                            posComp.getPosition(),
                                            currentLevel);
                                }
                            });
                });
    }

    protected void generateBoss() {
        hero.getComponent(XPComponent.class)
            .ifPresent(
                component -> {
                    XPComponent comp = (XPComponent) component;

                    hero.getComponent(PositionComponent.class)
                        .ifPresent(
                            component1 -> {
                                PositionComponent posComp =
                                    (PositionComponent) component1;
                                MonsterFactory.spawnBoss((int) comp.getCurrentLevel(),
                                    posComp.getPosition(),
                                    currentLevel);
                            });
                });
    }

    /**
     * Called at the beginning of each frame. Before the controllers call <code>update</code>.
     */
    protected void frame() {
        setCameraFocus();
        manageEntitiesSets();
        getHero().ifPresent(this::loadNextLevelIfEntityIsOnEndTile);
        if (heroType == CharacterType.ROGUE) InvisibilitySkill.applyInvisibilityCost(hero);
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) togglePause();
        if (Gdx.input.isKeyJustPressed(KeyboardConfig.INVENTORY_OPEN.get())) toggleInventory();
    }

    private void toggleInventory() {
        paused = !paused;
        if (systems != null) {
            systems.forEach(ECS_System::toggleRun);
        }
        if (inventoryUI != null) {
            if (paused) {
                inventoryUI.showScreen();
                pauseMenu.hideMenu();
            } else {
                inventoryUI.hideScreen();
                pauseMenu.hideMenu();
            }
        }
    }

    @Override
    public void onLevelLoad() {
        currentLevel = levelAPI.getCurrentLevel();
        entities.clear();
        getHero().ifPresent(this::placeOnLevelStart);

        if (floor % Constants.BOSS_ON_FLOORS == 0) {
            generateBoss();
            return;
        }

        generateMonsters();
        spawnChest();
        generateTraps();
    }

    private void manageEntitiesSets() {
        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        for (Entity entity : entitiesToRemove) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was deleted.");
        }
        for (Entity entity : entitiesToAdd) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was added.");
        }
        entitiesToRemove.clear();
        entitiesToAdd.clear();
    }

    private void setCameraFocus() {
        if (getHero().isPresent()) {
            PositionComponent pc =
                (PositionComponent)
                    getHero()
                        .get()
                        .getComponent(PositionComponent.class)
                        .orElseThrow(
                            () ->
                                new MissingComponentException(
                                    "PositionComponent"));
            camera.setFocusPoint(pc.getPosition());

        } else camera.setFocusPoint(new Point(0, 0));
    }

    private void loadNextLevelIfEntityIsOnEndTile(Entity hero) {
        if (!isOnEndTile(hero)) return;
        if (getEntities().stream().anyMatch(entity -> entity instanceof Boss)) return;

        if (floor == 6) LEVELDESIGN = DesignLabel.DEFAULT;

        if (floor % Constants.BOSS_ON_FLOORS == 0) {
            levelAPI.loadBossLevel();
            floor++;

            return;
        }

        levelAPI.loadLevel(levelSize, LEVELDESIGN);
        floor++;
    }

    private boolean isOnEndTile(Entity entity) {
        PositionComponent pc =
            (PositionComponent)
                entity.getComponent(PositionComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("PositionComponent"));
        Tile currentTile = currentLevel.getTileAt(pc.getPosition().toCoordinate());
        return currentTile.equals(currentLevel.getEndTile());
    }

    private void placeOnLevelStart(Entity hero) {
        entities.add(hero);
        PositionComponent pc =
            (PositionComponent)
                hero.getComponent(PositionComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("PositionComponent"));

        if (floor % Constants.BOSS_ON_FLOORS == 0) {
            pc.setPosition(new Point(7, 1));
            return;
        }

        pc.setPosition(currentLevel.getStartTile().getCoordinate().toPoint());
    }

    /**
     * Toggle between pause and run
     */
    public static void togglePause() {
        paused = !paused;
        if (systems != null) {
            systems.forEach(ECS_System::toggleRun);
        }
        if (pauseMenu != null) {
            if (paused) pauseMenu.showMenu();
            else {
                pauseMenu.hideMenu();
                inventoryUI.hideScreen();
            }
        }
    }

    public static void showGameOverScreen() {
        gameOverScreen.showScreen();
    }

    /**
     * Given entity will be added to the game in the next frame
     *
     * @param entity will be added to the game next frame
     */
    public static void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }

    /**
     * Given entity will be removed from the game in the next frame
     *
     * @param entity will be removed from the game next frame
     */
    public static void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    /**
     * @return Set with all entities currently in game
     */
    public static Set<Entity> getEntities() {
        return entities;
    }

    /**
     * @return Set with all entities that will be added to the game next frame
     */
    public static Set<Entity> getEntitiesToAdd() {
        return entitiesToAdd;
    }

    /**
     * @return Set with all entities that will be removed from the game next frame
     */
    public static Set<Entity> getEntitiesToRemove() {
        return entitiesToRemove;
    }

    /**
     * @return the player character, can be null if not initialized
     */
    public static Optional<Entity> getHero() {
        return Optional.ofNullable(hero);
    }

    /**
     * set the reference of the playable character careful: old hero will not be removed from the
     * game
     *
     * @param hero new reference of hero
     */
    public static void setHero(Entity hero) {
        Game.hero = hero;
    }

    /**
     * set the reference of the chest
     *
     * @param chest new reference of chest
     */
    public static void setChest(Entity chest) {
        Game.chest = chest;
    }

    /**
     * Sets the heroType, which will determine as what characterClass the player will respawn, when
     * restarting the game from the gameOverScreen.
     *
     * @param characterType The type of character the player chose to play as.
     */
    public static void setHeroType(CharacterType characterType) {
        Game.heroType = characterType;
    }

    public static void makeCharacterSet() {
        characterSet = true;
    }

    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void setupCameras() {
        camera = new DungeonCamera(null, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.zoom = Constants.DEFAULT_ZOOM_FACTOR;

        // See also:
        // https://stackoverflow.com/questions/52011592/libgdx-set-ortho-camera
    }

    private void createSystems() {
        new VelocitySystem();
        new DrawSystem(painter);
        new PlayerSystem();
        new AISystem();
        new CollisionSystem();
        new HealthSystem();
        new XPSystem();
        new SkillSystem();
        new ProjectileSystem();
    }

    public static void setLevelSize(LevelSize levelSize) {
        Game.levelSize = levelSize;
    }

    public static void setMaxMonster(int maxMonster) {
        Game.maxMonster = maxMonster;
    }
}

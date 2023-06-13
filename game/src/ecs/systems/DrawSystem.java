package ecs.systems;

import ecs.components.AnimationComponent;
import ecs.components.LightSourceComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.heros.Rogue;
import ecs.entities.traps.Warhead;
import graphic.Animation;
import graphic.Painter;
import graphic.PainterConfig;

import java.util.HashMap;
import java.util.Map;

import starter.Game;
import tools.Point;
import tools.Settings;

/** used to draw entities */
public class DrawSystem extends ECS_System {

    private Painter painter;
    private Map<String, PainterConfig> configs;

    private record DSData(Entity e, AnimationComponent ac, PositionComponent pc) {}

    /**
     * @param painter PM-Dungeon painter to draw
     */
    public DrawSystem(Painter painter) {
        super();
        this.painter = painter;
        configs = new HashMap<>();
    }

    /** draw entities at their position */
    public void update() {
        Game.getEntities().stream()
                .flatMap(e -> e.getComponent(AnimationComponent.class).stream())
                .map(ac -> buildDataObject((AnimationComponent) ac))
                .forEach(this::draw);
    }

    private void draw(DSData dsd) {

        final Animation animation = dsd.ac.getCurrentAnimation();
        String currentAnimationTexture = animation.getNextAnimationTexturePath();

        PositionComponent heroPositionComp =
                (PositionComponent)
                        Game.getHero()
                                .orElseThrow()
                                .getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () ->
                                                new MissingComponentException(
                                                        "Missing "
                                                                + PositionComponent.class.getName()
                                                                + " of Hero, which is required for "
                                                                + DrawSystem.class.getName()));

        float alphaFromLightSource = Settings.allowDynamicLighting ? checkIfLit(dsd) : 0;

        float distance = Point.calculateDistance(dsd.pc().getPosition(), heroPositionComp.getPosition());

        if (!configs.containsKey(currentAnimationTexture)) {
            configs.put(currentAnimationTexture, new PainterConfig(currentAnimationTexture));
        }

        // if entity is in range
        if (distance <= Settings.PLAYER_LIGHT_RANGE || alphaFromLightSource > 0) {
            float alpha = 1 - (distance / Settings.PLAYER_LIGHT_RANGE);

            // for rogue going invisible
            if (!dsd.e.isVisible() && dsd.e instanceof Rogue) {
                painter.draw(
                    dsd.pc().getPosition(),
                    currentAnimationTexture,
                    configs.get(currentAnimationTexture),
                    0.4f);
                return;
            }

            // for invisible entities
            if (!dsd.e.isVisible()) {
                painter.draw(
                        dsd.pc().getPosition(),
                        currentAnimationTexture,
                        configs.get(currentAnimationTexture),
                        0);
                return;
            }

            // normal draw for other entities
            float finalAlpha = alpha + alphaFromLightSource + 0.1f;
            if (finalAlpha > 1) finalAlpha = 1;
            painter.draw(
                    dsd.pc.getPosition(),
                    currentAnimationTexture,
                    configs.get(currentAnimationTexture),
                    finalAlpha);
        }

        // FIXME: THIS SOLUTION IS FOR ANIMATIONS REPEATING EVEN THOUGH THEY ARE SET NOT TO!
        // This solution is hardcoded. Must be changed, when new set of Textures are added
        if (dsd.e instanceof Warhead && animation.getCurrentFrameIndex() == 8)
            ((Warhead) dsd.e).setIdleAnimation();
    }

    /*
    Checks if a given entity is lit by any entity acting as a lightSource.
    It sums up all lights hitting a tile and then returns the calculated alpha value.
     */
    private float checkIfLit(DSData dsd) {
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

                float distance = Point.calculateDistance(dsd.pc().getPosition(), psC.getPosition());

                if (distance <= ((LightSourceComponent) lsC).getLightRadius()) {
                    reference.alpha += 1 - (distance / ((LightSourceComponent) lsC).getLightRadius());
                }
            });
        });

        if (reference.alpha > 1) return 1;
        return reference.alpha;
    }

    private DSData buildDataObject(AnimationComponent ac) {
        Entity e = ac.getEntity();

        PositionComponent pc =
                (PositionComponent)
                        e.getComponent(PositionComponent.class).orElseThrow(DrawSystem::missingPC);

        return new DSData(e, ac, pc);
    }

    @Override
    public void toggleRun() {
        // DrawSystem cant pause
        run = true;
    }

    private static MissingComponentException missingPC() {
        return new MissingComponentException("PositionComponent");
    }
}

package ecs.systems;

import ecs.components.AnimationComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.traps.Trap;
import ecs.entities.traps.Warhead;
import graphic.Animation;
import graphic.Painter;
import graphic.PainterConfig;

import java.util.HashMap;
import java.util.Map;

import starter.Game;

/**
 * used to draw entities
 */
public class DrawSystem extends ECS_System {

    private Painter painter;
    private Map<String, PainterConfig> configs;

    private record DSData(Entity e, AnimationComponent ac, PositionComponent pc) {
    }

    /**
     * @param painter PM-Dungeon painter to draw
     */
    public DrawSystem(Painter painter) {
        super();
        this.painter = painter;
        configs = new HashMap<>();
    }

    /**
     * draw entities at their position
     */
    public void update() {
        Game.getEntities().stream()
            .flatMap(e -> e.getComponent(AnimationComponent.class).stream())
            .map(ac -> buildDataObject((AnimationComponent) ac))
            .forEach(this::draw);
    }

    private void draw(DSData dsd) {
        final Animation animation = dsd.ac.getCurrentAnimation();
        String currentAnimationTexture = animation.getNextAnimationTexturePath();

        if (!configs.containsKey(currentAnimationTexture)) {
            configs.put(currentAnimationTexture, new PainterConfig(currentAnimationTexture));
        }
        if (!dsd.e.isVisible()) { // draw the sprite slightly invisible
            painter.draw(
                dsd.pc().getPosition(),
                currentAnimationTexture,
                configs.get(currentAnimationTexture),
                0.4f
            );
            return;
        }
        painter.draw( // draw the sprite fully visible
            dsd.pc.getPosition(),
            currentAnimationTexture,
            configs.get(currentAnimationTexture));

        // FIXME: THIS SOLUTION IS FOR ANIMATIONS REPEATING EVEN THOUGH THEY ARE SET NOT TO!
        // This solution is hardcoded. Must be changed, when new set of Textures are added
        if (dsd.e instanceof Warhead && animation.getCurrentFrameIndex() == 8) ((Warhead) dsd.e).setIdleAnimation();



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

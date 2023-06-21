package ecs.components.ai.transition;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.ai.AITools;
import ecs.components.ai.idle.WalkToHero;
import ecs.entities.Entity;
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

/**
 * transition used by the boss monster in phase 1 --> moving directly to the hero within a certain
 * range
 */
public class WalkToHeroTransition implements ITransition {

    private GraphPath<Tile> path;
    private Point currentPosition;
    private Point heroPosition;
    private final float range;

    /** constructor for the WalkToHeroTransition */
    public WalkToHeroTransition(float range) {
        this.range = range;
    }

    @Override
    public boolean isInFightMode(Entity entity) {
        currentPosition =
                ((PositionComponent)
                                entity.getComponent(PositionComponent.class)
                                        .orElseThrow(
                                                () ->
                                                        new MissingComponentException(
                                                                "Missing "
                                                                        + PositionComponent.class
                                                                                .getName()
                                                                        + " which is required for the "
                                                                        + WalkToHero.class
                                                                                .getName())))
                        .getPosition();

        Game.getHero()
                .flatMap(hero -> hero.getComponent(PositionComponent.class))
                .ifPresent(
                        component -> {
                            heroPosition = ((PositionComponent) component).getPosition();
                        });

        // already moves to the hero at range of 4
        if (heroPosition != null && AITools.playerInRange(entity, 4f)) {
            path = AITools.calculatePath(currentPosition, heroPosition);
            AITools.move(entity, path);
        }
        // returns the original range with declares when to switch to meeleAI
        return AITools.playerInRange(entity, range);
    }
}

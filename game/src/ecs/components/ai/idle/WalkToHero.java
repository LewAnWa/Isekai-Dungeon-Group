package ecs.components.ai.idle;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.ai.AITools;
import ecs.entities.Entity;
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

/**
 * The entity always knows where the hero is. It finds a path to the hero and moves to the position.
 */
public class WalkToHero implements IIdleAI {
    private GraphPath<Tile> path;
    private Point currentPosition;
    private Point heroPosition;

    @Override
    public void idle(Entity entity) {
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

        if (heroPosition != null) {
            path = AITools.calculatePath(currentPosition, heroPosition);
            AITools.move(entity, path);
        }
    }
}

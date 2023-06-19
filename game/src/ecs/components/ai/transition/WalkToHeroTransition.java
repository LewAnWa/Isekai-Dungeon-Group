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

public class WalkToHeroTransition implements ITransition{

    private GraphPath<Tile> path;
    private Point currentPosition;
    private Point heroPosition;
    private final float range;

    public WalkToHeroTransition(float range) {
        this.range = range;
    }

    @Override
    public boolean isInFightMode(Entity entity) {
        currentPosition = ((PositionComponent) entity.getComponent(PositionComponent.class).orElseThrow(
            () -> new MissingComponentException(
                "Missing "
                    + PositionComponent.class.getName()
                    + " which is required for the "
                    + WalkToHero.class.getName())
        )).getPosition();

        Game.getHero().flatMap(hero -> hero.getComponent(PositionComponent.class)).ifPresent(component -> {
            heroPosition = ((PositionComponent) component).getPosition();
        });

        if (heroPosition != null && AITools.playerInRange(entity, 4f)) {
            path = AITools.calculatePath(currentPosition, heroPosition);
            AITools.move(entity, path);
        }
        return AITools.playerInRange(entity,range);
    }
}

package ecs.components.ai.idle;

import ecs.components.PositionComponent;
import ecs.entities.Entity;
import starter.Game;
import tools.Constants;
import tools.Point;

/** idle ai used by the boss monster in phase 2 --> jumps directly to the heros position */
public class JumpAI implements IIdleAI {

    private Point heroPosition;
    private final int delay = Constants.FRAME_RATE;
    private int timeSinceLastUpdate = 0;

    @Override
    public void idle(Entity entity) {

        Game.getHero()
                .flatMap(hero -> hero.getComponent(PositionComponent.class))
                .ifPresent(
                        component -> {
                            heroPosition = ((PositionComponent) component).getPosition();
                        });
        if (timeSinceLastUpdate >= delay) {
            entity.getComponent(PositionComponent.class)
                    .ifPresent(
                            pSComp -> {
                                PositionComponent pSC = (PositionComponent) pSComp;
                                pSC.setPosition(heroPosition);
                            });
            timeSinceLastUpdate = -1;
        }
        timeSinceLastUpdate++;
    }
}

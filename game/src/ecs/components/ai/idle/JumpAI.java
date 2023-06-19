package ecs.components.ai.idle;

import ecs.components.PositionComponent;
import ecs.components.skill.Skill;
import ecs.entities.Entity;
import starter.Game;
import tools.Constants;
import tools.Point;

public class JumpAI implements IIdleAI {

    private Point heroPosition;
    private final int delay = Constants.FRAME_RATE;
    private int timeSinceLastUpdate = 0;
    private Skill fireball;
    private int fbCounter = 0;
    private boolean wait = true;

    public JumpAI(Skill fireball) {
        this.fireball = fireball;
    }


    @Override
    public void idle(Entity entity) {

        while(wait){
            fireball.execute(entity);
        }

        if (!wait) {
            Game.getHero().flatMap(hero -> hero.getComponent(PositionComponent.class)).ifPresent(component -> {
                heroPosition = ((PositionComponent) component).getPosition();
            });

            if (timeSinceLastUpdate >= delay) {
                entity.getComponent(PositionComponent.class)
                    .ifPresent(pSComp -> {
                        PositionComponent pSC = (PositionComponent) pSComp;
                        pSC.setPosition(heroPosition);
                    });
                timeSinceLastUpdate = -1;
            }
            timeSinceLastUpdate++;
        }
    }
}


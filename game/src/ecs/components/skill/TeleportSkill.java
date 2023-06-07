package ecs.components.skill;

import ecs.components.ManaComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import starter.Game;
import tools.Point;

/** The TeleportSkill allows an entity to teleport to a walkable tile on the map. */
public class TeleportSkill implements ISkillFunction {

    public static final String pathToTextureUI = "skills/teleport/teleportIcon.png";
    private final int manaCost;

    public TeleportSkill(int manaCost) {
        this.manaCost = manaCost;
    }

    @Override
    public void execute(Entity entity) {
        Point teleportTo = SkillTools.getCursorPositionAsPoint();

        entity.getComponent(ManaComponent.class)
                .ifPresent(
                        component -> {
                            ManaComponent manaComp = (ManaComponent) component;

                            // execute only if tile accessible and enough mana available
                            if (Game.currentLevel
                                            .getTileAt(teleportTo.toCoordinate())
                                            .isAccessible()
                                    && manaComp.getCurrentManaPoints() - manaCost >= 0) {

                                entity.getComponent(PositionComponent.class)
                                        .ifPresent(
                                                component1 -> {
                                                    ((PositionComponent) component1)
                                                            .setPosition(teleportTo);
                                                    manaComp.setCurrentManaPoints(
                                                            manaComp.getCurrentManaPoints()
                                                                    - manaCost);
                                                });
                            }
                        });
    }
}

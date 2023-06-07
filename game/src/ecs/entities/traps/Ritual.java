package ecs.entities.traps;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HitboxComponent;
import ecs.components.PositionComponent;
import ecs.components.xp.XPComponent;
import ecs.entities.Entity;
import ecs.entities.heros.Hero;
import ecs.entities.monsters.MonsterFactory;
import graphic.Animation;
import level.elements.ILevel;
import starter.Game;
import tools.Point;

import static starter.Game.currentLevel;

/**
 * The ritual is a trap, which cannot be seen until stepped on. Monsters will spawn when activated.
 */
public class Ritual extends Trap {

    private boolean active = true;

    private Animation ritualHidden;
    private Animation ritualVisible;

    /**
     * Default constructor for a ritual trap.
     *
     * @param playerPos The position of the player in the level.
     * @param currentLevel The current map.
     */
    public Ritual(Point playerPos, ILevel currentLevel) {
        super(playerPos, currentLevel);

        setupAnimationComponent();
        setHitboxComponent();

    }

    @Override
    protected void setupAnimationComponent() {
        ritualHidden = AnimationBuilder.buildAnimation("ritual/ritual1.png", 1);
        ritualVisible = AnimationBuilder.buildAnimation("ritual/ritual3.png", 1);
        new AnimationComponent(this, ritualHidden);
    }


    @Override
    protected void setHitboxComponent() {
        new HitboxComponent(this,
            (you, other, direction) -> {
                if (other instanceof Hero) {
                    generateMonsters(5);
                }

            }, (you, other, direction) -> System.out.print(""));
    }

    /**
     * Method which creates monsters when steeping on the ritual trap.
     *
     * @param threat Amount of monsters which will be created by the method.
     */
    protected void generateMonsters(int threat) {

        if (active) {
            Entity hero = Game.getHero().get();

            hero.getComponent(XPComponent.class)
                .ifPresent(
                    component -> {
                        XPComponent comp = (XPComponent) component;

                        hero.getComponent(PositionComponent.class)
                            .ifPresent(
                                component1 -> {
                                    PositionComponent posComp =
                                        (PositionComponent) component1;

                                    for (int i = 0; i < threat; i++) {
                                        MonsterFactory.generateMonster(
                                            (int) comp.getCurrentLevel(),
                                            posComp.getPosition(),
                                            currentLevel);
                                    }
                                });
                    });

            new AnimationComponent(this, ritualVisible);
            active = false;
        }
    }
}

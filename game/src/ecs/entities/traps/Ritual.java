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

public class Ritual extends Trap {

    private boolean active = true;
    private AnimationComponent animComp;

    public Ritual(Point playerPos, ILevel currentLevel) {
        super(playerPos, currentLevel);

        pathToIdle = "ritual/ritual1";

        setupAnimationComponent();
        setHitboxComponent();

    }

    @Override
    protected void setupAnimationComponent() {
        Animation idle = AnimationBuilder.buildAnimation(pathToIdle, 1);
        animComp = new AnimationComponent(this, idle);
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

            animComp.setCurrentAnimation(AnimationBuilder.buildAnimation("ritual", 1, false));
            active = false;
        }
    }
}

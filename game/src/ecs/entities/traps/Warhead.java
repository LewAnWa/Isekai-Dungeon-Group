package ecs.entities.traps;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import graphic.Animation;
import level.elements.ILevel;
import tools.Point;

public class Warhead extends Trap {

    private boolean active = true;
    private AnimationComponent animComp;
    private Animation explosion;
    private Animation idle;

    public Warhead(Point playerPos, ILevel currentLevel) {
        super(playerPos, currentLevel);

        pathToIdle = "warhead/warhead_active";

        setupAnimationComponent();
        setHitboxComponent();
    }

    @Override
    protected void setHitboxComponent() {
        new HitboxComponent(this,
            (you, other, direction) -> {
                if (!other.isIgnorable()) {
                    other.getComponent(HealthComponent.class)
                        .ifPresent(hc -> {
                            if (active) {
                                active = false;

                                ((HealthComponent) hc).receiveHit(new Damage(30, DamageType.PHYSICAL, you));
                                System.out.println("ouch");
                                animComp.setCurrentAnimation(explosion);
                            }
                        });
                }
            }, (you, other, direction) -> System.out.print(""));

    }

    @Override
    protected void setupAnimationComponent() {
        idle = AnimationBuilder.buildAnimation(pathToIdle, 1);
        explosion = AnimationBuilder.buildAnimation("traps/warhead/warhead_explosion", 1, false);
        animComp = new AnimationComponent(this, idle);
    }

    public void setIdleAnimation() {
        animComp.setCurrentAnimation(idle);
    }
}

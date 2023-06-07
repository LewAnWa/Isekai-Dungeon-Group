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

/**
 * The warhead is a trap which wil be activated when stepped on.
 * This trap does damage to the first enemy stepping on it.
 */
public class Warhead extends Trap {

    private boolean active = true;
    private Animation explosion;
    private Animation idle;
    private Animation finish;

    /**
     * Default constructor for a warhead trap.
     *
     * @param playerPos    The position of the player in the level.
     * @param currentLevel The current map.
     */
    public Warhead(Point playerPos, ILevel currentLevel) {
        super(playerPos, currentLevel);

        setupAnimationComponent();
        setHitboxComponent();
    }

    @Override
    protected void setHitboxComponent() {
        new HitboxComponent(this,
            (you, other, direction) -> {
                if (!other.isIgnorable()) {
                    if (active) {
                        other.getComponent(HealthComponent.class)
                            .ifPresent(hc -> {
                                ((HealthComponent) hc).receiveHit(new Damage(30, DamageType.PHYSICAL, you));
                                System.out.println("ouch");
                            });
                        active = false;
                        new AnimationComponent(this, explosion);
                    }
                }
            }, (you, other, direction) -> System.out.print(""));
    }

    @Override
    protected void setupAnimationComponent() {
        idle = AnimationBuilder.buildAnimation("warhead/warhead_active", 1);
        explosion = AnimationBuilder.buildAnimation("traps/warhead/warhead_explosion", 1, false);
        finish = AnimationBuilder.buildAnimation("warhead/finish.png", 1);
        new AnimationComponent(this, idle);
    }

    /**
     * Special setter for the used animation.
     */
    public void setIdleAnimation() {
        new AnimationComponent(this, finish);
    }
}

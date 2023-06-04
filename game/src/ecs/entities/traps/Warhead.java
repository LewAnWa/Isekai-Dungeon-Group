package ecs.entities.traps;

import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import level.elements.ILevel;
import tools.Point;

public class Warhead extends Trap {

    public Warhead(Point playerPos, ILevel currentLevel) {
        super(playerPos, currentLevel);

        pathToIdle = "empty";

        setupAnimationComponent();
        setHitboxComponent();

    }

    @Override
    protected void setHitboxComponent() {
        new HitboxComponent(this,
            (you, other, direction) -> {
                if (!other.isProjectile()) {
                    other.getComponent(HealthComponent.class)
                        .ifPresent(hc -> {
                            ((HealthComponent) hc).receiveHit(new Damage(30, DamageType.PHYSICAL, you));
                            System.out.println("ouch");
                        });
                }

            }, (you, other, direction) -> System.out.print(""));

    }

}

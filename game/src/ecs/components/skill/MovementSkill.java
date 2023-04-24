package ecs.components.skill;

import ecs.components.VelocityComponent;
import ecs.entities.Entity;

import java.util.Timer;
import java.util.TimerTask;

public abstract class MovementSkill implements ISkillFunction{

    private float heroSpeed;
    private float dashDuration;

    public MovementSkill (float heroSpeed, float dashDuration){
        this.heroSpeed = heroSpeed;
        this.dashDuration = dashDuration;
    }
    @Override
    public void execute(Entity entity) {

        entity.getComponent(VelocityComponent.class)
            .ifPresent(
                hvc -> {
                    ((VelocityComponent) hvc).setXVelocity(heroSpeed);
                    ((VelocityComponent) hvc).setYVelocity(heroSpeed);
                });

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                entity.getComponent(VelocityComponent.class)
                    .ifPresent(
                        hvc -> {
                            ((VelocityComponent) hvc).setXVelocity(0.3f);
                            ((VelocityComponent) hvc).setYVelocity(0.3f);
                        });
                timer.cancel();
            }
        }, 80);


    }
}

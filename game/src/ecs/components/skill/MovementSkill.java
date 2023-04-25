package ecs.components.skill;

import ecs.components.StaminaComponent;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;

import java.util.Timer;
import java.util.TimerTask;

public abstract class MovementSkill implements ISkillFunction{

    private float heroSpeed;
    private int staminaCost;

    public MovementSkill (float heroSpeed, int staminaCost){
        this.heroSpeed = heroSpeed;
        this.staminaCost = staminaCost;
    }
    @Override
    public void execute(Entity entity) {
        // get the StaminaComponent of the Entity that want to execute this Skill
        entity.getComponent(StaminaComponent.class).ifPresent(component -> {
            StaminaComponent comp = (StaminaComponent) component;

            // only if enough Stamina, then execute the Skill
            if (comp.getCurrentStamina() - staminaCost >= 0) {
                // reduce Stamina by the cost
                comp.setCurrentStamina(comp.getCurrentStamina() - staminaCost);

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
            else {
                System.out.println("You are too exhausted!");
            }
        });
    }
}

package ecs.components.skill;

import ecs.components.StaminaComponent;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;

import java.util.Timer;
import java.util.TimerTask;

public abstract class MovementSkill implements ISkillFunction{

    private float heroSpeed;
    private int staminaCost;

    /**
     * @param heroSpeed is the velocity the Hero will receive upon using the skill
     * @param staminaCost is the cost of stamina to use the skill
     */

    public MovementSkill (float heroSpeed, int staminaCost){
        this.heroSpeed = heroSpeed;
        this.staminaCost = staminaCost;
    }

    /**
     * Upon use of this skill in game, the Entity that uses this skill (Hero) will receive a boost to its Velocity
     * for a very short duration and appears to be dashing in the moving direction
     */

    @Override
    public void execute(Entity entity) {
        // get the StaminaComponent of the Entity that want to execute this Skill
        entity.getComponent(StaminaComponent.class).ifPresent(component -> {
            StaminaComponent comp = (StaminaComponent) component;

            // only if enough Stamina, then execute the Skill
            if (comp.getCurrentStamina() - staminaCost >= 0) {
                // reduce Stamina by the cost
                comp.setCurrentStamina(comp.getCurrentStamina() - staminaCost);
                System.out.println(comp.getCurrentStamina()+"/"+comp.getMaxStamina()+" STAMINA");

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

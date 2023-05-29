package ecs.components.skill;

import ecs.components.AnimationComponent;
import ecs.components.StaminaComponent;
import ecs.entities.Entity;

public class InvisibilitySkill implements ISkillFunction {

    public static final String pathToTextureUI = "skills/invisibility/invisibilityIcon.png";
    private final int staminaCost;

    public InvisibilitySkill(int staminaCost) {
        this.staminaCost = staminaCost;
    }

    @Override
    public void execute(Entity entity) {
        if (!entity.isVisible()) {
            entity.setVisible(true);
        } else {

            entity.getComponent(StaminaComponent.class).ifPresent(component -> {
                StaminaComponent staminaComp = (StaminaComponent) component;

                if (staminaComp.getCurrentStamina() - staminaCost >= 0) {
                    entity.setVisible(false);

                    entity.getComponent(AnimationComponent.class).ifPresent(animComp -> {
                        // TODO: INDICATE TO THE PLAYER THAT HE IS INVISIBLE!
                    });

                    staminaComp.setCurrentStamina(staminaComp.getCurrentStamina() - staminaCost);
                }
            });
        }
    }
}

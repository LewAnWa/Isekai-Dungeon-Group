package ecs.components.skill;

import ecs.components.StaminaComponent;
import ecs.entities.Entity;

/**
 * The InvisibilitySkill allows an entity to toggle between becoming visible and invisible.
 */
public class InvisibilitySkill implements ISkillFunction {

    public static final String pathToTextureUI = "skills/invisibility/invisibilityIcon.png";
    private static int staminaCost;
    private static int counter = 0;

    public InvisibilitySkill(int staminaCost) {
        InvisibilitySkill.staminaCost = staminaCost;
    }

    /**
     * Applies the stamina costs of being invisible to an entity.
     * It does that only if the entity is invisible, else it will do nothing.
     * <p>
     * NOTE: THIS METHOD SHOULD BE CALLED BY THE GAME IN THE FRAME METHOD!!!
     * @param entity The entity that turned invisible and should be charged for that.
     */
    public static void applyInvisibilityCost(Entity entity) {
        if (entity.isVisible()) return;

        counter++;

        if (counter >= 30) {
            entity.getComponent(StaminaComponent.class).ifPresent(component -> {
                StaminaComponent staminaComp = (StaminaComponent) component;

                staminaComp.setCurrentStamina(staminaComp.getCurrentStamina() - staminaCost);

                if (staminaComp.getCurrentStamina() <= 0) entity.setVisible(true);
            });

            counter = 0;
        }
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
                    staminaComp.setCurrentStamina(staminaComp.getCurrentStamina() - staminaCost);
                }
            });
        }
    }
}

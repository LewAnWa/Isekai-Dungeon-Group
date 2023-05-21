package ecs.components;

import ecs.entities.Entity;

/** The StaminaComponent adds the resource stamina to an entity */
public class StaminaComponent extends Component {

    private int currentStamina;
    private int maxStamina;

    /**
     * Creates a new ManaComponent
     *
     * @param entity associated entity
     * @param maxStamina maximum amount of stamina
     */
    public StaminaComponent(Entity entity, int maxStamina) {
        super(entity);

        this.maxStamina = maxStamina;
        this.currentStamina = maxStamina;
    }

    @Override
    public String toString() {
        return currentStamina + "/" + maxStamina;
    }

    // -------------------- GETTER AND SETTER -------------------- //

    /**
     * @return The current stamina the entity has
     */
    public int getCurrentStamina() {
        return currentStamina;
    }

    /**
     * Sets the current stamina
     *
     * @param currentStamina new amount of current stamina
     */
    public void setCurrentStamina(int currentStamina) {
        this.currentStamina = currentStamina;
    }

    /**
     * @return The maximum stamina the entity can have
     */
    public int getMaxStamina() {
        return maxStamina;
    }

    /**
     * Sets the maximum stamina
     *
     * @param maxStamina new amount of maximum stamina
     */
    public void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
        currentStamina = maxStamina;
    }
}

package ecs.components;

import ecs.entities.Entity;

public class StaminaComponent extends Component {

    private int currentStamina;
    private int maxStamina;

    public StaminaComponent(Entity entity, int maxStamina) {
        super(entity);

        this.maxStamina = maxStamina;
        this.currentStamina = maxStamina;
    }

    // -------------------- GETTER AND SETTER -------------------- //
    public int getCurrentStamina() {
        return currentStamina;
    }

    public void setCurrentStamina(int currentStamina) {
        this.currentStamina = currentStamina;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
        currentStamina = Math.min(currentStamina, maxStamina);
    }
}

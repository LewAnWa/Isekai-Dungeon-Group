package ecs.components;

import ecs.entities.Entity;

public class ManaComponent extends Component {

    private int currentManaPoints;
    private int maximalManaPoints;

    public ManaComponent(Entity entity, int maximalManaPoints) {
        super(entity);

        this.maximalManaPoints = maximalManaPoints;
        this.currentManaPoints = maximalManaPoints;
    }

    // -------------------- GETTER AND SETTER -------------------- //
    public int getCurrentManaPoints() {
        return currentManaPoints;
    }

    public void setCurrentManaPoints(int currentManaPoints) {
        this.currentManaPoints = currentManaPoints;
    }

    public int getMaximalManaPoints() {
        return maximalManaPoints;
    }

    public void setMaximalManaPoints(int maximalManaPoints) {
        this.maximalManaPoints = maximalManaPoints;
        currentManaPoints = Math.min(currentManaPoints, maximalManaPoints);
    }
}

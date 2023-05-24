package ecs.components;

import ecs.entities.Entity;

/** The ManaComponent adds the resource Mana to an entity */
public class ManaComponent extends Component {

    private int currentManaPoints;
    private int maximalManaPoints;

    /**
     * Creates a new ManaComponent
     *
     * @param entity associated entity
     * @param maximalManaPoints maximum amount of mana-points
     */
    public ManaComponent(Entity entity, int maximalManaPoints) {
        super(entity);

        this.maximalManaPoints = maximalManaPoints;
        this.currentManaPoints = maximalManaPoints;
    }

    @Override
    public String toString() {
        return currentManaPoints + "/" + maximalManaPoints;
    }

    // -------------------- GETTER AND SETTER -------------------- //

    /**
     * @return The current mana-points the entity has
     */
    public int getCurrentManaPoints() {
        return currentManaPoints;
    }

    /**
     * Sets the current mana points
     *
     * @param currentManaPoints new amount of current mana-points
     */
    public void setCurrentManaPoints(int currentManaPoints) {
        this.currentManaPoints = Math.min(currentManaPoints, maximalManaPoints);
    }

    /**
     * @return The maximum mana-points the entity can have
     */
    public int getMaximalManaPoints() {
        return maximalManaPoints;
    }

    /**
     * Sets the maximum mana-points
     *
     * @param maximalManaPoints new amount of maximum mana-points
     */
    public void setMaximalManaPoints(int maximalManaPoints) {
        this.maximalManaPoints = maximalManaPoints;
        currentManaPoints = maximalManaPoints;
    }
}

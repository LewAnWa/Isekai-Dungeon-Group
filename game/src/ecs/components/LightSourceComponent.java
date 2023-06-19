package ecs.components;

import ecs.entities.Entity;

/**
 * A component that makes an Entity behave as a light source.
 *
 * <p>This is a process heavy component!!
 */
public class LightSourceComponent extends Component {

    private final float lightRadius;

    /**
     * Create a new component and add it to the associated entity
     *
     * @param entity associated entity
     * @param lightRadius radius of the light
     */
    public LightSourceComponent(Entity entity, float lightRadius) {
        super(entity);

        this.lightRadius = lightRadius;
    }

    // -------------------- Getter -------------------- //
    public float getLightRadius() {
        return lightRadius;
    }
}

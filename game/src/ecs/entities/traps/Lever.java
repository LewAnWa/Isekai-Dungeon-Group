package ecs.entities.traps;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.InteractionComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import graphic.Animation;
import starter.Game;

/**
 * The lever extends the functionality of the teleporter trap.
 * Can be used to activate or deactivate the teleporter.
 */
public class Lever extends Entity {

    protected Teleporter teleporter;
    public static final float defaultInteractionRadius = 1f;

    private Animation moveAI = AnimationBuilder.buildAnimation(
        "traps/lever/lever_moving_active-inactive", 1, false);
    private Animation moveIA = AnimationBuilder.buildAnimation(
        "traps/lever/lever_moving_inactive-active", 1, false);
    private Animation active = AnimationBuilder.buildAnimation(
        "traps/lever/lever_active", 1, false);
    private Animation inactive = AnimationBuilder.buildAnimation(
        "traps/lever/lever_inactive", 1, false);


    /**
     * Default constructor for a lever.
     *
     * @param teleporter Connect the teleporter and the lever.
     */
    public Lever(Teleporter teleporter){
        super(true);
        this.teleporter = teleporter;
        new PositionComponent(this, Game.currentLevel.getRandomFloorTile().getCoordinateAsPoint());
        new InteractionComponent(this, defaultInteractionRadius, true, this::flipSwitchOn);
        new AnimationComponent(this, inactive);
    }

    /**
     * Implements the interaction behavior of an Interactive entity.
     *
     * @param entity Is the entity which has the interaction component.
     */
    public void flipSwitchOn(Entity entity) {
        if(teleporter.isActive()){
            teleporter.setInactive();
            new AnimationComponent(this, moveAI);
            new AnimationComponent(this, inactive);
        }else{
            teleporter.setActive();
            new AnimationComponent(this, moveIA);
            new AnimationComponent(this, active);
        }
    }
}

package ecs.entities.traps;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HitboxComponent;
import ecs.components.PositionComponent;
import ecs.entities.monsters.Monster;
import graphic.Animation;
import level.elements.ILevel;
import starter.Game;
import tools.Point;

/**
 * The teleporter is a trap which be activated and deactivated by using a lever. Will set the hero
 * to a random accessible location in the dungeon.
 */
public class Teleporter extends Trap {

    private Animation teleporterActivated;
    private Animation teleporterDeactivated;

    private boolean active = false;

    /**
     * Default constructor for a teleport trap.
     *
     * @param playerPos The position of the player in the level.
     * @param currentLevel The current map.
     */
    public Teleporter(Point playerPos, ILevel currentLevel) {
        super(playerPos, currentLevel);
        new Lever(this);
        setupAnimationComponent();
        setHitboxComponent();
    }

    @Override
    protected void setupAnimationComponent() {
        teleporterDeactivated =
                AnimationBuilder.buildAnimation("teleporter/teleport_inactive.png", 1);
        teleporterActivated = AnimationBuilder.buildAnimation("teleporter/teleport_active.png", 1);
        new AnimationComponent(this, teleporterDeactivated);
    }

    @Override
    protected void setHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> {
                    if (!(other instanceof Monster)) {
                        if (active) {
                            other.getComponent(PositionComponent.class)
                                    .ifPresent(
                                            pC -> {
                                                PositionComponent positionComponent =
                                                        (PositionComponent) pC;
                                                positionComponent.setPosition(
                                                        Game.currentLevel
                                                                .getRandomFloorTile()
                                                                .getCoordinateAsPoint());
                                            });
                        }
                    }
                },
                (you, other, direction) -> System.out.print(""));
    }

    /** Special Setter for boolean which updates the AnimationComponent too */
    protected void setActive() {
        this.active = true;
        new AnimationComponent(this, teleporterActivated);
    }

    /** Special Setter for boolean which updates the AnimationComponent too */
    protected void setInactive() {
        this.active = false;
        new AnimationComponent(this, teleporterDeactivated);
    }

    // -------------------Getter-------------------//
    public boolean isActive() {
        return active;
    }
}

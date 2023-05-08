package ecs.entities;

import ecs.components.Component;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

import ecs.components.PositionComponent;
import semanticAnalysis.types.DSLContextPush;
import semanticAnalysis.types.DSLType;
import starter.Game;
import tools.Point;

/** Entity is a unique identifier for an object in the game world */
@DSLType(name = "game_object")
@DSLContextPush(name = "entity")
public class Entity {
    private static int nextId = 0;
    public final int id = nextId++;
    private HashMap<Class, Component> components;
    private final Logger entityLogger;

    private boolean isBoomerang = false;
    public boolean reachedMiddlePoint = false;


    public Entity() {
        components = new HashMap<>();
        Game.addEntity(this);
        entityLogger = Logger.getLogger(this.getClass().getName());
        entityLogger.info("The entity '" + this.getClass().getSimpleName() + "' was created.");
    }

    public Entity(boolean isBoomerang) {
        components = new HashMap<>();
        Game.addEntity(this);
        entityLogger = Logger.getLogger(this.getClass().getName());
        entityLogger.info("The entity '" + this.getClass().getSimpleName() + "' was created.");
        isBoomerang = true;
    }

    /**
     * Add a new component to this entity
     *
     * @param component The component
     */
    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    /**
     * Remove a component from this entity
     *
     * @param klass Class of the component
     */
    public void removeComponent(Class klass) {
        components.remove(klass);
    }

    // -------------------- Getter and Setter -------------------- //
    public boolean isBoomerang() {
        return isBoomerang;
    }

    /**
     * Get the component
     *
     * @param klass Class of the component
     * @return Optional that can contain the requested component
     */
    public Optional<Component> getComponent(Class klass) {
        return Optional.ofNullable(components.get(klass));
    }
}

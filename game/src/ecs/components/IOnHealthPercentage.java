package ecs.components;

import ecs.entities.Entity;

/** Functional interfaces for implementing a function that is called when an entity only has a certain amount of Health left */
public interface IOnHealthPercentage {

    /**
     * Function that is performed when an entity has a certain amount of Health
     *
     * @param entity Entity who's health is tracked
     */
    void onHealthPercentage(Entity entity);
}

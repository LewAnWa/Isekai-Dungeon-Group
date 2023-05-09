package ecs.systems;

import ecs.components.*;
import ecs.components.skill.BumerangSkill;
import ecs.components.skill.ProjectileComponent;
import ecs.entities.Entity;
import starter.Game;
import tools.Point;

public class ProjectileSystem extends ECS_System {

    private static Point heroPos;
    private boolean reachedMiddlePoint = false;

    // private record to hold all data during streaming
    private record PSData(
        Entity e, ProjectileComponent prc, PositionComponent pc, VelocityComponent vc) {
    }

    /**
     * sets the velocity and removes entities that reached their endpoint
     */
    @Override
    public void update() {
        Game.getEntities().stream()
            // Consider only entities that have a ProjectileComponent
            .flatMap(e -> e.getComponent(ProjectileComponent.class).stream())
            .map(prc -> buildDataObject((ProjectileComponent) prc))
            .map(this::setVelocity)
            // Filter all entities that have reached their endpoint
            .filter(
                psd -> {
                    // if the entity is NOT a boomerang or if the entity(boomerang) has reached its middle-point
                    if (!psd.e.isBoomerang() || psd.e.reachedMiddlePoint) {
                        // check if endpoint reached
                        return hasReachedEndpoint(
                            psd.prc.getStartPosition(),
                            psd.prc.getGoalLocation(),
                            psd.pc.getPosition());
                    } else {
                        // check if middle-point reached
                        hasReachedMiddlePoint(
                            psd.prc.getStartPosition(),
                            psd.prc.getGoalLocation(),
                            psd.pc.getPosition(),
                            psd.e);
                        return false;
                    }
                })
            // Remove all entities who reached their endpoint
            .forEach(this::removeEntitiesOnEndpoint);
    }

    private PSData buildDataObject(ProjectileComponent prc) {
        Entity e = prc.getEntity();

        PositionComponent pc =
            (PositionComponent)
                e.getComponent(PositionComponent.class)
                    .orElseThrow(ProjectileSystem::missingAC);
        VelocityComponent vc =
            (VelocityComponent)
                e.getComponent(VelocityComponent.class)
                    .orElseThrow(ProjectileSystem::missingAC);

        return new PSData(e, prc, pc, vc);
    }

    private PSData setVelocity(PSData data) {
        data.vc.setCurrentYVelocity(data.vc.getYVelocity());
        data.vc.setCurrentXVelocity(data.vc.getXVelocity());

        return data;
    }

    private void removeEntitiesOnEndpoint(PSData data) {
        Game.removeEntity(data.pc.getEntity());
    }

    /**
     * checks if the endpoint is reached
     *
     * @param start   position to start the calculation
     * @param end     point to check if projectile has reached its goal
     * @param current current position
     * @return true if the endpoint was reached or passed, else false
     */
    public boolean hasReachedEndpoint(Point start, Point end, Point current) {
        float dx = start.x - current.x;
        float dy = start.y - current.y;
        double distanceToStart = Math.sqrt(dx * dx + dy * dy);

        dx = start.x - end.x;
        dy = start.y - end.y;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        if (distanceToStart > totalDistance) {
            // The point has reached or passed the endpoint
            reachedMiddlePoint = false;
            return true;
        } else {
            // The point has not yet reached the endpoint
            return false;
        }
    }

    /**
     * Works just like the hasReachedEndpoint, but with slight modifications.
     * This method should be used for the boomerang skill, and it checks if the boomerang has
     * reached its endpoint.
     * If that is the case, then the old boomerang will be removed and a new one will be shot back
     * to the hero.
     *
     * @param start      position to start the calculation
     * @param end        point to check if projectile has reached its goal
     * @param current    current position
     * @param projectile the entity that can return to its owner
     */
    public void hasReachedMiddlePoint(Point start, Point end, Point current, Entity projectile) {
        float dx = start.x - current.x;
        float dy = start.y - current.y;
        double distanceToStart = Math.sqrt(dx * dx + dy * dy);

        dx = start.x - end.x;
        dy = start.y - end.y;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        Game.getHero()
            .flatMap(hero -> hero.getComponent(PositionComponent.class))
            .ifPresent(component -> {
                heroPos = ((PositionComponent) component).getPosition();
            });

        if (distanceToStart > totalDistance && !reachedMiddlePoint) { // The point has reached or passed the middle-point
            reachedMiddlePoint = true;
            Game.removeEntity(projectile);
            BumerangSkill bumerangSkill = new BumerangSkill(ProjectileSystem::getHeroPos, projectile.getUser());
            bumerangSkill.execute(projectile, true);
        }
    }

    private static Point getHeroPos() {
        return heroPos;
    }

    private static MissingComponentException missingAC() {
        return new MissingComponentException("AnimationComponent");
    }
}

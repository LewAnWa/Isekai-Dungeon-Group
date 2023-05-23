package ecs.items;

import ecs.components.*;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;
import starter.Game;
import tools.Point;

import java.util.List;

public class Schuh extends ItemData implements IOnCollect, IOnDrop {

    private static final List<String> schuhTexture = List.of("items/Schuh/Schuh.png");

    public Schuh() {
        super(ItemType.Ausruestung,
            new Animation(schuhTexture, 1),
            new Animation(schuhTexture,1),
            "Schuh",
            "Alte und aufgetragene Wanderstiefel."
        );

        WorldItemBuilder.buildWorldItem(this);
        this.setOnCollect(this);
        this.setOnDrop(this);

    }

    @Override
    public void onCollect(Entity WorldItemEntity, Entity whoCollides) {
        if (whoCollides instanceof Hero hero) {
            Game.removeEntity(WorldItemEntity);
            hero.getComponent(InventoryComponent.class)
                .ifPresent(iC -> {
                    ((InventoryComponent) iC).addItem(this);
                });

            hero.getComponent(VelocityComponent.class)
                .ifPresent(vC ->{
                    ((VelocityComponent) vC).setYVelocity(((VelocityComponent) vC).getYVelocity() + 0.05f);
                    ((VelocityComponent) vC).setXVelocity(((VelocityComponent) vC).getXVelocity() + 0.05f);
                });
        }
    }

    @Override
    public void onDrop(Entity user, ItemData which, Point position) {
        Entity droppedItem = new Entity();
        new PositionComponent(droppedItem, position);
        new AnimationComponent(droppedItem, which.getWorldTexture());
        HitboxComponent component = new HitboxComponent(droppedItem);
        component.setiCollideEnter((a, b, direction) -> which.triggerCollect(a, b));

        user.getComponent(InventoryComponent.class)
            .ifPresent(iC -> {
                ((InventoryComponent) iC).removeItem(this);
            });

        user.getComponent(VelocityComponent.class)
            .ifPresent(vC ->{
                ((VelocityComponent) vC).setYVelocity(((VelocityComponent) vC).getYVelocity() - 0.05f);
                ((VelocityComponent) vC).setXVelocity(((VelocityComponent) vC).getXVelocity() - 0.05f);
            });
    }
}



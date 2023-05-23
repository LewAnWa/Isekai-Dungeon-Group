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
            hero.getComponent(InventoryComponent.class)
                .ifPresent(iC -> {
                    InventoryComponent inventoryComp = (InventoryComponent) iC;

                    List<Bag> bagList;
                    if (inventoryComp.checkForBag()){
                        bagList = inventoryComp.getBags();
                        for (Bag bag: bagList) {
                            if(bag.isEmpty() || (bag.getInhaltsArt() == this.getItemType() && bag.emptySlots() > 0)){
                                bag.addItem(this);
                                addMovementSpeed(whoCollides);
                                Game.removeEntity(WorldItemEntity);
                                return;
                            }
                        }
                    }

                    if(inventoryComp.emptySlots() > 0) {
                        inventoryComp.addItem(this);
                        addMovementSpeed(whoCollides);
                        Game.removeEntity(WorldItemEntity);
                    }
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

    private static void addMovementSpeed(Entity hero){
        hero.getComponent(VelocityComponent.class)
            .ifPresent(vC ->{
                ((VelocityComponent) vC).setYVelocity(((VelocityComponent) vC).getYVelocity() + 0.05f);
                ((VelocityComponent) vC).setXVelocity(((VelocityComponent) vC).getXVelocity() + 0.05f);
            });
    }
}



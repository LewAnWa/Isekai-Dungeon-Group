package ecs.items;

import dslToGame.AnimationBuilder;
import ecs.components.InventoryComponent;
import ecs.components.ManaComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import starter.Game;

public class ManaTrank extends ItemData implements IOnCollect, IOnUse {

    public ManaTrank() {
        super(ItemType.Tasche,
            AnimationBuilder.buildAnimation("items/ManaTrank"),
            AnimationBuilder.buildAnimation("items/ManaTrank"),
            "Tasche",
            "Eine Tasche, die Ihr Inventar um 4 Plätze der gleichen Itemart erweitert."
        );

        WorldItemBuilder.buildWorldItem(this);
    }

    @Override
    public void onCollect(Entity WorldItemEntity, Entity whoCollides) {
        if (whoCollides instanceof Hero hero) {
            Game.removeEntity(WorldItemEntity);
            hero.getComponent(InventoryComponent.class)
                .ifPresent(iC -> {
                    ((InventoryComponent) iC).addItem(this);
                });
        }
    }

    @Override
    public void onUse(Entity e, ItemData item) {

        e.getComponent(ManaComponent.class)
            .ifPresent(mC -> {
                ((ManaComponent) mC).setCurrentManaPoints(((ManaComponent) mC).getCurrentManaPoints() + 10);
            });

        e.getComponent(InventoryComponent.class)
            .ifPresent(iC -> {
                ((InventoryComponent) iC).removeItem(item);
            });
    }
}

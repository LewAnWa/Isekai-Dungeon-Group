package ecs.items;

import ecs.components.InventoryComponent;
import ecs.components.ManaComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;
import starter.Game;

import java.util.List;

public class ManaTrank extends ItemData implements IOnCollect, IOnUse {

    private static final List<String> manaTrankTexture = List.of("items/ManaTrank/ManaTrank.png");

    public ManaTrank() {
        super(ItemType.Trank,
            new Animation(manaTrankTexture, 1),
            new Animation(manaTrankTexture,1),
            "Manatrank",
            "Ein blaues, dickflüssiges Elixir."
        );

        WorldItemBuilder.buildWorldItem(this);
        this.setOnCollect(this);
        this.setOnUse(this);
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

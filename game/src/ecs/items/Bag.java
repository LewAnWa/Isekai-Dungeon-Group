package ecs.items;

import dslToGame.AnimationBuilder;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import starter.Game;

import java.util.ArrayList;
import java.util.List;

public class Bag extends ItemData implements IOnCollect {

    private List<ItemData> inventory;
    private ItemType inhaltsArt;
    private final int maxSize = 4;

    public Bag() {
        super(
            ItemType.Tasche,
            AnimationBuilder.buildAnimation("animation"),
            AnimationBuilder.buildAnimation("animation"),
            "Tasche",
            "Eine Tasche, die Ihr Inventar um 4 Plätze der gleichen Itemart erweitert."
        );

        WorldItemBuilder.buildWorldItem(this);
        inventory = new ArrayList<>(4);

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

    public void addItem(ItemData itemData){
        if (inventory.size()>= maxSize){
            return;
        }

        if (inventory.isEmpty()){
            inventory.add(itemData);
            inhaltsArt = itemData.getItemType();
            return;
        }

        if(itemData.getItemType() == inhaltsArt){
            inventory.add(itemData);
        }
    }
}

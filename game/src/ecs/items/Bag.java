package ecs.items;

import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;
import starter.Game;

import java.util.ArrayList;
import java.util.List;

public class Bag extends ItemData implements IOnCollect {

    private List<ItemData> inventory;
    private ItemType inhaltsArt;
    private final int maxSize = 5;
    private static final List<String> bagTexture = List.of("items/Tasche/Tasche.png");

    public Bag() {
        super(
            ItemType.Tasche,
            new Animation(bagTexture, 1),
            new Animation(bagTexture,1),
            "Tasche",
            "Eine Tasche, die Ihr Inventar um 4 Plätze der gleichen Itemart erweitert."
        );

        WorldItemBuilder.buildWorldItem(this);
        inventory = new ArrayList<>(4);
        this.setOnCollect(this);

    }

    @Override
    public void onCollect(Entity WorldItemEntity, Entity whoCollides) {
        if (whoCollides instanceof Hero hero) {
            hero.getComponent(InventoryComponent.class)
                .ifPresent(iC -> {
                    InventoryComponent inventoryComp = (InventoryComponent) iC;

                    if(inventoryComp.getBags().size() < 3){
                        inventoryComp.addItem(this);
                        Game.removeEntity(WorldItemEntity);
                    }
                });
        }
    }

    public void addItem(ItemData itemData){
        if(itemData.getItemType() == ItemType.Tasche){ //Taschen können keine Taschen Tragen
            return;
        }

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

    public boolean removeItem(ItemData itemData) {
        return inventory.remove(itemData);
    }

    public int filledSlots() {
        return inventory.size();
    }

    public int emptySlots() {
        return maxSize - inventory.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public List<ItemData> getItems() {
        return new ArrayList<>(inventory);
    }

    public ItemType getInhaltsArt(){
        return inhaltsArt;
    }

    public boolean isEmpty(){
        return emptySlots() == maxSize;
    }
}

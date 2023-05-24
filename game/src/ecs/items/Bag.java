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

    /**
     * the constructor for the bag
     */
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

    /**
     * Adds an item to the bag
     * @param itemData the item that will be added to the bag
     */
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

    /**
     * removes an item from the bag
     * @param itemData the item that will be removed
     * @return true if item could be successfully removed
     */
    public boolean removeItem(ItemData itemData) {
        return inventory.remove(itemData);
    }

    /**
     * @return the number of slots filled inside the bag
     */
    public int filledSlots() {
        return inventory.size();
    }

    /**
     * @return the number of empty slots left inside the bag
     */
    public int emptySlots() {
        return maxSize - inventory.size();
    }

    /**
     * @return the maximum size of the bag
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * @return a list of all items inside the bag
     */
    public List<ItemData> getItems() {
        return new ArrayList<>(inventory);
    }

    /**
     * @return the type of item that can be stowed inside the bag
     */
    public ItemType getInhaltsArt(){
        return inhaltsArt;
    }

    /**
     * @return true if the bag is empty
     */
    public boolean isEmpty(){
        return emptySlots() == maxSize;
    }
}

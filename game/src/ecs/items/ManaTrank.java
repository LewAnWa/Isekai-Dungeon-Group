package ecs.items;

import ecs.components.InventoryComponent;
import ecs.components.ManaComponent;
import ecs.entities.Entity;
import ecs.entities.heros.Hero;
import graphic.Animation;
import java.util.List;
import starter.Game;

/** The ManaTrank is an item which will instantly restore a certain amount of mana after use */
public class ManaTrank extends ItemData implements IOnCollect, IOnUse {

    private static final List<String> manaTrankTexture = List.of("items/ManaTrank/ManaTrank.png");

    /** the constructor for the ManaTrank */
    public ManaTrank() {
        super(
                ItemType.Trank,
                new Animation(manaTrankTexture, 1),
                new Animation(manaTrankTexture, 1),
                "Manatrank",
                "Ein blaues, dickflüssiges Elixir.");

        this.setOnCollect(this);
        this.setOnUse(this);
    }

    @Override
    public void onCollect(Entity WorldItemEntity, Entity whoCollides) {
        if (whoCollides instanceof Hero hero) {
            hero.getComponent(InventoryComponent.class)
                    .ifPresent(
                            iC -> {
                                InventoryComponent inventoryComp = (InventoryComponent) iC;

                                List<Bag> bagList;
                                if (inventoryComp.checkForBag()) {
                                    bagList = inventoryComp.getBags();
                                    for (Bag bag : bagList) {
                                        if (bag.isEmpty()
                                                || (bag.getInhaltsArt() == this.getItemType()
                                                        && bag.emptySlots() > 0)) {
                                            bag.addItem(this);
                                            Game.removeEntity(WorldItemEntity);
                                            return;
                                        }
                                    }
                                }

                                if (inventoryComp.emptySlots() > 0) {
                                    inventoryComp.addItem(this);
                                    Game.removeEntity(WorldItemEntity);
                                }
                            });
        }
    }

    @Override
    public void onUse(Entity e, ItemData item) {

        e.getComponent(ManaComponent.class)
                .ifPresent(
                        mC -> {
                            ((ManaComponent) mC)
                                    .setCurrentManaPoints(
                                            ((ManaComponent) mC).getCurrentManaPoints() + 10);
                        });
    }
}

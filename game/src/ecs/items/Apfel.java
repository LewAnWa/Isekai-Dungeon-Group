package ecs.items;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import graphic.Animation;
import starter.Game;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Apfel extends ItemData implements IOnCollect, IOnUse {

    private static final List<String> apfelTexture = List.of("items/Apfel/Apfel.png");

    public Apfel() {
        super(ItemType.Nahrung,
            new Animation(apfelTexture, 1),
            new Animation(apfelTexture, 1),
            "Apfel",
            "Ein wahrhaft leckeres Obst."
        );

        WorldItemBuilder.buildWorldItem(this);
        this.setOnCollect(this);
        this.setOnUse(this);
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
                                Game.removeEntity(WorldItemEntity);
                                return;
                            }
                        }
                    }

                    if(inventoryComp.emptySlots() > 0) {
                        inventoryComp.addItem(this);
                        Game.removeEntity(WorldItemEntity);
                    }
                });
        }
    }

    @Override
    public void onUse(Entity e, ItemData item) {
        int counter = 0;
        Timer timer = new Timer();

        while (counter <= 10) {
            timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        e.getComponent(HealthComponent.class)
                            .ifPresent(
                                hC -> {
                                    ((HealthComponent) hC)
                                        .setCurrentHealthpoints(((HealthComponent) hC)
                                            .getCurrentHealthpoints() + 1);
                                }
                            );
                    }
                }, 0);
            counter++;
        }
    }
}

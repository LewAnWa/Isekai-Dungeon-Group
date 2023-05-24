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
    private int regCounter = 0;

    /**
     * The constructor for the Apfel
     */
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
        regenerateHealth(e, 1);
    }

    /**
     * this function adds a certain amount of health once
     * @param entity whos health will be increased
     * @param healthAmount that will be added
     */
    public void addSomeHealth(Entity entity, int healthAmount){
        entity.getComponent(HealthComponent.class)
            .ifPresent(
                hC -> {
                    ((HealthComponent) hC)
                        .setCurrentHealthpoints(((HealthComponent) hC)
                            .getCurrentHealthpoints() + healthAmount);
                }
            );
        regCounter++;
    }

    /**
     * regenerates health for a certain amount of time
     * @param entity whos health will be regenerated
     * @param healthPerSecond the amount of health that will be added every second (or declared time)
     */
    public void regenerateHealth(Entity entity, int healthPerSecond){
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                addSomeHealth(entity, healthPerSecond);
                if(regCounter == 10){
                    timer.cancel();
                    regCounter = 0;
                }
            }
        }, 1000, 1000); //period --> time between function calls
    }
}

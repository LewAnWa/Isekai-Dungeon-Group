package ecs.systems;

import com.badlogic.gdx.Gdx;
import configuration.KeyboardConfig;
import ecs.components.HealthComponent;
import ecs.components.MissingComponentException;
import ecs.components.PlayableComponent;
import ecs.components.VelocityComponent;
import ecs.components.xp.XPComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.tools.interaction.InteractionTool;
import starter.Game;

/** Used to control the player */
public class PlayerSystem extends ECS_System {

    private record KSData(Entity e, PlayableComponent pc, VelocityComponent vc) {}

    @Override
    public void update() {
        Game.getEntities().stream()
                .flatMap(e -> e.getComponent(PlayableComponent.class).stream())
                .map(pc -> buildDataObject((PlayableComponent) pc))
                .forEach(this::checkKeystroke);
    }

    private void checkKeystroke(KSData ksd) {
        if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_UP.get()))
            ksd.vc.setCurrentYVelocity(1 * ksd.vc.getYVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_DOWN.get()))
            ksd.vc.setCurrentYVelocity(-1 * ksd.vc.getYVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_RIGHT.get()))
            ksd.vc.setCurrentXVelocity(1 * ksd.vc.getXVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_LEFT.get()))
            ksd.vc.setCurrentXVelocity(-1 * ksd.vc.getXVelocity());

        if (Gdx.input.isKeyPressed(KeyboardConfig.INTERACT_WORLD.get()))
            InteractionTool.interactWithClosestInteractable(ksd.e);

        // check skills
        else if (Gdx.input.isKeyPressed(KeyboardConfig.FIRST_SKILL.get()))
            ksd.pc.getSkillSlot1().ifPresent(skill -> skill.execute(ksd.e));
        else if (Gdx.input.isKeyPressed(KeyboardConfig.SECOND_SKILL.get()))
            ksd.pc.getSkillSlot2().ifPresent(skill -> skill.execute(ksd.e));
        else if (Gdx.input.isKeyPressed(KeyboardConfig.THIRD_SKILL.get()))
            ksd.pc.getSkillSlot3().ifPresent(skill -> skill.execute(ksd.e));
        else if (Gdx.input.isKeyPressed(KeyboardConfig.FOURTH_SKILL.get()))
            ksd.pc.getSkillSlot4().ifPresent(skill -> skill.execute(ksd.e));
        else if (Gdx.input.isKeyPressed(KeyboardConfig.FIFTH_SKILL.get()))
            ksd.pc.getSkillSlot5().ifPresent(skill -> skill.execute(ksd.e));
        else if (Gdx.input.isKeyPressed(KeyboardConfig.SIXTH_SKILL.get()))
            ksd.pc.getSkillSlot6().ifPresent(skill -> skill.execute(ksd.e));


        if (Gdx.input.isKeyPressed(KeyboardConfig.XPADDER_SKILL.get())){
            Game.getHero().flatMap(hero -> hero.getComponent(XPComponent.class)).ifPresent(component -> {
                ((XPComponent) component).addXP(50);
                System.out.println("Added 50 XP");
            });
        }

        if (Gdx.input.isKeyPressed(KeyboardConfig.HERO_INFO.get()))
            Game.getHero().flatMap(hero -> hero.getComponent(XPComponent.class)).ifPresent(component -> {
                XPComponent comp = (XPComponent) component;

                System.out.println("HERO : LVL " + comp.getCurrentLevel() + "(" + comp.getCurrentXP() + "/" + (comp.getXPToNextLevel() + comp.getCurrentXP()) + ")");
            });

        if (Gdx.input.isKeyPressed(KeyboardConfig.HERO_KILL.get()))
            Game.getHero().flatMap(hero -> hero.getComponent(HealthComponent.class)).ifPresent(component -> {
                HealthComponent comp = (HealthComponent) component;

                comp.receiveHit(new Damage(
                    10,
                    DamageType.PHYSICAL,
                    null
                ));
            });
    }

    private KSData buildDataObject(PlayableComponent pc) {
        Entity e = pc.getEntity();

        VelocityComponent vc =
                (VelocityComponent)
                        e.getComponent(VelocityComponent.class)
                                .orElseThrow(PlayerSystem::missingVC);

        return new KSData(e, pc, vc);
    }

    private static MissingComponentException missingVC() {
        return new MissingComponentException("VelocityComponent");
    }
}

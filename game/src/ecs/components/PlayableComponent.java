package ecs.components;

import ecs.components.skill.Skill;
import ecs.entities.Entity;
import java.util.Optional;
import java.util.logging.Logger;
import logging.CustomLogLevel;

/**
 * This component is for the player character entity only. It should only be implemented by one
 * entity and mark this entity as the player character. This component stores data that is only
 * relevant for the player character. The PlayerSystems acts on the PlayableComponent.
 */
public class PlayableComponent extends Component {

    private boolean playable;
    private final Logger playableCompLogger = Logger.getLogger(this.getClass().getName());

    private Skill skillSlot1;
    private Skill skillSlot2;
    private Skill skillSlot3;

    /** {@inheritDoc} */
    public PlayableComponent(Entity entity) {
        super(entity);
        playable = true;
    }

    /**
     * @return the playable state
     */
    public boolean isPlayable() {
        playableCompLogger.log(
                CustomLogLevel.DEBUG,
                "Checking if entity '"
                        + entity.getClass().getSimpleName()
                        + "' is playable: "
                        + playable);
        return playable;
    }

    /**
     * @param playable set the playabale state
     */
    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    /**
     * @param skill skill that will be on the first skill slot
     */
    public void setSkillSlot1(Skill skill) {
        this.skillSlot1 = skill;
    }

    /**
     * @param skill skill that will be on the second skill slot
     */
    public void setSkillSlot2(Skill skill) {
        this.skillSlot2 = skill;
    }

    /**
     * @param skill skill that will be on the third skill slot
     */
    public void setSkillSlot3(Skill skill) {
        this.skillSlot3 = skill;
    }

    /**
     * @return skill on first skill slot
     */
    public Optional<Skill> getSkillSlot1() {
        return Optional.ofNullable(skillSlot1);
    }

    /**
     * @return skill on second skill slot
     */
    public Optional<Skill> getSkillSlot2() {
        return Optional.ofNullable(skillSlot2);
    }

    /**
     * @return skill on third skill slot
     */
    public Optional<Skill> getSkillSlot3() {
        return Optional.ofNullable(skillSlot3);
    }
}

package ecs.components.skill;

import ecs.entities.Entity;
import tools.Constants;

public class Skill {

    private final ISkillFunction skillFunction;
    private final int coolDownInFrames;
    private int currentCoolDownInFrames;
    private final String pathToTextureUI;

    /**
     * @param skillFunction Function of this skill.
     * @param coolDownInSeconds how long it takes for the skill to be used again.
     * @param pathToTextureUI the path to the textures of the skill.
     */
    public Skill(ISkillFunction skillFunction, float coolDownInSeconds, String pathToTextureUI) {
        this.skillFunction = skillFunction;
        this.coolDownInFrames = (int) (coolDownInSeconds * Constants.FRAME_RATE);
        this.currentCoolDownInFrames = 0;
        this.pathToTextureUI = pathToTextureUI;
    }

    /**
     * Execute the method of this skill
     *
     * @param entity entity which uses the skill
     */
    public void execute(Entity entity) {
        if (!isOnCoolDown()) {
            skillFunction.execute(entity);
            activateCoolDown();
        }
    }

    /**
     * @return true if cool down is not 0, else false
     */
    public boolean isOnCoolDown() {
        return currentCoolDownInFrames > 0;
    }

    /** activate cool down */
    public void activateCoolDown() {
        currentCoolDownInFrames = coolDownInFrames;
    }

    /** reduces the current cool down by frame */
    public void reduceCoolDown() {
        currentCoolDownInFrames = Math.max(0, --currentCoolDownInFrames);
    }

    public String getPathToTextureUI() {
        return pathToTextureUI;
    }
}

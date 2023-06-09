package ecs.components.skill;

import tools.Point;

public class FrostBoltSkill extends ImpairingProjectileSkill {

    public static final String pathToTextureUI = "skills/frostbolt/down/frostBolt_Down4.png";

    /**
     * This Skill is an ImpairingProjectileSkill It is a skill that can be shot at an enemy entity
     * to reduce its movement speed (velocity)
     *
     * @param targetSelection the point that is aimed for with the skill
     */
    public FrostBoltSkill(ITargetSelection targetSelection) {
        super("skills/frostbolt/", 0.5f, 0.04f, new Point(1, 1), targetSelection, 5f, 5);
    }
}

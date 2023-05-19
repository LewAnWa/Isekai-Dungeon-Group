package ecs.components.skill;

import tools.Point;

public class FrostBoltSkill extends ImpairingProjectileSkill {

    /**
     * This Skill is an ImpairingProjectileSkill It is a skill that can be shot at an enemy entity
     * to reduce its movement speed (velocity)
     *
     * @param targetSelection the point that is aimed for with the skill
     */
    public FrostBoltSkill(ITargetSelection targetSelection) {
        super("skills/frostbolt/", 0.5f, 0.04f, new Point(10, 10), targetSelection, 5f, 5);
    }
}

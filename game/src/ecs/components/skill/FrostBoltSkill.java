package ecs.components.skill;

import tools.Point;

public class FrostBoltSkill extends ImpairingProjectileSkill{
    public FrostBoltSkill(ITargetSelection targetSelection){
        super(
            "skills/frostbolt/",
            0.5f,
            0.04f,
             new Point(10,10),
            targetSelection,
            5f,
            5);
    }
}

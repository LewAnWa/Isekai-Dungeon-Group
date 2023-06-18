package configuration;

import com.badlogic.gdx.Input;
import configuration.values.ConfigIntValue;

@ConfigMap(path = {"keyboard"})
public class KeyboardConfig {

    // --------------- Hero Movement --------------- //
    public static final ConfigKey<Integer> MOVEMENT_UP =
            new ConfigKey<>(new String[] {"movement", "up"}, new ConfigIntValue(Input.Keys.W));
    public static final ConfigKey<Integer> MOVEMENT_DOWN =
            new ConfigKey<>(new String[] {"movement", "down"}, new ConfigIntValue(Input.Keys.S));
    public static final ConfigKey<Integer> MOVEMENT_LEFT =
            new ConfigKey<>(new String[] {"movement", "left"}, new ConfigIntValue(Input.Keys.A));
    public static final ConfigKey<Integer> MOVEMENT_RIGHT =
            new ConfigKey<>(new String[] {"movement", "right"}, new ConfigIntValue(Input.Keys.D));

    // --------------- Hero Skills --------------- //
    public static final ConfigKey<Integer> FIRST_SKILL =
        new ConfigKey<>(new String[] {"skill", "first"}, new ConfigIntValue(Input.Keys.Q));
    public static final ConfigKey<Integer> SECOND_SKILL =
        new ConfigKey<>(new String[] {"skill", "second"}, new ConfigIntValue(Input.Keys.R));
    public static final ConfigKey<Integer> THIRD_SKILL =
        new ConfigKey<>(new String[] {"skill", "third"}, new ConfigIntValue(Input.Keys.SPACE));

    // --------------- Interaction --------------- //
    public static final ConfigKey<Integer> TOGGLE_PAUSE =
        new ConfigKey<>(new String[] {"game", "pause"}, new ConfigIntValue(Input.Keys.ESCAPE));
    public static final ConfigKey<Integer> INVENTORY_OPEN =
            new ConfigKey<>(new String[] {"inventory", "open"}, new ConfigIntValue(Input.Keys.I));
    public static final ConfigKey<Integer> INTERACT_WORLD =
            new ConfigKey<>(new String[] {"interact", "world"}, new ConfigIntValue(Input.Keys.E));


    // --------------- DEBUG --------------- //
    public static final ConfigKey<Integer> HERO_ADD_XP =
            new ConfigKey<>(new String[] {"cheat", "XPADDER"}, new ConfigIntValue(Input.Keys.H));
    public static final ConfigKey<Integer> HERO_KILL =
            new ConfigKey<>(new String[] {"kill", "hero"}, new ConfigIntValue(Input.Keys.K));
}

package net.knarcraft.dropper.property;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A representation of possible arena game-modes
 */
public enum ArenaGameMode implements ConfigurationSerializable {

    /**
     * The default game-mode. Failing once throws the player out.
     */
    DEFAULT,

    /**
     * A game-mode where the player's directional buttons are inverted
     */
    INVERTED,

    /**
     * A game-mode which swaps between normal and inverted controls on a set schedule of a few seconds
     */
    RANDOM_INVERTED,
    ;

    /**
     * Tries to match the correct game-mode according to the given string
     *
     * @param gameMode <p>The game-mode string to match</p>
     * @return <p>The specified arena game-mode</p>
     */
    public static @NotNull ArenaGameMode matchGamemode(@NotNull String gameMode) {
        String sanitized = gameMode.trim().toLowerCase();
        if (sanitized.matches("(invert(ed)?|inverse)")) {
            return ArenaGameMode.INVERTED;
        } else if (sanitized.matches("rand(om)?")) {
            return ArenaGameMode.RANDOM_INVERTED;
        } else {
            return ArenaGameMode.DEFAULT;
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name());
        return data;
    }

    /**
     * Deserializes the arena game-mode specified by the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized arena game-mode</p>
     */
    @SuppressWarnings("unused")
    public static ArenaGameMode deserialize(Map<String, Object> data) {
        return ArenaGameMode.valueOf((String) data.get("name"));
    }

}

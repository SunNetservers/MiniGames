package net.knarcraft.dropper.arena.parkour;

import net.knarcraft.dropper.arena.ArenaGameMode;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A representation of possible arena game-modes
 */
public enum ParkourArenaGameMode implements ConfigurationSerializable, ArenaGameMode {

    /**
     * The default game-mode. Failing once throws the player out.
     */
    DEFAULT,
    ;

    /**
     * Tries to match the correct game-mode according to the given string
     *
     * @param gameMode <p>The game-mode string to match</p>
     * @return <p>The specified arena game-mode</p>
     */
    public static @NotNull ParkourArenaGameMode matchGamemode(@NotNull String gameMode) {
        return ParkourArenaGameMode.DEFAULT;
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
    public static ParkourArenaGameMode deserialize(Map<String, Object> data) {
        return ParkourArenaGameMode.valueOf((String) data.get("name"));
    }

}

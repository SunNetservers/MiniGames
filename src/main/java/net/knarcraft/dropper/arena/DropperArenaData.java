package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.container.SerializableUUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Data stored for an arena
 *
 * @param arenaId          <p>The id of the arena this data belongs to</p>
 * @param recordsRegistry  <p>The records belonging to the arena</p>
 * @param playersCompleted <p>A list of all player that have completed this arena</p>
 */
public record DropperArenaData(@NotNull UUID arenaId, @NotNull DropperArenaRecordsRegistry recordsRegistry,
                               @NotNull Set<SerializableUUID> playersCompleted) implements ConfigurationSerializable {

    /**
     * Instantiates a new dropper arena data object
     *
     * @param arenaId          <p>The id of the arena this data belongs to</p>
     * @param recordsRegistry  <p>The registry of this arena's records</p>
     * @param playersCompleted <p>The set of ids for players that have cleared this data's arena</p>
     */
    public DropperArenaData(@NotNull UUID arenaId, @NotNull DropperArenaRecordsRegistry recordsRegistry,
                            @NotNull Set<SerializableUUID> playersCompleted) {
        this.arenaId = arenaId;
        this.recordsRegistry = recordsRegistry;
        this.playersCompleted = new HashSet<>(playersCompleted);
    }

    /**
     * Gets whether the given player has cleared this arena
     *
     * @param player <p>The player to check</p>
     * @return <p>True if the player has cleared the arena this data belongs to</p>
     */
    public boolean hasNotCompleted(@NotNull Player player) {
        return !this.playersCompleted.contains(new SerializableUUID(player.getUniqueId()));
    }

    /**
     * Registers the given player as having completed this arena
     *
     * @param player <p>The player that completed this data's arena</p>
     */
    public boolean addCompleted(@NotNull Player player) {
        boolean added = this.playersCompleted.add(new SerializableUUID(player.getUniqueId()));
        // Persistently save the completion
        if (added) {
            Dropper.getInstance().getArenaHandler().saveData(this.arenaId);
        }
        return added;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("arenaId", new SerializableUUID(this.arenaId));
        data.put("recordsRegistry", this.recordsRegistry);
        data.put("playersCompleted", this.playersCompleted);
        return data;
    }

    /**
     * Deserializes a dropper arena data from the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized dropper arena data</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static @NotNull DropperArenaData deserialize(@NotNull Map<String, Object> data) {
        SerializableUUID serializableUUID = (SerializableUUID) data.get("arenaId");
        DropperArenaRecordsRegistry recordsRegistry = (DropperArenaRecordsRegistry) data.get("recordsRegistry");
        Set<SerializableUUID> playersCompleted = (Set<SerializableUUID>) data.get("playersCompleted");
        return new DropperArenaData(serializableUUID.uuid(), recordsRegistry, playersCompleted);
    }

}

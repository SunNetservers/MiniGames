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
 * @param recordRegistries <p>The records belonging to the arena</p>
 * @param playersCompleted <p>A list of all player that have completed this arena</p>
 */
public record DropperArenaData(@NotNull UUID arenaId,
                               @NotNull Map<ArenaGameMode, DropperArenaRecordsRegistry> recordRegistries,
                               @NotNull Map<ArenaGameMode, Set<UUID>> playersCompleted) implements ConfigurationSerializable {

    /**
     * Instantiates a new dropper arena data object
     *
     * @param arenaId          <p>The id of the arena this data belongs to</p>
     * @param recordRegistries <p>The registries of this arena's records</p>
     * @param playersCompleted <p>The set of the players that have cleared this arena for each game-mode</p>
     */
    public DropperArenaData(@NotNull UUID arenaId,
                            @NotNull Map<ArenaGameMode, DropperArenaRecordsRegistry> recordRegistries,
                            @NotNull Map<ArenaGameMode, Set<UUID>> playersCompleted) {
        this.arenaId = arenaId;
        this.recordRegistries = recordRegistries;
        this.playersCompleted = new HashMap<>(playersCompleted);
    }

    /**
     * Gets whether the given player has cleared this arena
     *
     * @param arenaGameMode <p>The game-mode to check for</p>
     * @param player        <p>The player to check</p>
     * @return <p>True if the player has cleared the arena this data belongs to</p>
     */
    public boolean hasNotCompleted(@NotNull ArenaGameMode arenaGameMode, @NotNull Player player) {
        return !this.playersCompleted.getOrDefault(arenaGameMode, new HashSet<>()).contains(player.getUniqueId());
    }

    /**
     * Registers the given player as having completed this arena
     *
     * @param player <p>The player that completed this data's arena</p>
     */
    public boolean addCompleted(@NotNull ArenaGameMode arenaGameMode, @NotNull Player player) {
        // Make sure to add an empty set to prevent a NullPointerException
        if (!this.playersCompleted.containsKey(arenaGameMode)) {
            this.playersCompleted.put(arenaGameMode, new HashSet<>());
        }

        boolean added = this.playersCompleted.get(arenaGameMode).add(player.getUniqueId());
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
        data.put("recordsRegistry", this.recordRegistries);

        // Convert normal UUIDs to serializable UUIDs
        Map<ArenaGameMode, Set<SerializableUUID>> serializablePlayersCompleted = new HashMap<>();
        for (ArenaGameMode arenaGameMode : this.playersCompleted.keySet()) {
            Set<SerializableUUID> playersCompleted = new HashSet<>();
            for (UUID playerCompleted : this.playersCompleted.get(arenaGameMode)) {
                playersCompleted.add(new SerializableUUID(playerCompleted));
            }
            serializablePlayersCompleted.put(arenaGameMode, playersCompleted);
        }
        data.put("playersCompleted", serializablePlayersCompleted);
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
        Map<ArenaGameMode, DropperArenaRecordsRegistry> recordsRegistry =
                (Map<ArenaGameMode, DropperArenaRecordsRegistry>) data.get("recordsRegistry");
        Map<ArenaGameMode, Set<SerializableUUID>> playersCompletedData =
                (Map<ArenaGameMode, Set<SerializableUUID>>) data.get("playersCompleted");

        if (recordsRegistry == null) {
            recordsRegistry = new HashMap<>();
        } else if (playersCompletedData == null) {
            playersCompletedData = new HashMap<>();
        }

        // Convert the serializable UUIDs to normal UUIDs
        Map<ArenaGameMode, Set<UUID>> allPlayersCompleted = new HashMap<>();
        for (ArenaGameMode arenaGameMode : playersCompletedData.keySet()) {
            Set<UUID> playersCompleted = new HashSet<>();
            for (SerializableUUID completedId : playersCompletedData.get(arenaGameMode)) {
                playersCompleted.add(completedId.uuid());
            }
            allPlayersCompleted.put(arenaGameMode, playersCompleted);

            if (!recordsRegistry.containsKey(arenaGameMode) || recordsRegistry.get(arenaGameMode) == null) {
                recordsRegistry.put(arenaGameMode, new DropperArenaRecordsRegistry(serializableUUID.uuid()));
            }
        }
        return new DropperArenaData(serializableUUID.uuid(), recordsRegistry, allPlayersCompleted);
    }

}

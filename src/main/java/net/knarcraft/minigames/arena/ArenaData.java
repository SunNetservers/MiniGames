package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.container.SerializableContainer;
import net.knarcraft.minigames.container.SerializableUUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static net.knarcraft.minigames.util.SerializableConverter.makeSerializable;

/**
 * An interface describing generic arena data
 */
public abstract class ArenaData implements ConfigurationSerializable {

    protected final @NotNull UUID arenaId;
    private final @NotNull Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries;
    private final @NotNull Map<ArenaGameMode, Set<UUID>> playersCompleted;

    /**
     * Instantiates arena data
     *
     * @param arenaId          <p>The id of the arena this data belongs to</p>
     * @param recordRegistries <p>The registry storing records for this arena</p>
     * @param playersCompleted <p>The players that have completed this arena</p>
     */
    public ArenaData(@NotNull UUID arenaId, @NotNull Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries,
                     @NotNull Map<ArenaGameMode, Set<UUID>> playersCompleted) {
        this.arenaId = arenaId;
        this.recordRegistries = recordRegistries;
        this.playersCompleted = playersCompleted;
    }

    /**
     * Gets the id of this arena
     *
     * @return <p>The id of this arena</p>
     */
    public @NotNull UUID getArenaId() {
        return this.arenaId;
    }

    /**
     * Gets all record registries
     *
     * @return <p>All record registries</p>
     */
    public @NotNull Map<ArenaGameMode, ArenaRecordsRegistry> getRecordRegistries() {
        return new HashMap<>(this.recordRegistries);
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
     * @param arenaGameMode <p>The game-mode the player completed</p>
     * @param player        <p>The player that completed this data's arena</p>
     */
    public boolean setCompleted(@NotNull ArenaGameMode arenaGameMode, @NotNull Player player) {
        // Make sure to add an empty set to prevent a NullPointerException
        if (!this.playersCompleted.containsKey(arenaGameMode)) {
            this.playersCompleted.put(arenaGameMode, new HashSet<>());
        }

        boolean added = this.playersCompleted.get(arenaGameMode).add(player.getUniqueId());
        // Persistently save the completion
        if (added) {
            saveData();
        }
        return added;
    }

    /**
     * Saves this data to disk
     */
    public abstract void saveData();

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("arenaId", new SerializableUUID(this.arenaId));
        data.put("recordsRegistry", this.recordRegistries);

        // Convert normal UUIDs to serializable UUIDs
        Map<ArenaGameMode, Set<SerializableContainer<UUID>>> serializablePlayersCompleted = new HashMap<>();
        makeSerializable(this.playersCompleted, serializablePlayersCompleted, new SerializableUUID(null));
        data.put("playersCompleted", serializablePlayersCompleted);
        return data;
    }

}

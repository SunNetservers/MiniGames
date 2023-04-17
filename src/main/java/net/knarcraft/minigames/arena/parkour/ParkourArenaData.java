package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaData;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.container.SerializableContainer;
import net.knarcraft.minigames.container.SerializableUUID;
import net.knarcraft.minigames.util.SerializableConverter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Data stored for an arena
 */
public class ParkourArenaData extends ArenaData {

    /**
     * Instantiates a new parkour arena data object
     *
     * @param arenaId          <p>The id of the arena this data belongs to</p>
     * @param recordRegistries <p>The registry of this arena's records</p>
     * @param playersCompleted <p>The set of the players that have cleared this arena</p>
     */
    public ParkourArenaData(@NotNull UUID arenaId,
                            @NotNull Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries,
                            @NotNull Map<ArenaGameMode, Set<UUID>> playersCompleted) {
        super(arenaId, recordRegistries, playersCompleted);
    }

    @Override
    public void saveData() {
        MiniGames.getInstance().getParkourArenaHandler().saveData(this.arenaId);
    }

    /**
     * Deserializes a parkour arena data from the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized parkour arena data</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static @NotNull ParkourArenaData deserialize(@NotNull Map<String, Object> data) {
        SerializableUUID serializableUUID = (SerializableUUID) data.get("arenaId");
        Map<ArenaGameMode, ArenaRecordsRegistry> recordsRegistry =
                (Map<ArenaGameMode, ArenaRecordsRegistry>) data.get("recordsRegistry");
        Map<ArenaGameMode, Set<SerializableContainer<UUID>>> playersCompletedData =
                (Map<ArenaGameMode, Set<SerializableContainer<UUID>>>) data.get("playersCompleted");

        if (recordsRegistry == null) {
            recordsRegistry = new HashMap<>();
        } else if (playersCompletedData == null) {
            playersCompletedData = new HashMap<>();
        }

        // Convert the serializable UUIDs to normal UUIDs
        Map<ArenaGameMode, Set<UUID>> allPlayersCompleted = new HashMap<>();
        SerializableConverter.getRawValue(playersCompletedData, allPlayersCompleted);

        for (ArenaGameMode arenaGameMode : playersCompletedData.keySet()) {
            if (!recordsRegistry.containsKey(arenaGameMode) || recordsRegistry.get(arenaGameMode) == null) {
                recordsRegistry.put(arenaGameMode, new ParkourArenaRecordsRegistry(serializableUUID.getRawValue()));
            }
        }
        return new ParkourArenaData(serializableUUID.getRawValue(), recordsRegistry, allPlayersCompleted);
    }

}

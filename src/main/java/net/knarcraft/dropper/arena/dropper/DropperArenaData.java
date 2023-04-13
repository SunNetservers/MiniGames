package net.knarcraft.dropper.arena.dropper;

import net.knarcraft.dropper.MiniGames;
import net.knarcraft.dropper.arena.ArenaData;
import net.knarcraft.dropper.arena.ArenaGameMode;
import net.knarcraft.dropper.arena.ArenaRecordsRegistry;
import net.knarcraft.dropper.container.SerializableUUID;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Data stored for an arena
 */
public class DropperArenaData extends ArenaData {

    /**
     * Instantiates a new dropper arena data object
     *
     * @param arenaId          <p>The id of the arena this data belongs to</p>
     * @param recordRegistries <p>The registries of this arena's records</p>
     * @param playersCompleted <p>The set of the players that have cleared this arena for each game-mode</p>
     */
    public DropperArenaData(@NotNull UUID arenaId,
                            @NotNull Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries,
                            @NotNull Map<ArenaGameMode, Set<UUID>> playersCompleted) {
        super(arenaId, recordRegistries, playersCompleted);
    }

    @Override
    public void saveData() {
        MiniGames.getInstance().getDropperArenaHandler().saveData(this.arenaId);
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
        Map<ArenaGameMode, ArenaRecordsRegistry> recordsRegistry =
                (Map<ArenaGameMode, ArenaRecordsRegistry>) data.get("recordsRegistry");
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

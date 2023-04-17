package net.knarcraft.minigames.arena.dropper;

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
                recordsRegistry.put(arenaGameMode, new DropperArenaRecordsRegistry(serializableUUID.getRawValue()));
            }
        }
        return new DropperArenaData(serializableUUID.getRawValue(), recordsRegistry, allPlayersCompleted);
    }

}

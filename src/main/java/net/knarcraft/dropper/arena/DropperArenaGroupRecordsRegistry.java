package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.property.ArenaGameMode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DropperArenaGroupRecordsRegistry {

    private final Map<UUID, Map<UUID, Map<ArenaGameMode, Integer>>> deathRecords = new HashMap<>();
    private final Map<UUID, Map<UUID, Map<ArenaGameMode, Long>>> timeRecords = new HashMap<>();
    private int numberOfArenas;

    public void initialize(DropperArenaGroup group) {
        DropperArenaHandler arenaHandler = Dropper.getInstance().getArenaHandler();
        // The number of arenas is required to know if a player has finished all the arenas
        numberOfArenas = group.getArenas().size();
        for (UUID arenaId : group.getArenas()) {
            DropperArena arena = arenaHandler.getArena(arenaId);
            if (arena == null) {
                continue;
            }

            // Load all existing records
            @NotNull Map<ArenaGameMode, DropperArenaRecordsRegistry> registries = arena.getData().recordRegistries();
            for (Map.Entry<ArenaGameMode, DropperArenaRecordsRegistry> entry : registries.entrySet()) {
                for (ArenaRecord<Integer> record : entry.getValue().getLeastDeathsRecords()) {
                    loadRecords(deathRecords, record, arenaId, entry.getKey());
                }
                for (ArenaRecord<Long> record : entry.getValue().getShortestTimeMilliSecondsRecords()) {
                    loadRecords(timeRecords, record, arenaId, entry.getKey());
                }
            }
        }
    }

    public Set<ArenaRecord<Integer>> getCombinedDeathRecords(ArenaGameMode gameMode) {
        return getCombinedRecords(gameMode, deathRecords);
    }

    public Set<ArenaRecord<Long>> getCombinedTimeRecords(ArenaGameMode gameMode) {
        return getCombinedRecords(gameMode, timeRecords);
    }

    private <K extends Comparable<K>> Set<ArenaRecord<K>> getCombinedRecords(ArenaGameMode gameMode,
                                                                             Map<UUID, Map<UUID, Map<ArenaGameMode, K>>> rawRecords) {
        Map<UUID, ArenaRecord<K>> combinedRecords = new HashMap<>();
        for (Map.Entry<UUID, Map<UUID, Map<ArenaGameMode, K>>> entry : rawRecords.entrySet()) {
            // Only get records for players that have played all arenas
            if (entry.getValue().size() != numberOfArenas) {
                continue;
            }

            // Combine all records to get a "best of all" value
            for (Map<ArenaGameMode, K> records : entry.getValue().values()) {
                K value = records.get(gameMode);
                if (!combinedRecords.containsKey(entry.getKey())) {
                    combinedRecords.put(entry.getKey(), new ArenaRecord<>(entry.getKey(), value));
                } else {
                    //TODO: Find the best way to combine objects of type K
                    //combinedRecords.put(entry.getKey(), combinedRecords.get(entry.getKey()).combine(value));
                }
            }
        }
        return new HashSet<>(combinedRecords.values());
    }

    /**
     * Loads a record
     *
     * @param combinedRecords <p>The map of combined records to update</p>
     * @param record          <p>The record to add</p>
     * @param arenaId         <p>The arena the record belongs to</p>
     * @param arenaGameMode   <p>The game-mode the record was achieved for</p>
     * @param <K>             <p>The type of the record's value</p>
     */
    private <K extends Comparable<K>> void loadRecords(
            Map<UUID, Map<UUID, Map<ArenaGameMode, K>>> combinedRecords,
            ArenaRecord<K> record, UUID arenaId, ArenaGameMode arenaGameMode) {
        if (!combinedRecords.containsKey(record.userId())) {
            combinedRecords.put(record.userId(), new HashMap<>());
        }
        if (!combinedRecords.get(record.userId()).containsKey(arenaId)) {
            combinedRecords.get(record.userId()).put(arenaId, new HashMap<>());
        }
        combinedRecords.get(record.userId()).get(arenaId).put(arenaGameMode, record.record());
    }

}

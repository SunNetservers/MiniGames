package net.knarcraft.minigames.util;

import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaGroup;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.arena.record.ArenaRecord;
import net.knarcraft.minigames.arena.record.SummableArenaRecord;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * A helper class for getting combined record data for a dropper group
 */
public final class GroupRecordHelper {

    private GroupRecordHelper() {

    }

    /**
     * Gets the combined least-death records for the given group and game-mode
     *
     * @param group        <p>The group to get records from</p>
     * @param gameMode     <p>The game-mode to get records for</p>
     * @param arenaHandler <p>The handler to get arenas from</p>
     * @return <p>The combined death records</p>
     */
    public static @NotNull Set<ArenaRecord<Integer>> getCombinedDeaths(@NotNull ArenaGroup<?, ?> group,
                                                                       @NotNull ArenaGameMode gameMode,
                                                                       @NotNull ArenaHandler<?, ?> arenaHandler) {
        Map<UUID, SummableArenaRecord<Integer>> records = new HashMap<>();
        @NotNull BiFunction<Arena, ArenaGameMode, Set<SummableArenaRecord<Integer>>> recordSupplier =
                (arena, aGameMode) -> arena.getData().getRecordRegistries().get(gameMode).getLeastDeathsRecords();

        return getCombined(group, gameMode, records, recordSupplier, arenaHandler);
    }

    /**
     * Gets the combined least-time records for the given group and game-mode
     *
     * @param group        <p>The group to get records from</p>
     * @param gameMode     <p>The game-mode to get records for</p>
     * @param arenaHandler <p>The handler to get arenas from</p>
     * @return <p>The combined least-time records</p>
     */
    public static @NotNull Set<ArenaRecord<Long>> getCombinedTime(@NotNull ArenaGroup<?, ?> group,
                                                                  @NotNull ArenaGameMode gameMode,
                                                                  @NotNull ArenaHandler<?, ?> arenaHandler) {
        Map<UUID, SummableArenaRecord<Long>> records = new HashMap<>();
        @NotNull BiFunction<Arena, ArenaGameMode, Set<SummableArenaRecord<Long>>> recordSupplier =
                (arena, aGameMode) -> arena.getData().getRecordRegistries().get(gameMode).getShortestTimeMilliSecondsRecords();

        return getCombined(group, gameMode, records, recordSupplier, arenaHandler);
    }

    /**
     * Gets the combined records for a group and game-mode
     *
     * @param group          <p>The group to get combined records for</p>
     * @param gameMode       <p>The game-mode to get records for</p>
     * @param records        <p>The map to store the combined records to</p>
     * @param recordSupplier <p>The function that supplies records of this type</p>
     * @param arenaHandler   <p>The handler to get arenas from</p>
     * @param <K>            <p>The type of the records to combine</p>
     * @return <p>The combined records</p>
     */
    private static <K extends Comparable<K>> @NotNull Set<ArenaRecord<K>> getCombined(@NotNull ArenaGroup<?, ?> group,
                                                                                      @NotNull ArenaGameMode gameMode,
                                                                                      @NotNull Map<UUID,
                                                                                              SummableArenaRecord<K>> records,
                                                                                      @NotNull BiFunction<Arena,
                                                                                              ArenaGameMode,
                                                                                              Set<SummableArenaRecord<K>>> recordSupplier,
                                                                                      @NotNull ArenaHandler<?, ?> arenaHandler) {
        // Get all arenas in the group
        Set<Arena> arenas = getArenas(arenaHandler, group);

        // Calculate the combined records
        Map<UUID, Integer> recordsFound = new HashMap<>();
        combineRecords(arenas, gameMode, records, recordsFound, recordSupplier);

        // Filter out any players that haven't played through all arenas
        filterRecords(records, recordsFound, arenas.size());

        return new HashSet<>(records.values());
    }

    /**
     * Filters away any records that belong to users who haven't set records for all arenas in the group
     *
     * @param records      <p>The records to filter</p>
     * @param recordsFound <p>The map of how many records have been registered for each user</p>
     * @param arenas       <p>The number of arenas in the group</p>
     * @param <K>          <p>The type of the given records</p>
     */
    private static <K extends Comparable<K>> void filterRecords(@NotNull Map<UUID, SummableArenaRecord<K>> records,
                                                                @NotNull Map<UUID, Integer> recordsFound, int arenas) {
        for (UUID userId : recordsFound.keySet()) {
            if (recordsFound.get(userId) != arenas) {
                records.remove(userId);
            }
        }
    }

    /**
     * Gets all arenas in the given group
     *
     * @param arenaHandler <p>The arena handler to get arenas from</p>
     * @param group        <p>The group to get arenas for</p>
     * @return <p>The arenas found in the group</p>
     */
    private static @NotNull Set<Arena> getArenas(@NotNull ArenaHandler<?, ?> arenaHandler,
                                                 @NotNull ArenaGroup<?, ?> group) {
        // Get all arenas in the group
        Set<Arena> arenas = new HashSet<>();
        for (UUID arenaId : group.getArenas()) {
            Arena arena = arenaHandler.getArena(arenaId);
            if (arena != null) {
                arenas.add(arena);
            }
        }
        return arenas;
    }

    /**
     * Combines arena records
     *
     * @param arenas          <p>The arenas whose records should be combined</p>
     * @param gameMode        <p>The game-mode to combine records for</p>
     * @param combinedRecords <p>The map to store the combined records to</p>
     * @param recordsFound    <p>The map used to store the number of records registered for each player</p>
     * @param recordSupplier  <p>The function that supplies record data of this type</p>
     * @param <K>             <p>The type of record to combine</p>
     */
    private static <K extends Comparable<K>> void combineRecords(@NotNull Set<Arena> arenas,
                                                                 @NotNull ArenaGameMode gameMode,
                                                                 @NotNull Map<UUID,
                                                                         SummableArenaRecord<K>> combinedRecords,
                                                                 @NotNull Map<UUID, Integer> recordsFound,
                                                                 @NotNull BiFunction<Arena, ArenaGameMode,
                                                                         Set<SummableArenaRecord<K>>> recordSupplier) {
        for (Arena arena : arenas) {
            Set<SummableArenaRecord<K>> existingRecords = recordSupplier.apply(arena, gameMode);
            // For each arena's record registry, calculate the combined records
            for (SummableArenaRecord<K> value : existingRecords) {
                if (value == null) {
                    continue;
                }
                UUID userId = value.getUserId();

                // Bump the number of records found for the user
                if (!recordsFound.containsKey(userId)) {
                    recordsFound.put(userId, 0);
                }
                recordsFound.put(userId, recordsFound.get(userId) + 1);

                // Put the value, or the sum with the existing value, into combined records
                if (!combinedRecords.containsKey(userId)) {
                    combinedRecords.put(value.getUserId(), value);
                } else {
                    combinedRecords.put(userId, combinedRecords.get(userId).sum(value.getRecord()));
                }
            }
        }
    }

}

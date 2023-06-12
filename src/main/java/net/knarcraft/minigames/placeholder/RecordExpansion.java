package net.knarcraft.minigames.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaGroup;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.arena.record.ArenaRecord;
import net.knarcraft.minigames.placeholder.parsing.InfoType;
import net.knarcraft.minigames.placeholder.parsing.SelectionType;
import net.knarcraft.minigames.property.RecordType;
import net.knarcraft.minigames.util.GroupRecordHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * A placeholder expansion for parkour record placeholders
 */
public abstract class RecordExpansion extends PlaceholderExpansion {

    private final ArenaHandler<?, ?> arenaHandler;
    private final Map<UUID, Set<GroupRecordCache<Integer>>> groupRecordDeathsCache;
    private final Map<UUID, Set<GroupRecordCache<Long>>> groupRecordTimeCache;

    /**
     * Initializes a new record expansion
     */
    public RecordExpansion(ArenaHandler<?, ?> arenaHandler) {
        this.groupRecordDeathsCache = new HashMap<>();
        this.groupRecordTimeCache = new HashMap<>();
        this.arenaHandler = arenaHandler;
    }

    @Override
    public String getAuthor() {
        return "EpicKnarvik97";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String parameters) {
        String[] parts = parameters.split("_");
        // Record is used as the prefix for all record placeholders in case more placeholder types are added
        if (parts.length < 7 || !parts[0].equals("record")) {
            return parameters;
        }
        RecordType recordType = RecordType.getFromString(parts[1]);
        ArenaGameMode gameMode = parseGameMode(parts[2]);
        SelectionType selectionType = SelectionType.getFromString(parts[3]);
        String identifier = parts[4];
        int recordNumber;
        try {
            recordNumber = Integer.parseInt(parts[5]) - 1;
        } catch (NumberFormatException exception) {
            MiniGames.log(Level.WARNING, "Invalid placeholder given. " + parts[5] +
                    " supplied instead of record placement.");
            return parameters;
        }
        InfoType infoType = InfoType.getFromString(parts[6]);

        if (recordType == null || infoType == null) {
            return parameters;
        }

        String info = null;
        if (selectionType == SelectionType.GROUP) {
            info = getGroupRecord(arenaHandler, identifier, gameMode, recordType, recordNumber, infoType);
        } else if (selectionType == SelectionType.ARENA) {
            info = getArenaRecord(arenaHandler, identifier, gameMode, recordType, recordNumber, infoType);
        }

        return Objects.requireNonNullElse(info, parameters);
    }

    /**
     * Parses the game-mode specified in the given string
     *
     * @param gameMode <p>The game-mode to parse</p>
     * @return <p>The parsed game-mode</p>
     */
    protected abstract @NotNull ArenaGameMode parseGameMode(@NotNull String gameMode);

    /**
     * Clears all record caches
     */
    public void clearCaches() {
        this.groupRecordDeathsCache.clear();
        this.groupRecordTimeCache.clear();
    }

    /**
     * Gets a piece of record information from an arena group
     *
     * @param arenaHandler <p>The arena handler to get the group from</p>
     * @param identifier   <p>The identifier (name/uuid) selecting the group</p>
     * @param gameMode     <p>The game-mode to get a record for</p>
     * @param recordType   <p>The type of record to get</p>
     * @param recordNumber <p>The placing of the record to get (1st place, 2nd place, etc.)</p>
     * @param infoType     <p>The type of info (player, value, combined) to get</p>
     * @return <p>The selected information about the record, or null if not found</p>
     */
    private @Nullable String getGroupRecord(@NotNull ArenaHandler<?, ?> arenaHandler, @NotNull String identifier,
                                            @NotNull ArenaGameMode gameMode, @NotNull RecordType recordType,
                                            int recordNumber, @NotNull InfoType infoType) {
        // Allow specifying the group UUID or the arena name
        ArenaGroup<?, ?> group;
        try {
            group = arenaHandler.getGroup(UUID.fromString(identifier));
        } catch (IllegalArgumentException exception) {
            group = arenaHandler.getGroup(identifier);
        }
        if (group == null) {
            return null;
        }

        ArenaRecord<?> record;
        if (recordType == RecordType.DEATHS) {
            record = getGroupDeathRecord(group, gameMode, recordNumber, arenaHandler);
        } else {
            record = getGroupTimeRecord(group, gameMode, recordNumber, arenaHandler);
        }

        // If a record number is not found, leave it blank, so it looks neat
        if (record == null) {
            return "";
        }

        return getRecordData(infoType, record);
    }

    /**
     * Gets a time record from a group, using the cache if possible
     *
     * @param group        <p>The group to get the record from</p>
     * @param gameMode     <p>The game-mode to get the record from</p>
     * @param recordNumber <p>The placing of the record to get (1st place, 2nd place, etc.)</p>
     * @param arenaHandler <p>The handler to get arenas from</p>
     * @return <p>The record, or null if not found</p>
     */
    private @Nullable ArenaRecord<?> getGroupTimeRecord(@NotNull ArenaGroup<?, ?> group,
                                                        @NotNull ArenaGameMode gameMode, int recordNumber,
                                                        @NotNull ArenaHandler<?, ?> arenaHandler) {
        return getCachedGroupRecord(group, gameMode, RecordType.TIME, recordNumber, groupRecordTimeCache,
                () -> GroupRecordHelper.getCombinedTime(group, gameMode, arenaHandler));
    }

    /**
     * Gets a death record from a group, using the cache if possible
     *
     * @param group        <p>The group to get the record from</p>
     * @param gameMode     <p>The game-mode to get the record from</p>
     * @param recordNumber <p>The placing of the record to get (1st place, 2nd place, etc.)</p>
     * @param arenaHandler <p>The handler to get arenas from</p>
     * @return <p>The record, or null if not found</p>
     */
    private @Nullable ArenaRecord<?> getGroupDeathRecord(@NotNull ArenaGroup<?, ?> group,
                                                         @NotNull ArenaGameMode gameMode, int recordNumber,
                                                         @NotNull ArenaHandler<?, ?> arenaHandler) {
        return getCachedGroupRecord(group, gameMode, RecordType.DEATHS, recordNumber, groupRecordDeathsCache,
                () -> GroupRecordHelper.getCombinedDeaths(group, gameMode, arenaHandler));
    }

    /**
     * Gets a group record, fetching from a cache if possible
     *
     * @param group          <p>The group to get the record for</p>
     * @param gameMode       <p>The game-mode to get the record for</p>
     * @param recordType     <p>The type of record to get</p>
     * @param recordNumber   <p>The placing of the record to get (1st place, 2nd place, etc.)</p>
     * @param caches         <p>The caches to use for looking for and saving the record</p>
     * @param recordProvider <p>The provider of records if the cache cannot provide the record</p>
     * @param <K>            <p>The type of the provided records</p>
     * @return <p>The specified record, or null if not found</p>
     */
    private <K extends Comparable<K>> @Nullable ArenaRecord<?> getCachedGroupRecord(@NotNull ArenaGroup<?, ?> group,
                                                                                    @NotNull ArenaGameMode gameMode,
                                                                                    @NotNull RecordType recordType,
                                                                                    int recordNumber,
                                                                                    @NotNull Map<UUID, Set<GroupRecordCache<K>>> caches,
                                                                                    @NotNull Supplier<Set<ArenaRecord<K>>> recordProvider) {
        UUID groupId = group.getGroupId();
        if (!caches.containsKey(groupId)) {
            caches.put(groupId, new HashSet<>());
        }

        Set<GroupRecordCache<K>> existingCaches = caches.get(groupId);
        Set<GroupRecordCache<K>> expired = new HashSet<>();
        Set<ArenaRecord<K>> cachedRecords = null;

        for (GroupRecordCache<K> cache : existingCaches) {
            // Expire caches after 30 seconds
            if (System.currentTimeMillis() - cache.createdTime() > 30000) {
                expired.add(cache);
            }
            // If of the correct type, and not expired, use the cache
            if (cache.gameMode() == gameMode && cache.recordType() == recordType) {
                cachedRecords = cache.records();
                break;
            }
        }
        existingCaches.removeAll(expired);

        // If not found, generate and cache the specified record
        if (cachedRecords == null) {
            cachedRecords = recordProvider.get();
            existingCaches.add(new GroupRecordCache<>(gameMode, recordType, cachedRecords, System.currentTimeMillis()));
        }
        return getRecord(cachedRecords, recordNumber);
    }

    /**
     * Gets a piece of record information from an arena
     *
     * @param arenaHandler <p>The arena handler to get the arena from</p>
     * @param identifier   <p>The identifier (name/uuid) selecting the arena</p>
     * @param gameMode     <p>The game-mode to get a record for</p>
     * @param recordType   <p>The type of record to get</p>
     * @param recordNumber <p>The placing of the record to get (1st place, 2nd place, etc.)</p>
     * @param infoType     <p>The type of info (player, value, combined) to get</p>
     * @return <p>The selected information about the record, or null if not found</p>
     */
    private @Nullable String getArenaRecord(@NotNull ArenaHandler<?, ?> arenaHandler, @NotNull String identifier,
                                            @NotNull ArenaGameMode gameMode, @NotNull RecordType recordType,
                                            int recordNumber, @NotNull InfoType infoType) {
        // Allow specifying the arena UUID or the arena name
        Arena arena;
        try {
            arena = arenaHandler.getArena(UUID.fromString(identifier));
        } catch (IllegalArgumentException exception) {
            arena = arenaHandler.getArena(identifier);
        }
        if (arena == null) {
            return null;
        }
        @NotNull Map<ArenaGameMode, ArenaRecordsRegistry> registries = arena.getData().getRecordRegistries();
        ArenaRecordsRegistry recordsRegistry = registries.get(gameMode);

        ArenaRecord<?> record = getRecord(recordsRegistry, recordType, recordNumber);

        // If a record number is not found, leave it blank, so it looks neat
        if (record == null) {
            return "";
        }

        return getRecordData(infoType, record);
    }

    /**
     * Gets the specified record
     *
     * @param recordsRegistry <p>The records registry to get the record from</p>
     * @param recordType      <p>The type of record to get</p>
     * @param recordNumber    <p>The placing of the record to get (1st place, 2nd place, etc.)</p>
     * @return <p>The record, or null if not found</p>
     */
    private @Nullable ArenaRecord<?> getRecord(@NotNull ArenaRecordsRegistry recordsRegistry,
                                               @NotNull RecordType recordType, int recordNumber) {
        return switch (recordType) {
            case TIME -> getRecord(new HashSet<>(recordsRegistry.getShortestTimeMilliSecondsRecords()), recordNumber);
            case DEATHS -> getRecord(new HashSet<>(recordsRegistry.getLeastDeathsRecords()), recordNumber);
        };
    }

    /**
     * Gets the record at the given index
     *
     * @param records <p>The records to search through</p>
     * @param index   <p>The index of the record to get</p>
     * @param <K>     <p>The type of record in the record list</p>
     * @return <p>The record, or null if index is out of bounds</p>
     */
    private <K extends Comparable<K>> @Nullable ArenaRecord<K> getRecord(Set<ArenaRecord<K>> records, int index) {
        List<ArenaRecord<K>> sorted = getSortedRecords(records);
        if (index < sorted.size() && index >= 0) {
            return sorted.get(index);
        } else {
            return null;
        }
    }

    /**
     * Gets a piece of data from a record as a string
     *
     * @param infoType    <p>The type of info to get data for</p>
     * @param arenaRecord <p>The record to get the data from</p>
     * @return <p>The requested data as a string, or null</p>
     */
    private String getRecordData(@NotNull InfoType infoType, @NotNull ArenaRecord<?> arenaRecord) {
        return switch (infoType) {
            case PLAYER -> getPlayerName(arenaRecord.getUserId());
            case VALUE -> arenaRecord.getRecord().toString();
            case COMBINED -> getPlayerName(arenaRecord.getUserId()) + ": " + arenaRecord.getRecord().toString();
        };
    }

    /**
     * Gets the given set of records as a sorted list
     *
     * @param recordSet <p>The set of records to sort</p>
     * @param <K>       <p>The type of the records</p>
     * @return <p>The sorted records</p>
     */
    private <K extends Comparable<K>> @NotNull List<ArenaRecord<K>> getSortedRecords(
            @NotNull Set<ArenaRecord<K>> recordSet) {
        List<ArenaRecord<K>> records = new ArrayList<>(recordSet);
        Collections.sort(records);
        return records;
    }

    /**
     * Gets the name of a player, given the player's UUID
     *
     * @param playerId <p>The id of the player to get the name for</p>
     * @return <p>The name of the player, or a string representation of the UUID if not found</p>
     */
    private String getPlayerName(@NotNull UUID playerId) {
        return Bukkit.getOfflinePlayer(playerId).getName();
    }

}

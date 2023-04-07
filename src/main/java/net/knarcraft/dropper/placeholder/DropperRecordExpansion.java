package net.knarcraft.dropper.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.arena.DropperArenaGroup;
import net.knarcraft.dropper.arena.DropperArenaHandler;
import net.knarcraft.dropper.arena.DropperArenaRecordsRegistry;
import net.knarcraft.dropper.arena.record.ArenaRecord;
import net.knarcraft.dropper.placeholder.parsing.InfoType;
import net.knarcraft.dropper.placeholder.parsing.RecordType;
import net.knarcraft.dropper.placeholder.parsing.SelectionType;
import net.knarcraft.dropper.property.ArenaGameMode;
import net.knarcraft.dropper.util.DropperGroupRecordHelper;
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

/**
 * A placeholder expansion for dropper record placeholders
 */
public class DropperRecordExpansion extends PlaceholderExpansion {

    private final Dropper plugin;
    private final Map<UUID, Set<GroupRecordCache<Integer>>> groupRecordDeathsCache;
    private final Map<UUID, Set<GroupRecordCache<Long>>> groupRecordTimeCache;

    /**
     * Initializes a new record expansion
     *
     * @param plugin <p>A reference to the dropper plugin</p>
     */
    public DropperRecordExpansion(Dropper plugin) {
        this.plugin = plugin;
        this.groupRecordDeathsCache = new HashMap<>();
        this.groupRecordTimeCache = new HashMap<>();
    }

    @Override
    public String getIdentifier() {
        return "dropper";
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
        ArenaGameMode gameMode = ArenaGameMode.matchGamemode(parts[2]);
        SelectionType selectionType = SelectionType.getFromString(parts[3]);
        String identifier = parts[4];
        int recordNumber = Integer.parseInt(parts[5]) - 1;
        InfoType infoType = InfoType.getFromString(parts[6]);

        if (recordType == null || infoType == null) {
            return parameters;
        }

        String info = null;
        DropperArenaHandler arenaHandler = plugin.getArenaHandler();
        if (selectionType == SelectionType.GROUP) {
            info = getGroupRecord(arenaHandler, identifier, gameMode, recordType, recordNumber, infoType);
        } else if (selectionType == SelectionType.ARENA) {
            info = getArenaRecord(arenaHandler, identifier, gameMode, recordType, recordNumber, infoType);
        }

        return Objects.requireNonNullElse(info, parameters);
    }

    /**
     * Gets a piece of record information from a dropper arena group
     *
     * @param arenaHandler <p>The arena handler to get the group from</p>
     * @param identifier   <p>The identifier (name/uuid) selecting the group</p>
     * @param gameMode     <p>The game-mode to get a record for</p>
     * @param recordType   <p>The type of record to get</p>
     * @param recordNumber <p>The placing of the record to get (1st place, 2nd place, etc.)</p>
     * @param infoType     <p>The type of info (player, value, combined) to get</p>
     * @return <p>The selected information about the record, or null if not found</p>
     */
    private @Nullable String getGroupRecord(@NotNull DropperArenaHandler arenaHandler, @NotNull String identifier,
                                            @NotNull ArenaGameMode gameMode, @NotNull RecordType recordType,
                                            int recordNumber, @NotNull InfoType infoType) {
        // Allow specifying the group UUID or the arena name
        DropperArenaGroup group;
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
            record = getDeathRecords(group, gameMode, recordNumber);
        } else {
            record = getTimeRecords(group, gameMode, recordNumber);
        }

        // If a record number is not found, leave it blank, so it looks neat
        if (record == null) {
            return "";
        }

        return getRecordData(infoType, record);
    }

    private ArenaRecord<?> getTimeRecords(DropperArenaGroup group, ArenaGameMode gameMode, int recordNumber) {
        return getCachedGroupRecord(group, gameMode, RecordType.TIME, recordNumber, groupRecordTimeCache,
                () -> DropperGroupRecordHelper.getCombinedTime(group, gameMode));
    }

    private ArenaRecord<?> getDeathRecords(DropperArenaGroup group, ArenaGameMode gameMode, int recordNumber) {
        return getCachedGroupRecord(group, gameMode, RecordType.DEATHS, recordNumber, groupRecordDeathsCache,
                () -> DropperGroupRecordHelper.getCombinedDeaths(group, gameMode));
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
    private <K extends Comparable<K>> @Nullable ArenaRecord<?> getCachedGroupRecord(@NotNull DropperArenaGroup group,
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
     * Gets a piece of record information from a dropper arena
     *
     * @param arenaHandler <p>The arena handler to get the arena from</p>
     * @param identifier   <p>The identifier (name/uuid) selecting the arena</p>
     * @param gameMode     <p>The game-mode to get a record for</p>
     * @param recordType   <p>The type of record to get</p>
     * @param recordNumber <p>The placing of the record to get (1st place, 2nd place, etc.)</p>
     * @param infoType     <p>The type of info (player, value, combined) to get</p>
     * @return <p>The selected information about the record, or null if not found</p>
     */
    private @Nullable String getArenaRecord(@NotNull DropperArenaHandler arenaHandler, @NotNull String identifier,
                                            @NotNull ArenaGameMode gameMode, @NotNull RecordType recordType,
                                            int recordNumber, @NotNull InfoType infoType) {
        // Allow specifying the arena UUID or the arena name
        DropperArena arena;
        try {
            arena = arenaHandler.getArena(UUID.fromString(identifier));
        } catch (IllegalArgumentException exception) {
            arena = arenaHandler.getArena(identifier);
        }
        if (arena == null) {
            return null;
        }
        @NotNull Map<ArenaGameMode, DropperArenaRecordsRegistry> registries = arena.getData().recordRegistries();
        DropperArenaRecordsRegistry recordsRegistry = registries.get(gameMode);

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
    private @Nullable ArenaRecord<?> getRecord(@NotNull DropperArenaRecordsRegistry recordsRegistry,
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

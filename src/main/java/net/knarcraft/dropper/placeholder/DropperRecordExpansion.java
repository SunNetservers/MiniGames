package net.knarcraft.dropper.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.ArenaRecord;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.arena.DropperArenaHandler;
import net.knarcraft.dropper.arena.DropperArenaRecordsRegistry;
import net.knarcraft.dropper.placeholder.parsing.InfoType;
import net.knarcraft.dropper.placeholder.parsing.RecordType;
import net.knarcraft.dropper.placeholder.parsing.SelectionType;
import net.knarcraft.dropper.property.ArenaGameMode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A placeholder expansion for dropper record placeholders
 */
public class DropperRecordExpansion extends PlaceholderExpansion {

    private final Dropper plugin;

    public DropperRecordExpansion(Dropper plugin) {
        this.plugin = plugin;
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
        if (selectionType == SelectionType.ARENA) {
            info = getArenaRecord(arenaHandler, identifier, gameMode, recordType, recordNumber, infoType);
        }

        return Objects.requireNonNullElse(info, parameters);
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
            case TIME -> getRecord(recordsRegistry.getShortestTimeMilliSecondsRecords(), recordNumber);
            case DEATHS -> getRecord(recordsRegistry.getLeastDeathsRecords(), recordNumber);
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
            case PLAYER -> getPlayerName(arenaRecord.userId());
            case VALUE -> arenaRecord.record().toString();
            case COMBINED -> getPlayerName(arenaRecord.userId()) + ": " + arenaRecord.record().toString();
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
        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            return player.getName();
        } else {
            return playerId.toString();
        }
    }

}

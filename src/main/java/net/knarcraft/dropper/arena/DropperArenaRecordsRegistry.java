package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.record.ArenaRecord;
import net.knarcraft.dropper.arena.record.IntegerRecord;
import net.knarcraft.dropper.arena.record.LongRecord;
import net.knarcraft.dropper.arena.record.SummableArenaRecord;
import net.knarcraft.dropper.container.SerializableUUID;
import net.knarcraft.dropper.property.RecordResult;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A registry keeping track of all records
 */
public class DropperArenaRecordsRegistry implements ConfigurationSerializable {

    private final UUID arenaId;
    private final @NotNull Set<IntegerRecord> leastDeaths;
    private final @NotNull Set<LongRecord> shortestTimeMilliSeconds;

    /**
     * Instantiates a new empty records registry
     */
    public DropperArenaRecordsRegistry(@NotNull UUID arenaId) {
        this.arenaId = arenaId;
        this.leastDeaths = new HashSet<>();
        this.shortestTimeMilliSeconds = new HashSet<>();
    }

    /**
     * Instantiates a new records registry
     *
     * @param leastDeaths              <p>The existing least death records to use</p>
     * @param shortestTimeMilliSeconds <p>The existing leash time records to use</p>
     */
    private DropperArenaRecordsRegistry(@NotNull UUID arenaId, @NotNull Set<IntegerRecord> leastDeaths,
                                        @NotNull Set<LongRecord> shortestTimeMilliSeconds) {
        this.arenaId = arenaId;
        this.leastDeaths = new HashSet<>(leastDeaths);
        this.shortestTimeMilliSeconds = new HashSet<>(shortestTimeMilliSeconds);
    }

    /**
     * Gets all existing death records
     *
     * @return <p>Existing death records</p>
     */
    public Set<SummableArenaRecord<Integer>> getLeastDeathsRecords() {
        return new HashSet<>(this.leastDeaths);
    }

    /**
     * Gets all existing time records
     *
     * @return <p>Existing time records</p>
     */
    public Set<SummableArenaRecord<Long>> getShortestTimeMilliSecondsRecords() {
        return new HashSet<>(this.shortestTimeMilliSeconds);
    }

    /**
     * Registers a new deaths-record
     *
     * @param playerId <p>The id of the player that performed the records</p>
     * @param deaths   <p>The number of deaths suffered before the player finished the arena</p>
     * @return <p>The result explaining what type of record was achieved</p>
     */
    public @NotNull RecordResult registerDeathRecord(@NotNull UUID playerId, int deaths) {
        Set<ArenaRecord<Integer>> asInt = new HashSet<>(leastDeaths);
        return registerRecord(asInt, playerId, deaths);
    }

    /**
     * Registers a new time-record
     *
     * @param playerId     <p>The id of the player that performed the records</p>
     * @param milliseconds <p>The number of milliseconds it took the player to finish the dropper arena</p>
     * @return <p>The result explaining what type of record was achieved</p>
     */
    public @NotNull RecordResult registerTimeRecord(@NotNull UUID playerId, long milliseconds) {
        Set<ArenaRecord<Long>> asLong = new HashSet<>(shortestTimeMilliSeconds);
        return registerRecord(asLong, playerId, milliseconds);
    }

    /**
     * Saves changed records
     */
    private void save() {
        Dropper.getInstance().getArenaHandler().saveData(this.arenaId);
    }

    /**
     * Registers a new record if applicable
     *
     * @param existingRecords <p>The map of existing records to use</p>
     * @param playerId        <p>The id of the player that potentially achieved a record</p>
     * @param amount          <p>The amount of whatever the player achieved</p>
     * @return <p>The result of the player's record attempt</p>
     */
    private <T extends Comparable<T>> @NotNull RecordResult registerRecord(@NotNull Set<ArenaRecord<T>> existingRecords,
                                                                           @NotNull UUID playerId, T amount) {
        RecordResult result;
        if (existingRecords.stream().allMatch((entry) -> amount.compareTo(entry.getRecord()) < 0)) {
            // If the given value is less than all other values, that's a world record!
            result = RecordResult.WORLD_RECORD;
            existingRecords.add(new ArenaRecord<>(playerId, amount));
            save();
            return result;
        }

        ArenaRecord<T> playerRecord = getRecord(existingRecords, playerId);
        if (playerRecord != null && amount.compareTo(playerRecord.getRecord()) < 0) {
            // If the given value is less than the player's previous value, that's a personal best!
            result = RecordResult.PERSONAL_BEST;
            existingRecords.add(new ArenaRecord<>(playerId, amount));
            save();
        } else {
            // Make sure to save the record if this is the user's first attempt
            if (playerRecord == null) {
                save();
            }
            result = RecordResult.NONE;
        }

        return result;
    }

    /**
     * Gets the record stored for the given player
     *
     * @param existingRecords <p>The existing records to look through</p>
     * @param playerId        <p>The id of the player to look for</p>
     * @param <T>             <p>The type of the stored record</p>
     * @return <p>The record, or null if not found</p>
     */
    private <T extends Comparable<T>> @Nullable ArenaRecord<T> getRecord(@NotNull Set<ArenaRecord<T>> existingRecords,
                                                                         @NotNull UUID playerId) {
        AtomicReference<ArenaRecord<T>> record = new AtomicReference<>();
        existingRecords.forEach((item) -> {
            if (item.getUserId().equals(playerId)) {
                record.set(item);
            }
        });
        return record.get();
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("arenaId", new SerializableUUID(this.arenaId));
        data.put("leastDeaths", this.leastDeaths);
        data.put("shortestTime", this.shortestTimeMilliSeconds);
        return data;
    }

    /**
     * Deserializes the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized records registry</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static DropperArenaRecordsRegistry deserialize(Map<String, Object> data) {
        UUID arenaId = ((SerializableUUID) data.get("arenaId")).uuid();
        Set<IntegerRecord> leastDeaths =
                (Set<IntegerRecord>) data.getOrDefault("leastDeaths", new HashMap<>());
        Set<LongRecord> shortestTimeMilliseconds =
                (Set<LongRecord>) data.getOrDefault("shortestTime", new HashMap<>());
        return new DropperArenaRecordsRegistry(arenaId, leastDeaths, shortestTimeMilliseconds);
    }

}

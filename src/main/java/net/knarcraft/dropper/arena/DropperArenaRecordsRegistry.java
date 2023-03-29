package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.container.SerializableUUID;
import net.knarcraft.dropper.property.RecordResult;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * A registry keeping track of all records
 */
public class DropperArenaRecordsRegistry implements ConfigurationSerializable {

    private final UUID arenaId;
    private final @NotNull Map<UUID, Number> leastDeaths;
    private final @NotNull Map<UUID, Number> shortestTimeMilliSeconds;

    /**
     * Instantiates a new empty records registry
     */
    public DropperArenaRecordsRegistry(@NotNull UUID arenaId) {
        this.arenaId = arenaId;
        this.leastDeaths = new HashMap<>();
        this.shortestTimeMilliSeconds = new HashMap<>();
    }

    /**
     * Instantiates a new records registry
     *
     * @param leastDeaths              <p>The existing least death records to use</p>
     * @param shortestTimeMilliSeconds <p>The existing leash time records to use</p>
     */
    private DropperArenaRecordsRegistry(@NotNull UUID arenaId, @NotNull Map<UUID, Integer> leastDeaths,
                                        @NotNull Map<UUID, Long> shortestTimeMilliSeconds) {
        this.arenaId = arenaId;
        this.leastDeaths = new HashMap<>(leastDeaths);
        this.shortestTimeMilliSeconds = new HashMap<>(shortestTimeMilliSeconds);
    }

    /**
     * Gets all existing death records
     *
     * @return <p>Existing death records</p>
     */
    public Map<UUID, Integer> getLeastDeathsRecords() {
        Map<UUID, Integer> leastDeathRecords = new HashMap<>();
        for (Map.Entry<UUID, Number> entry : this.leastDeaths.entrySet()) {
            leastDeathRecords.put(entry.getKey(), entry.getValue().intValue());
        }
        return leastDeathRecords;
    }

    /**
     * Gets all existing time records
     *
     * @return <p>Existing time records</p>
     */
    public Map<UUID, Long> getShortestTimeMilliSecondsRecords() {
        Map<UUID, Long> leastTimeRecords = new HashMap<>();
        for (Map.Entry<UUID, Number> entry : this.shortestTimeMilliSeconds.entrySet()) {
            leastTimeRecords.put(entry.getKey(), entry.getValue().longValue());
        }
        return leastTimeRecords;
    }

    /**
     * Registers a new deaths-record
     *
     * @param playerId <p>The id of the player that performed the records</p>
     * @param deaths   <p>The number of deaths suffered before the player finished the arena</p>
     * @return <p>The result explaining what type of record was achieved</p>
     */
    public @NotNull RecordResult registerDeathRecord(@NotNull UUID playerId, int deaths) {
        return registerRecord(leastDeaths, playerId, deaths);
    }

    /**
     * Registers a new time-record
     *
     * @param playerId     <p>The id of the player that performed the records</p>
     * @param milliseconds <p>The number of milliseconds it took the player to finish the dropper arena</p>
     * @return <p>The result explaining what type of record was achieved</p>
     */
    public @NotNull RecordResult registerTimeRecord(@NotNull UUID playerId, long milliseconds) {
        return registerRecord(shortestTimeMilliSeconds, playerId, milliseconds);
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
    private @NotNull RecordResult registerRecord(@NotNull Map<UUID, Number> existingRecords, @NotNull UUID playerId,
                                                 Number amount) {
        RecordResult result;
        Stream<Map.Entry<UUID, Number>> records = existingRecords.entrySet().stream();
        long amountLong = amount.longValue();

        if (records.allMatch((entry) -> amountLong < entry.getValue().longValue())) {
            // If the given value is less than all other values, that's a world record!
            result = RecordResult.WORLD_RECORD;
            existingRecords.put(playerId, amount);
            save();
        } else if (existingRecords.containsKey(playerId) && amountLong < existingRecords.get(playerId).longValue()) {
            // If the given value is less than the player's previous value, that's a personal best!
            result = RecordResult.PERSONAL_BEST;
            existingRecords.put(playerId, amount);
            save();
        } else {
            // Make sure to save the record if this is the user's first attempt
            if (!existingRecords.containsKey(playerId)) {
                save();
            }
            result = RecordResult.NONE;
        }

        return result;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("arenaId", new SerializableUUID(this.arenaId));

        Map<SerializableUUID, Number> leastDeaths = new HashMap<>();
        for (Map.Entry<UUID, Number> entry : this.leastDeaths.entrySet()) {
            leastDeaths.put(new SerializableUUID(entry.getKey()), entry.getValue());
        }
        data.put("leastDeaths", leastDeaths);

        Map<SerializableUUID, Number> shortestTimeMilliSeconds = new HashMap<>();
        for (Map.Entry<UUID, Number> entry : this.shortestTimeMilliSeconds.entrySet()) {
            shortestTimeMilliSeconds.put(new SerializableUUID(entry.getKey()), entry.getValue());
        }
        data.put("shortestTime", shortestTimeMilliSeconds);
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
        Map<SerializableUUID, Integer> leastDeathsData =
                (Map<SerializableUUID, Integer>) data.getOrDefault("leastDeaths", new HashMap<>());
        Map<UUID, Integer> leastDeaths = new HashMap<>();
        for (Map.Entry<SerializableUUID, Integer> entry : leastDeathsData.entrySet()) {
            leastDeaths.put(entry.getKey().uuid(), entry.getValue());
        }

        Map<SerializableUUID, Number> shortestTimeMillisecondsData =
                (Map<SerializableUUID, Number>) data.getOrDefault("shortestTime", new HashMap<>());
        Map<UUID, Long> shortestTimeMilliseconds = new HashMap<>();
        for (Map.Entry<SerializableUUID, Number> entry : shortestTimeMillisecondsData.entrySet()) {
            shortestTimeMilliseconds.put(entry.getKey().uuid(), entry.getValue().longValue());
        }

        return new DropperArenaRecordsRegistry(arenaId, leastDeaths, shortestTimeMilliseconds);
    }

}

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
    private final Map<SerializableUUID, Integer> leastDeaths;
    private final Map<SerializableUUID, Long> shortestTimeMilliSeconds;

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
    public DropperArenaRecordsRegistry(@NotNull UUID arenaId, @NotNull Map<SerializableUUID, Integer> leastDeaths,
                                       @NotNull Map<SerializableUUID, Long> shortestTimeMilliSeconds) {
        this.arenaId = arenaId;
        this.leastDeaths = new HashMap<>(leastDeaths);
        this.shortestTimeMilliSeconds = new HashMap<>(shortestTimeMilliSeconds);
    }

    /**
     * Gets all existing death records
     *
     * @return <p>Existing death records</p>
     */
    public Map<SerializableUUID, Integer> getLeastDeathsRecords() {
        return new HashMap<>(this.leastDeaths);
    }

    /**
     * Gets all existing time records
     *
     * @return <p>Existing time records</p>
     */
    public Map<SerializableUUID, Long> getShortestTimeMilliSecondsRecords() {
        return new HashMap<>(this.shortestTimeMilliSeconds);
    }

    /**
     * Registers a new deaths-record
     *
     * @param playerId <p>The id of the player that performed the records</p>
     * @param deaths   <p>The number of deaths suffered before the player finished the arena</p>
     * @return <p>The result explaining what type of record was achieved</p>
     */
    public @NotNull RecordResult registerDeathRecord(@NotNull UUID playerId, int deaths) {
        RecordResult result;
        Stream<Map.Entry<SerializableUUID, Integer>> records = leastDeaths.entrySet().stream();
        SerializableUUID serializableUUID = new SerializableUUID(playerId);

        if (records.allMatch((entry) -> deaths < entry.getValue())) {
            // If the given value is less than all other values, that's a world record!
            result = RecordResult.WORLD_RECORD;
            leastDeaths.put(serializableUUID, deaths);
            save();
        } else if (leastDeaths.containsKey(serializableUUID) && deaths < leastDeaths.get(serializableUUID)) {
            // If the given value is less than the player's previous value, that's a personal best!
            result = RecordResult.PERSONAL_BEST;
            leastDeaths.put(serializableUUID, deaths);
            save();
        } else {
            // Make sure to save the record if this is the user's first attempt
            if (!leastDeaths.containsKey(serializableUUID)) {
                save();
            }
            result = RecordResult.NONE;
        }

        return result;
    }

    /**
     * Registers a new time-record
     *
     * @param playerId     <p>The id of the player that performed the records</p>
     * @param milliseconds <p>The number of milliseconds it took the player to finish the dropper arena</p>
     * @return <p>The result explaining what type of record was achieved</p>
     */
    public @NotNull RecordResult registerTimeRecord(@NotNull UUID playerId, long milliseconds) {
        RecordResult result;
        Stream<Map.Entry<SerializableUUID, Long>> records = shortestTimeMilliSeconds.entrySet().stream();
        SerializableUUID serializableUUID = new SerializableUUID(playerId);

        if (records.allMatch((entry) -> milliseconds < entry.getValue())) {
            //If the given value is less than all other values, that's a world record!
            result = RecordResult.WORLD_RECORD;
            shortestTimeMilliSeconds.put(serializableUUID, milliseconds);
            save();
        } else if (shortestTimeMilliSeconds.containsKey(serializableUUID) &&
                milliseconds < shortestTimeMilliSeconds.get(serializableUUID)) {
            //If the given value is less than the player's previous value, that's a personal best!
            result = RecordResult.PERSONAL_BEST;
            shortestTimeMilliSeconds.put(serializableUUID, milliseconds);
            save();
        } else {
            // Make sure to save the record if this is the user's first attempt
            if (!shortestTimeMilliSeconds.containsKey(serializableUUID)) {
                save();
            }
            result = RecordResult.NONE;
        }

        return result;
    }

    /**
     * Saves changed records
     */
    private void save() {
        Dropper.getInstance().getArenaHandler().saveData(this.arenaId);
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
        Map<SerializableUUID, Integer> leastDeathsData =
                (Map<SerializableUUID, Integer>) data.getOrDefault("leastDeaths", new HashMap<>());
        Map<SerializableUUID, Long> shortestTimeMillisecondsData =
                (Map<SerializableUUID, Long>) data.getOrDefault("shortestTime", new HashMap<>());

        return new DropperArenaRecordsRegistry(arenaId, leastDeathsData, shortestTimeMillisecondsData);
    }

}

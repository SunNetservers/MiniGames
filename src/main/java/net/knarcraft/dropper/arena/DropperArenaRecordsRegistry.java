package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.property.RecordResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A registry keeping track of all records
 */
public class DropperArenaRecordsRegistry {

    private final Map<Player, Integer> leastDeaths;
    private final Map<Player, Long> shortestTimeMilliSeconds;

    /**
     * Instantiates a new empty records registry
     */
    public DropperArenaRecordsRegistry() {
        this.leastDeaths = new HashMap<>();
        this.shortestTimeMilliSeconds = new HashMap<>();
    }

    /**
     * Instantiates a new records registry
     *
     * @param leastDeaths              <p>The existing least death records to use</p>
     * @param shortestTimeMilliSeconds <p>The existing leash time records to use</p>
     */
    public DropperArenaRecordsRegistry(@NotNull Map<Player, Integer> leastDeaths,
                                       @NotNull Map<Player, Long> shortestTimeMilliSeconds) {
        this.leastDeaths = new HashMap<>(leastDeaths);
        this.shortestTimeMilliSeconds = new HashMap<>(shortestTimeMilliSeconds);
    }

    /**
     * Gets all existing death records
     *
     * @return <p>Existing death records</p>
     */
    public Map<Player, Integer> getLeastDeathsRecords() {
        return new HashMap<>(this.leastDeaths);
    }

    /**
     * Gets all existing time records
     *
     * @return <p>Existing time records</p>
     */
    public Map<Player, Long> getShortestTimeMilliSecondsRecords() {
        return new HashMap<>(this.shortestTimeMilliSeconds);
    }

    /**
     * Registers a new deaths-record
     *
     * @param player <p>The player that performed the records</p>
     * @param deaths <p>The number of deaths suffered before the player finished the arena</p>
     * @return <p>The result explaining what type of record was achieved</p>
     */
    public @NotNull RecordResult registerDeathRecord(@NotNull Player player, int deaths) {
        RecordResult result;
        Stream<Map.Entry<Player, Integer>> records = leastDeaths.entrySet().stream();

        if (records.allMatch((entry) -> deaths < entry.getValue())) {
            //If the given value is less than all other values, that's a world record!
            result = RecordResult.WORLD_RECORD;
            leastDeaths.put(player, deaths);
        } else if (leastDeaths.containsKey(player) && deaths < leastDeaths.get(player)) {
            //If the given value is less than the player's previous value, that's a personal best!
            result = RecordResult.PERSONAL_BEST;
            leastDeaths.put(player, deaths);
        } else {
            result = RecordResult.NONE;
        }

        return result;
    }

    /**
     * Registers a new time-record
     *
     * @param player       <p>The player that performed the records</p>
     * @param milliseconds <p>The number of milliseconds it took the player to finish the dropper arena</p>
     * @return <p>The result explaining what type of record was achieved</p>
     */
    public @NotNull RecordResult registerTimeRecord(@NotNull Player player, long milliseconds) {
        RecordResult result;
        Stream<Map.Entry<Player, Long>> records = shortestTimeMilliSeconds.entrySet().stream();

        if (records.allMatch((entry) -> milliseconds < entry.getValue())) {
            //If the given value is less than all other values, that's a world record!
            result = RecordResult.WORLD_RECORD;
            shortestTimeMilliSeconds.put(player, milliseconds);
        } else if (shortestTimeMilliSeconds.containsKey(player) && milliseconds < shortestTimeMilliSeconds.get(player)) {
            //If the given value is less than the player's previous value, that's a personal best!
            result = RecordResult.PERSONAL_BEST;
            shortestTimeMilliSeconds.put(player, milliseconds);
        } else {
            result = RecordResult.NONE;
        }

        return result;
    }

}

package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.arena.record.IntegerRecord;
import net.knarcraft.minigames.arena.record.LongRecord;
import net.knarcraft.minigames.container.SerializableUUID;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A registry keeping track of all records
 */
public class ParkourArenaRecordsRegistry extends ArenaRecordsRegistry {

    /**
     * Instantiates a new empty records registry
     */
    public ParkourArenaRecordsRegistry(@NotNull UUID arenaId) {
        super(arenaId);
    }

    /**
     * Instantiates a new records registry
     *
     * @param leastDeaths              <p>The existing least death records to use</p>
     * @param shortestTimeMilliSeconds <p>The existing leash time records to use</p>
     */
    private ParkourArenaRecordsRegistry(@NotNull UUID arenaId, @NotNull Set<IntegerRecord> leastDeaths,
                                        @NotNull Set<LongRecord> shortestTimeMilliSeconds) {
        super(arenaId, leastDeaths, shortestTimeMilliSeconds);
    }

    /**
     * Saves changed records
     */
    @Override
    protected void save() {
        MiniGames.getInstance().getParkourArenaHandler().saveData(this.arenaId);
    }

    /**
     * Deserializes the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized records registry</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static ParkourArenaRecordsRegistry deserialize(Map<String, Object> data) {
        UUID arenaId = ((SerializableUUID) data.get("arenaId")).getRawValue();
        Set<IntegerRecord> leastDeaths =
                (Set<IntegerRecord>) data.getOrDefault("leastDeaths", new HashMap<>());
        Set<LongRecord> shortestTimeMilliseconds =
                (Set<LongRecord>) data.getOrDefault("shortestTime", new HashMap<>());

        leastDeaths.removeIf(Objects::isNull);
        shortestTimeMilliseconds.removeIf(Objects::isNull);
        return new ParkourArenaRecordsRegistry(arenaId, leastDeaths, shortestTimeMilliseconds);
    }

}

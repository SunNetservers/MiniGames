package net.knarcraft.minigames.arena.record;

import net.knarcraft.minigames.container.SerializableUUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * A record storing a Long time
 */
public class LongRecord extends SummableArenaRecord<Long> {

    /**
     * Instantiates a new long record
     *
     * @param userId <p>The id of the player that achieved the record</p>
     * @param record <p>The record achieved</p>
     */
    public LongRecord(@NotNull UUID userId, @NotNull Long record) {
        super(userId, record);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return other instanceof LongRecord && this.getUserId().equals(((LongRecord) other).getUserId());
    }

    @Override
    @NotNull
    public SummableArenaRecord<Long> sum(@NotNull Long value) {
        return new LongRecord(this.getUserId(), this.getRecord() + value);
    }

    @Override
    @NotNull
    public String getAsString() {
        double seconds = getRecord() / 1000.0;
        double minutes = 0;
        if (seconds > 60) {
            minutes = seconds / 60.0;
            seconds = seconds % 60;
        }

        if (minutes > 0) {
            return String.format("%.0fm%.2fs", minutes, seconds);
        } else {
            return String.format("%.2fs", seconds);
        }
    }

    /**
     * Deserializes the saved arena record
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings("unused")
    @NotNull
    public static LongRecord deserialize(@NotNull Map<String, Object> data) {
        return new LongRecord(((SerializableUUID) data.get("userId")).getRawValue(),
                ((Number) data.get("record")).longValue());
    }

}

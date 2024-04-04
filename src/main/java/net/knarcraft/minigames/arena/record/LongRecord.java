package net.knarcraft.minigames.arena.record;

import net.knarcraft.minigames.container.SerializableUUID;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * A record storing a Long time
 */
public class LongRecord extends SummableArenaRecord<Long> {

    /**
     * @param userId <p>The id of the player that achieved the record</p>
     * @param record <p>The record achieved</p>
     */
    public LongRecord(UUID userId, Long record) {
        super(userId, record);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof LongRecord && this.getUserId().equals(((LongRecord) other).getUserId());
    }

    @Override
    public SummableArenaRecord<Long> sum(Long value) {
        return new LongRecord(this.getUserId(), this.getRecord() + value);
    }

    @Override
    public String getAsString() {
        int seconds = (int) Math.floor(getRecord() / 1000.0);
        int minutes = 0;
        if (seconds > 60) {
            minutes = (int) Math.floor(seconds / 60.0);
            seconds = seconds % 60;
        }

        if (minutes > 0) {
            return minutes + "m" + seconds + "s";
        } else {
            return seconds + "s";
        }
    }

    /**
     * Deserializes the saved arena record
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings("unused")
    public static LongRecord deserialize(@NotNull Map<String, Object> data) {
        return new LongRecord(((SerializableUUID) data.get("userId")).getRawValue(), ((Number) data.get("record")).longValue());
    }

}

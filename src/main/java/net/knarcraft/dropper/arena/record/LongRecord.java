package net.knarcraft.dropper.arena.record;

import net.knarcraft.dropper.container.SerializableUUID;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * A record storing a Long
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
    public SummableArenaRecord<Long> sum(Long value) {
        return new LongRecord(this.getUserId(), this.getRecord() + value);
    }

    /**
     * Deserializes the saved arena record
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings("unused")
    public static LongRecord deserialize(@NotNull Map<String, Object> data) {
        return new LongRecord(((SerializableUUID) data.get("userId")).uuid(), ((Number) data.get("record")).longValue());
    }

}

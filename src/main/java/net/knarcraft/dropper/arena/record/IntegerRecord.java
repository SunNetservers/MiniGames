package net.knarcraft.dropper.arena.record;

import net.knarcraft.dropper.container.SerializableUUID;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * A record storing an integer
 */
public class IntegerRecord extends SummableArenaRecord<Integer> {

    /**
     * @param userId <p>The id of the player that achieved the record</p>
     * @param record <p>The record achieved</p>
     */
    public IntegerRecord(UUID userId, Integer record) {
        super(userId, record);
    }

    @Override
    public SummableArenaRecord<Integer> sum(Integer value) {
        return new IntegerRecord(this.getUserId(), this.getRecord() + value);
    }

    /**
     * Deserializes the saved arena record
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings("unused")
    public static IntegerRecord deserialize(@NotNull Map<String, Object> data) {
        return new IntegerRecord(((SerializableUUID) data.get("userId")).uuid(), (Integer) data.get("record"));
    }

}
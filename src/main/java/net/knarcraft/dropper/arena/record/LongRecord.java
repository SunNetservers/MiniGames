package net.knarcraft.dropper.arena.record;

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

}

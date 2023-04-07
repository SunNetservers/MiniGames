package net.knarcraft.dropper.arena.record;

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

}

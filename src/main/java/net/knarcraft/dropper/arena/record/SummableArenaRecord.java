package net.knarcraft.dropper.arena.record;

import java.util.UUID;

/**
 * A type of arena record which can be summed together
 *
 * @param <K> <p>The type of the stored value</p>
 */
public abstract class SummableArenaRecord<K extends Comparable<K>> extends ArenaRecord<K> {

    /**
     * @param userId <p>The id of the player that achieved the record</p>
     * @param record <p>The record achieved</p>
     */
    public SummableArenaRecord(UUID userId, K record) {
        super(userId, record);
    }

    /**
     * Returns a summable record with the resulting sum
     *
     * @param value <p>The value to add to the existing value</p>
     * @return <p>A record with the sum of this record and the given value</p>
     */
    public abstract SummableArenaRecord<K> sum(K value);

}

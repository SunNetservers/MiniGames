package net.knarcraft.minigames.arena.record;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A type of arena record which can be summed together
 *
 * @param <RecordType> <p>The type of the stored value</p>
 */
public abstract class SummableArenaRecord<RecordType extends Comparable<RecordType>> extends ArenaRecord<RecordType> {

    /**
     * @param userId <p>The id of the player that achieved the record</p>
     * @param record <p>The record achieved</p>
     */
    public SummableArenaRecord(@NotNull UUID userId, @NotNull RecordType record) {
        super(userId, record);
    }

    /**
     * Returns a summable record with the resulting sum
     *
     * @param value <p>The value to add to the existing value</p>
     * @return <p>A record with the sum of this record and the given value</p>
     */
    @NotNull
    public abstract SummableArenaRecord<RecordType> sum(@NotNull RecordType value);

}

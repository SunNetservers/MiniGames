package net.knarcraft.minigames.property;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type of record a player can achieve
 */
public enum RecordType {

    /**
     * A least-deaths record
     */
    DEATHS,

    /**
     *
     */
    TIME,
    ;

    /**
     * Gets the record type specified in the given string
     *
     * @param type <p>The string specifying the record type</p>
     * @return <p>The record type, or null if not found</p>
     */
    public static @Nullable RecordType getFromString(@NotNull String type) {
        for (RecordType recordType : RecordType.values()) {
            if (recordType.name().equalsIgnoreCase(type)) {
                return recordType;
            }
        }
        return null;
    }

}

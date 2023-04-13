package net.knarcraft.minigames.placeholder.parsing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The type of information returned by a placeholder
 */
public enum InfoType {

    /**
     * The player that achieved the record
     */
    PLAYER,

    /**
     * The value of the record, whatever it is
     */
    VALUE,

    /**
     * A combined PLAYER: VALUE
     */
    COMBINED,
    ;

    /**
     * Gets the info type specified in the given string
     *
     * @param type <p>The string specifying the info type</p>
     * @return <p>The info type, or null if not found</p>
     */
    public static @Nullable InfoType getFromString(@NotNull String type) {
        for (InfoType infoType : InfoType.values()) {
            if (infoType.name().equalsIgnoreCase(type)) {
                return infoType;
            }
        }
        return null;
    }

}

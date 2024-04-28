package net.knarcraft.minigames.placeholder.parsing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The types of player information that can be retrieved in placeholders
 */
public enum PlayerInfoType {

    /**
     * The number of current players
     */
    COUNT,

    /**
     * Information about a single player
     */
    PLAYER,
    ;

    /**
     * Gets the info type specified in the given string
     *
     * @param type <p>The string specifying the info type</p>
     * @return <p>The info type, or null if not found</p>
     */
    public static @Nullable PlayerInfoType getFromString(@NotNull String type) {
        for (PlayerInfoType infoType : PlayerInfoType.values()) {
            if (infoType.name().equalsIgnoreCase(type)) {
                return infoType;
            }
        }
        return null;
    }

}

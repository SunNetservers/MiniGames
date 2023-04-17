package net.knarcraft.minigames.placeholder.parsing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type of selection performed by a placeholder
 */
public enum SelectionType {

    /**
     * The identifier is trying to select a group
     */
    GROUP,

    /**
     * The identifier is trying to select an arena
     */
    ARENA,
    ;

    /**
     * Gets the selection type specified in the given string
     *
     * @param type <p>The string specifying the selection type</p>
     * @return <p>The selection type, or null if not found</p>
     */
    public static @Nullable SelectionType getFromString(@NotNull String type) {
        for (SelectionType selectionType : SelectionType.values()) {
            if (selectionType.name().equalsIgnoreCase(type)) {
                return selectionType;
            }
        }
        return null;
    }

}

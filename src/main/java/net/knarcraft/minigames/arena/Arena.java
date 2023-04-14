package net.knarcraft.minigames.arena;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * An interface describing an arena
 */
public interface Arena {

    /**
     * Gets the data stored for this arena
     *
     * @return <p>The stored data</p>
     */
    ArenaData getData();

    /**
     * Gets the id of this arena
     *
     * @return <p>This arena's identifier</p>
     */
    @NotNull UUID getArenaId();

    /**
     * Gets this arena's sanitized name
     *
     * @return <p>This arena's sanitized name</p>
     */
    @NotNull String getArenaNameSanitized();

    /**
     * Removes the data file belonging to this arena
     *
     * @return <p>True if successfully removed</p>
     */
    boolean removeData();

    /**
     * Saves this arena's data
     *
     * @return <p>True if successfully saved</p>
     */
    boolean saveData();

}

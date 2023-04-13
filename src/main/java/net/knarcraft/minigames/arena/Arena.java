package net.knarcraft.minigames.arena;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * An interface describing all arenas
 */
public interface Arena {

    /**
     * Gets the id of this arena
     *
     * @return <p>This arena's identifier</p>
     */
    @NotNull UUID getArenaId();

    /**
     * Gets the name of this arena
     *
     * @return <p>The name of this arena</p>
     */
    @NotNull String getArenaName();

    /**
     * Gets this arena's sanitized name
     *
     * @return <p>This arena's sanitized name</p>
     */
    @NotNull String getArenaNameSanitized();

}

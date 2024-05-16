package net.knarcraft.minigames.arena;

import org.jetbrains.annotations.NotNull;

/**
 * A representation of each key used for storing arena data
 */
public interface StorageKey {

    /**
     * Gets the configuration key this enum represents
     *
     * @return <p>The string key representation.</p>
     */
    @NotNull String getKey();

}

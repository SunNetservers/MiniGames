package net.knarcraft.dropper.property;

import org.jetbrains.annotations.NotNull;

/**
 * A representation of each key used for storing arena data
 */
public enum ArenaStorageKey {

    /**
     * The key for an arena's name
     */
    NAME("arenaName"),

    /**
     * The key for an arena's spawn location
     */
    SPAWN_LOCATION("arenaSpawnLocation"),

    /**
     * The key for an arena's exit location
     */
    EXIT_LOCATION("arenaExitLocation"),

    /**
     * The key for a player in this arena's vertical velocity
     */
    PLAYER_VERTICAL_VELOCITY("arenaPlayerVerticalVelocity"),

    /**
     * The key for a player in this arena's horizontal velocity
     */
    PLAYER_HORIZONTAL_VELOCITY("arenaPlayerHorizontalVelocity"),

    /**
     * The key for this arena's stage
     */
    STAGE("arenaStage"),

    /**
     * The key for the type of this arena's win block
     */
    WIN_BLOCK_TYPE("winBlockType"),
    ;

    private final @NotNull String key;

    /**
     * Instantiates a new arena storage key
     *
     * @param key <p>The string path of the configuration key this value represents.</p>
     */
    ArenaStorageKey(@NotNull String key) {
        this.key = key;
    }

    /**
     * Gets the configuration key this enum represents
     *
     * @return <p>The string key representation.</p>
     */
    public @NotNull String getKey() {
        return this.key;
    }

}

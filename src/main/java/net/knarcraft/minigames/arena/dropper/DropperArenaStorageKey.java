package net.knarcraft.minigames.arena.dropper;

import org.jetbrains.annotations.NotNull;

/**
 * A representation of each key used for storing arena data
 */
public enum DropperArenaStorageKey {

    /**
     * The key for an arena's id
     */
    ID("arenaId"),

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
     * The key for the type of this arena's win block
     */
    WIN_BLOCK_TYPE("winBlockType"),

    /**
     * The key for this arena's data
     */
    DATA("arenaData"),

    /**
     * The key for this arena's rewards
     */
    REWARDS("rewards"),
    ;

    private final @NotNull String key;

    /**
     * Instantiates a new arena storage key
     *
     * @param key <p>The string path of the configuration key this value represents.</p>
     */
    DropperArenaStorageKey(@NotNull String key) {
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

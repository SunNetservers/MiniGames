package net.knarcraft.minigames.arena.parkour;

import org.jetbrains.annotations.NotNull;

/**
 * A representation of each key used for storing arena data
 */
public enum ParkourArenaStorageKey {

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
     * The key for the type of this arena's win block
     */
    WIN_BLOCK_TYPE("winBlockType"),

    /**
     * The key for this arena's win location (overrides win block type)
     */
    WIN_LOCATION("winLocation"),

    /**
     * The key for this arena's kill plane blocks (overrides the config)
     */
    KILL_PLANE_BLOCKS("killPlaneBlocks"),

    /**
     * The key for this arena's checkpoint locations
     */
    CHECKPOINTS("checkpoints"),

    /**
     * The hey for this arena's data
     */
    DATA("arenaData"),
    ;

    private final @NotNull String key;

    /**
     * Instantiates a new arena storage key
     *
     * @param key <p>The string path of the configuration key this value represents.</p>
     */
    ParkourArenaStorageKey(@NotNull String key) {
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

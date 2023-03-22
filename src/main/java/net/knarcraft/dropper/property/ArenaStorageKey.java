package net.knarcraft.dropper.property;

import org.jetbrains.annotations.NotNull;

/**
 * A representation of each key used for storing arena data
 */
public enum ArenaStorageKey {

    NAME("arenaName"),
    SPAWN_LOCATION("arenaSpawnLocation"),
    EXIT_LOCATION("arenaExitLocation"),
    PLAYER_VELOCITY("arenaPlayerVelocity"),
    STAGE("arenaStage"),
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

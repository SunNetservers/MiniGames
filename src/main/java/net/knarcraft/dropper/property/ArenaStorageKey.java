package net.knarcraft.dropper.property;

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

    private final String key;

    /**
     * Instantiates a new arena storage key
     *
     * @param key <p>The string path of the configuration key this value represents.</p>
     */
    ArenaStorageKey(String key) {
        this.key = key;
    }

    /**
     * Gets the configuration key this enum represents
     *
     * @return <p>The string key representation.</p>
     */
    public String getKey() {
        return this.key;
    }

}

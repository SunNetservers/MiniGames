package net.knarcraft.dropper.property;

/**
 * A representation of possible arena game-modes
 */
public enum ArenaGameMode {

    /**
     * The default game-mode. Failing once throws the player out.
     * //TODO: Verify if we want the default game-mode to lock the player in the arena until they beat it
     */
    DEFAULT,

    /**
     * The least-deaths game-mode. Player plays until they manage to win. The number of deaths is recorded.
     */
    LEAST_DEATHS,

    /**
     * The least-time game-mode. Player plays until they manage to win. The total time of the session is recorded.
     */
    LEAST_TIME,

}

package net.knarcraft.dropper.property;

import org.jetbrains.annotations.NotNull;

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
    ;

    /**
     * Tries to match the correct game-mode according to the given string
     *
     * @param gameMode <p>The game-mode string to match</p>
     * @return <p>The specified arena game-mode</p>
     */
    public static @NotNull ArenaGameMode matchGamemode(@NotNull String gameMode) {
        String sanitized = gameMode.trim().toLowerCase();
        if (sanitized.matches("(least)?deaths?")) {
            return ArenaGameMode.LEAST_DEATHS;
        } else if (sanitized.matches("(least)?time")) {
            return ArenaGameMode.LEAST_TIME;
        } else {
            return ArenaGameMode.DEFAULT;
        }
    }

}

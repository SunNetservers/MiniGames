package net.knarcraft.minigames.arena;

import org.jetbrains.annotations.NotNull;

/**
 * An interface describing any arena game-mode
 */
public interface ArenaGameMode {

    /**
     * Gets the name of this game-mode
     *
     * @return <p>The name of this game-mode</p>
     */
    @NotNull String name();

    /**
     * Gets a set of all available arena game-modes in the type definition of this game-mode
     *
     * @return <p>All game-modes in this game-mode's class</p>
     */
    @NotNull ArenaGameMode[] getValues();

}

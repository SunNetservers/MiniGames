package net.knarcraft.minigames.arena;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A player's session while in an arena
 */
public interface ArenaSession {

    /**
     * Gets the game-mode the player is playing in this session
     *
     * @return <p>The game-mode for this session</p>
     */
    @NotNull ArenaGameMode getGameMode();

    /**
     * Gets the state of the player when they joined the session
     *
     * @return <p>The player's entry state</p>
     */
    @NotNull PlayerEntryState getEntryState();

    /**
     * Triggers a win for the player playing in this session
     */
    void triggerWin();

    /**
     * Triggers a loss for the player playing in this session
     */
    void triggerLoss();

    /**
     * Triggers a quit for the player playing in this session
     *
     * @param immediately <p>Whether to to the teleportation immediately, not using any timers</p>
     */
    void triggerQuit(boolean immediately);

    /**
     * Gets the arena this session is being played in
     *
     * @return <p>The session's arena</p>
     */
    @NotNull Arena getArena();

    /**
     * Gets the player playing in this session
     *
     * @return <p>This session's player</p>
     */
    @NotNull Player getPlayer();

}

package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.gui.ArenaGUI;
import org.jetbrains.annotations.NotNull;

/**
 * A player's session while in an arena
 */
public interface ArenaSession {

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
     * @param immediately   <p>Whether to to the teleportation immediately, not using any timers</p>
     * @param removeSession <p>Whether to also remove the session. Should usually be true.</p>
     */
    void triggerQuit(boolean immediately, boolean removeSession);

    /**
     * Gets the arena this session is being played in
     *
     * @return <p>The session's arena</p>
     */
    @NotNull Arena getArena();

    /**
     * Gets the GUI with this arena's options
     *
     * @return <p>This arena's GUI</p>
     */
    @NotNull ArenaGUI getGUI();

}

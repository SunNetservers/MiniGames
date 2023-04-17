package net.knarcraft.minigames.arena;

import org.bukkit.Location;

/**
 * The stored state of a player
 */
public interface PlayerEntryState {

    /**
     * Sets the state of the stored player to the state used by the arena
     */
    void setArenaState();

    /**
     * Restores the stored state for the stored player
     */
    void restore();

    /**
     * Gets the location the player entered from
     *
     * @return <p>The location the player entered from</p>
     */
    Location getEntryLocation();

}

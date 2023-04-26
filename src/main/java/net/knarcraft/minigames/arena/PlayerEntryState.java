package net.knarcraft.minigames.arena;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The stored state of a player
 */
public interface PlayerEntryState extends ConfigurationSerializable {

    /**
     * Sets the state of the stored player to the state used by the arena
     */
    void setArenaState();

    /**
     * Restores the stored state for the stored player
     */
    boolean restore();

    /**
     * Restores the stored state for the given player
     *
     * @param player <p>A player object that's refers to the same player as the stored player</p>
     */
    void restore(Player player);

    /**
     * Gets the id of the player this state belongs to
     *
     * @return <p>The player the state belongs to</p>
     */
    UUID getPlayerId();

    /**
     * Gets the location the player entered from
     *
     * @return <p>The location the player entered from</p>
     */
    Location getEntryLocation();

}

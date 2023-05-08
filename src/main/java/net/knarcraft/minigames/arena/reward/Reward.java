package net.knarcraft.minigames.arena.reward;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A reward a player can be awarded
 */
public interface Reward extends ConfigurationSerializable {

    /**
     * Grants this reward to the given player
     *
     * @param player <p>The player this reward should be granted to</p>
     * @return <p>True if the item was granted. False if not possible.</p>
     */
    boolean grant(@NotNull Player player);

    /**
     * Gets the message to display to a user when granting this reward
     *
     * @return <p>The message to display when this reward is granted</p>
     */
    @NotNull String getGrantMessage();

}

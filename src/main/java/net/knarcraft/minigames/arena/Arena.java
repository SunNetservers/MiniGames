package net.knarcraft.minigames.arena;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * An interface describing an arena
 */
public interface Arena {

    /**
     * Gets the name of this arena
     *
     * @return <p>The name of this arena</p>
     */
    @NotNull String getArenaName();

    /**
     * Gets the data stored for this arena
     *
     * @return <p>The stored data</p>
     */
    @NotNull ArenaData getData();

    /**
     * Gets the id of this arena
     *
     * @return <p>This arena's identifier</p>
     */
    @NotNull UUID getArenaId();

    /**
     * Gets this arena's sanitized name
     *
     * @return <p>This arena's sanitized name</p>
     */
    @NotNull String getArenaNameSanitized();

    /**
     * Removes the data file belonging to this arena
     *
     * @return <p>True if successfully removed</p>
     */
    boolean removeData();

    /**
     * Saves this arena's data
     *
     * @return <p>True if successfully saved</p>
     */
    boolean saveData();

    /**
     * Gets whether standing on the given block should cause a win
     *
     * @param block <p>The block to check</p>
     * @return <p>True if standing on the block will cause a win</p>
     */
    boolean willCauseWin(Block block);

    /**
     * Gets whether standing on the given block should cause a loss
     *
     * @param block <p>The block to check</p>
     * @return <p>True if standing on the block will cause a loss</p>
     */
    boolean willCauseLoss(Block block);

    /**
     * Gets whether the win location is a solid block
     *
     * @return <p>True if the location is a solid block</p>
     */
    boolean winLocationIsSolid();

    /**
     * Gets the location of this arena's spawn
     *
     * @return <p>This arena's spawn location</p>
     */
    @NotNull Location getSpawnLocation();

}

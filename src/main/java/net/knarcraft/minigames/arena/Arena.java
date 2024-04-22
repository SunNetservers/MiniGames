package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.arena.reward.Reward;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
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
    @NotNull
    String getArenaName();

    /**
     * Gets the data stored for this arena
     *
     * @return <p>The stored data</p>
     */
    @NotNull
    ArenaData getData();

    /**
     * Gets the id of this arena
     *
     * @return <p>This arena's identifier</p>
     */
    @NotNull
    UUID getArenaId();

    /**
     * Gets this arena's sanitized name
     *
     * @return <p>This arena's sanitized name</p>
     */
    @NotNull
    String getArenaNameSanitized();

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
    boolean willCauseWin(@NotNull Block block);

    /**
     * Gets whether standing on the given block should cause a loss
     *
     * @param block <p>The block to check</p>
     * @return <p>True if standing on the block will cause a loss</p>
     */
    boolean willCauseLoss(@NotNull Block block);

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
    @NotNull
    Location getSpawnLocation();

    /**
     * Gets this arena's exit location
     *
     * @return <p>This arena's exit location, or null if no such location is set.</p>
     */
    @Nullable
    Location getExitLocation();

    /**
     * Adds a reward to this arena
     *
     * @param rewardCondition <p>The condition for the reward to be granted</p>
     * @param reward          <p>The reward to be granted</p>
     */
    void addReward(@NotNull RewardCondition rewardCondition, @NotNull Reward reward);

    /**
     * Clears this arena's rewards for the given condition
     *
     * @param rewardCondition <p>The reward condition to clear all rewards for</p>
     */
    void clearRewards(@NotNull RewardCondition rewardCondition);

    /**
     * Gets all rewards for the given reward condition
     *
     * @param rewardCondition <p>The condition to get the rewards for</p>
     * @return <p>All rewards</p>
     */
    @NotNull
    Set<Reward> getRewards(RewardCondition rewardCondition);

}

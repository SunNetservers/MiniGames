package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.arena.reward.Reward;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * An interface describing an arena
 */
@SuppressWarnings("unused")
public interface Arena {

    /**
     * Gets the name of this arena
     *
     * @return <p>The name of this arena</p>
     */
    @NotNull
    String getArenaName();

    /**
     * Sets the name of this arena
     *
     * @param arenaName <p>The new name</p>
     * @return <p>True if successfully updated</p>
     */
    boolean setName(@NotNull String arenaName);

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
     * Gets the type of block a player has to hit to win this arena
     *
     * @return <p>The kind of block players must hit</p>
     */
    @NotNull
    Material getWinBlockType();

    /**
     * Sets the material of the win block type
     *
     * <p>The win block type is the type of block a player must hit to win in this arena</p>
     *
     * @param material <p>The material to set for the win block type</p>
     * @return <p>True if successfully updated</p>
     */
    boolean setWinBlockType(@NotNull Material material);

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
     * Sets the spawn location for this arena
     *
     * @param newLocation <p>The new spawn location</p>
     * @return <p>True if successfully updated</p>
     */
    boolean setSpawnLocation(@Nullable Location newLocation);

    /**
     * Gets this arena's exit location
     *
     * @return <p>This arena's exit location, or null if no such location is set.</p>
     */
    @Nullable
    Location getExitLocation();

    /**
     * Sets the exit location for this arena
     *
     * @param newLocation <p>The new exit location</p>
     * @return <p>True if successfully updated</p>
     */
    boolean setExitLocation(@Nullable Location newLocation);

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

    /**
     * Gets the maximum amount of players that can join this arena at once
     *
     * @return <p>The maximum amount of players</p>
     */
    int getMaxPlayers();

    /**
     * Sets the maximum amount of players that can join this arena at once
     *
     * @param newValue <p>The new maximum amount of players</p>
     */
    boolean setMaxPlayers(int newValue);

}

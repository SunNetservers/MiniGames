package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.arena.reward.Reward;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import net.knarcraft.minigames.config.DropperConfiguration;
import net.knarcraft.minigames.util.DropperArenaStorageHelper;
import net.knarcraft.minigames.util.StringSanitizer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import static net.knarcraft.minigames.util.InputValidationHelper.isInvalid;

/**
 * A representation of one dropper arena
 */
public class DropperArena implements Arena {

    /**
     * An unique and persistent identifier for this arena
     */
    private final UUID arenaId;

    /**
     * A name used when listing and storing this arena.
     */
    private @NotNull String arenaName;

    /**
     * The location players are teleported to when joining this arena.
     */
    private @NotNull Location spawnLocation;

    /**
     * The location players will be sent to when they win or lose the arena. If not set, their entry location should be
     * used instead.
     */
    private @Nullable Location exitLocation;

    /**
     * The velocity in the y-direction to apply to all players in this arena.
     */
    private double playerVerticalVelocity;

    /**
     * The velocity in the x-direction to apply to all players in this arena
     *
     * <p>This is technically the fly speed</p>
     */
    private float playerHorizontalVelocity;

    /**
     * The material of the block players have to hit to win this dropper arena
     */
    private @NotNull Material winBlockType;

    /**
     * The arena data for this arena
     */
    private final DropperArenaData dropperArenaData;

    private final DropperArenaHandler dropperArenaHandler;

    private Map<RewardCondition, Set<Reward>> rewards = new HashMap<>();

    private static final DropperConfiguration dropperConfiguration = MiniGames.getInstance().getDropperConfiguration();

    /**
     * Instantiates a new dropper arena
     *
     * @param arenaId                  <p>The id of the arena</p>
     * @param arenaName                <p>The name of the arena</p>
     * @param spawnLocation            <p>The location players spawn in when entering the arena</p>
     * @param exitLocation             <p>The location the players are teleported to when exiting the arena, or null</p>
     * @param playerVerticalVelocity   <p>The velocity to use for players' vertical velocity</p>
     * @param playerHorizontalVelocity <p>The velocity to use for players' horizontal velocity (-1 to 1)</p>
     * @param winBlockType             <p>The material of the block players have to hit to win this dropper arena</p>
     * @param rewards                  <p>The rewards given by this arena</p>
     * @param dropperArenaData         <p>The arena data keeping track of which players have done what in this arena</p>
     * @param arenaHandler             <p>The arena handler used for saving any changes</p>
     */
    public DropperArena(@NotNull UUID arenaId, @NotNull String arenaName, @NotNull Location spawnLocation,
                        @Nullable Location exitLocation, double playerVerticalVelocity, float playerHorizontalVelocity,
                        @NotNull Material winBlockType, @NotNull Map<RewardCondition, Set<Reward>> rewards,
                        @NotNull DropperArenaData dropperArenaData, @NotNull DropperArenaHandler arenaHandler) {
        this.arenaId = arenaId;
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = exitLocation;
        this.playerVerticalVelocity = playerVerticalVelocity;
        this.playerHorizontalVelocity = playerHorizontalVelocity;
        this.winBlockType = winBlockType;
        this.dropperArenaData = dropperArenaData;
        this.dropperArenaHandler = arenaHandler;
        this.rewards = rewards;
    }

    /**
     * Instantiates a new dropper arena
     *
     * <p>Note that this minimal constructor can be used to quickly create a new dropper arena at the player's given
     * location, simply by them giving an arena name.</p>
     *
     * @param arenaName     <p>The name of the arena</p>
     * @param spawnLocation <p>The location players spawn in when entering the arena</p>
     * @param arenaHandler  <p>The arena handler used for saving any changes</p>
     */
    public DropperArena(@NotNull String arenaName, @NotNull Location spawnLocation,
                        @NotNull DropperArenaHandler arenaHandler) {
        DropperConfiguration configuration = MiniGames.getInstance().getDropperConfiguration();
        this.arenaId = UUID.randomUUID();
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = null;
        this.playerVerticalVelocity = configuration.getVerticalVelocity();
        this.playerHorizontalVelocity = configuration.getHorizontalVelocity();

        Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries = new HashMap<>();
        for (ArenaGameMode arenaGameMode : DropperArenaGameMode.values()) {
            recordRegistries.put(arenaGameMode, new DropperArenaRecordsRegistry(this.arenaId));
        }

        this.dropperArenaData = new DropperArenaData(this.arenaId, recordRegistries, new HashMap<>());
        this.winBlockType = Material.WATER;
        this.dropperArenaHandler = arenaHandler;
    }

    @Override
    public @NotNull DropperArenaData getData() {
        return this.dropperArenaData;
    }

    @Override
    public @NotNull UUID getArenaId() {
        return this.arenaId;
    }

    @Override
    public @NotNull String getArenaName() {
        return this.arenaName;
    }

    @Override
    public @NotNull Location getSpawnLocation() {
        return this.spawnLocation.clone();
    }

    @Override
    public @Nullable Location getExitLocation() {
        return this.exitLocation != null ? this.exitLocation.clone() : null;
    }

    @Override
    public void addReward(@NotNull RewardCondition rewardCondition, @NotNull Reward reward) {
        this.rewards.computeIfAbsent(rewardCondition, k -> new HashSet<>());
        this.rewards.get(rewardCondition).add(reward);
        this.saveArena();
    }

    @Override
    public void clearRewards(@NotNull RewardCondition rewardCondition) {
        this.rewards.remove(rewardCondition);
        this.saveArena();
    }

    @Override
    public @NotNull Set<Reward> getRewards(RewardCondition rewardCondition) {
        if (this.rewards.containsKey(rewardCondition) && this.rewards.get(rewardCondition) != null) {
            return this.rewards.get(rewardCondition);
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Gets the vertical velocity for players in this arena
     *
     * <p>This velocity will be set on the negative y-axis, for all players in this arena.</p>
     *
     * @return <p>Players' velocity in this arena</p>
     */
    public double getPlayerVerticalVelocity() {
        return this.playerVerticalVelocity;
    }


    /**
     * Gets the horizontal for players in this arena
     *
     * <p>This will be used for players' fly-speed in this arena</p>
     *
     * @return <p>Players' velocity in this arena</p>
     */
    public float getPlayerHorizontalVelocity() {
        return this.playerHorizontalVelocity;
    }

    /**
     * Gets the type of block a player has to hit to win this arena
     *
     * @return <p>The kind of block players must hit</p>
     */
    public @NotNull Material getWinBlockType() {
        return this.winBlockType;
    }

    /**
     * Gets this arena's sanitized name
     *
     * @return <p>This arena's sanitized name</p>
     */
    public @NotNull String getArenaNameSanitized() {
        return StringSanitizer.sanitizeArenaName(this.getArenaName());
    }

    @Override
    public boolean removeData() {
        return DropperArenaStorageHelper.removeDropperArenaData(getArenaId());
    }

    @Override
    public boolean saveData() {
        try {
            DropperArenaStorageHelper.saveDropperArenaData(getData());
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    @Override
    public boolean willCauseWin(Block block) {
        return block.getType() == winBlockType;
    }

    @Override
    public boolean willCauseLoss(Block block) {
        return !dropperConfiguration.getBlockWhitelist().contains(block.getType());
    }

    @Override
    public boolean winLocationIsSolid() {
        return winBlockType.isSolid();
    }

    /**
     * Sets the spawn location for this arena
     *
     * @param newLocation <p>The new spawn location</p>
     * @return <p>True if successfully updated</p>
     */
    public boolean setSpawnLocation(@NotNull Location newLocation) {
        if (isInvalid(newLocation)) {
            return false;
        } else {
            this.spawnLocation = newLocation;
            this.saveArena();
            return true;
        }
    }

    /**
     * Sets the exit location for this arena
     *
     * @param newLocation <p>The new exit location</p>
     * @return <p>True if successfully updated</p>
     */
    public boolean setExitLocation(@NotNull Location newLocation) {
        if (isInvalid(newLocation)) {
            return false;
        } else {
            this.exitLocation = newLocation;
            this.saveArena();
            return true;
        }
    }

    /**
     * Sets the name of this arena
     *
     * @param arenaName <p>The new name</p>
     * @return <p>True if successfully updated</p>
     */
    public boolean setName(@NotNull String arenaName) {
        if (!arenaName.isBlank()) {
            String oldName = this.getArenaNameSanitized();
            this.arenaName = arenaName;
            // Update the arena lookup map to make sure the new name can be used immediately
            this.dropperArenaHandler.updateLookupName(oldName, this.getArenaNameSanitized());
            this.saveArena();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the material of the win block type
     *
     * <p>The win block type is the type of block a player must hit to win in this arena</p>
     *
     * @param material <p>The material to set for the win block type</p>
     * @return <p>True if successfully updated</p>
     */
    public boolean setWinBlockType(@NotNull Material material) {
        if (material.isAir() || !material.isBlock()) {
            return false;
        } else {
            this.winBlockType = material;
            this.saveArena();
            return true;
        }
    }

    /**
     * Sets the horizontal velocity of this arena's players
     *
     * <p>Note: It's assumed the given value is already bound-checked! (-1 to 1)</p>
     *
     * @param horizontalVelocity <p>The horizontal velocity to use</p>
     * @return <p>True if successfully updated</p>
     */
    public boolean setHorizontalVelocity(float horizontalVelocity) {
        if (horizontalVelocity < -1 || horizontalVelocity > 1) {
            return false;
        } else {
            this.playerHorizontalVelocity = horizontalVelocity;
            this.saveArena();
            return true;
        }
    }

    /**
     * Sets the vertical velocity of this arena's players
     *
     * @param verticalVelocity <p>The vertical velocity to use</p>
     * @return <p>True if successfully updated</p>
     */
    public boolean setVerticalVelocity(double verticalVelocity) {
        if (verticalVelocity <= 0 || verticalVelocity > 100) {
            return false;
        } else {
            this.playerVerticalVelocity = verticalVelocity;
            this.saveArena();
            return true;
        }
    }

    /**
     * Saves this arena to disk
     */
    public void saveArena() {
        try {
            DropperArenaStorageHelper.saveSingleDropperArena(this);
        } catch (IOException exception) {
            MiniGames.log(Level.SEVERE, "Unable to save arena! " +
                    "Data loss can occur!");
            MiniGames.log(Level.SEVERE, exception.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DropperArena otherArena)) {
            return false;
        }
        return this.getArenaNameSanitized().equals(otherArena.getArenaNameSanitized());
    }

}

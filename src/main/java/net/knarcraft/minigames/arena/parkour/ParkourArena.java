package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.knarlib.util.MaterialHelper;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.arena.reward.Reward;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import net.knarcraft.minigames.util.ParkourArenaStorageHelper;
import net.knarcraft.minigames.util.StringSanitizer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import static net.knarcraft.minigames.util.InputValidationHelper.isInvalid;

/**
 * A representation of one parkour arena
 */
public class ParkourArena implements Arena {

    /**
     * An unique and persistent identifier for this arena
     */
    private final @NotNull UUID arenaId;

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
     * The material of the block players have to hit to win this parkour arena
     */
    private @NotNull Material winBlockType;

    /**
     * The location the player has to reach to win. If not set, winBlockType is used instead
     */
    private @Nullable Location winLocation;

    /**
     * The names of the block types constituting this arena's kill plane
     */
    private @Nullable Set<String> killPlaneBlockNames;

    /**
     * The block types constituting this arena's kill plane
     */
    private @Nullable Set<Material> killPlaneBlocks;

    /**
     * The names of the block types serving as obstacles for this arena
     */
    private @Nullable Set<String> obstacleBlockNames;

    /**
     * The block types serving as obstacles for this arena
     */
    private @Nullable Set<Material> obstacleBlocks;

    /**
     * The checkpoints for this arena. Entering a checkpoint overrides the player's spawn location.
     */
    private final @NotNull List<Location> checkpoints;

    /**
     * The arena data for this arena
     */
    private final @NotNull ParkourArenaData parkourArenaData;

    private final @NotNull ParkourArenaHandler parkourArenaHandler;

    private Map<RewardCondition, Set<Reward>> rewards = new HashMap<>();

    /**
     * Instantiates a new parkour arena
     *
     * @param arenaId             <p>The id of the arena</p>
     * @param arenaName           <p>The name of the arena</p>
     * @param spawnLocation       <p>The location players spawn in when entering the arena</p>
     * @param exitLocation        <p>The location the players are teleported to when exiting the arena, or null</p>
     * @param winBlockType        <p>The material of the block players have to hit to win this parkour arena</p>
     * @param winLocation         <p>The location a player has to reach to win this arena</p>
     * @param killPlaneBlockNames <p>The names of the types of blocks that trigger a loss when stepped on</p>
     * @param obstacleBlockNames  <p>The names of the types of blocks that trigger a loss when touched</p>
     * @param checkpoints         <p>The checkpoints set for this arena</p>
     * @param rewards             <p>The rewards given by this arena</p>
     * @param parkourArenaData    <p>The arena data keeping track of which players have done what in this arena</p>
     * @param arenaHandler        <p>The arena handler used for saving any changes</p>
     */
    public ParkourArena(@NotNull UUID arenaId, @NotNull String arenaName, @NotNull Location spawnLocation,
                        @Nullable Location exitLocation, @NotNull Material winBlockType, @Nullable Location winLocation,
                        @Nullable Set<String> killPlaneBlockNames, @Nullable Set<String> obstacleBlockNames,
                        @NotNull List<Location> checkpoints,
                        @NotNull Map<RewardCondition, Set<Reward>> rewards,
                        @NotNull ParkourArenaData parkourArenaData, @NotNull ParkourArenaHandler arenaHandler) {
        this.arenaId = arenaId;
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = exitLocation;
        this.winBlockType = winBlockType;
        this.winLocation = winLocation;
        this.killPlaneBlockNames = killPlaneBlockNames;
        this.killPlaneBlocks = this.killPlaneBlockNames == null ? null : MaterialHelper.loadMaterialList(
                new ArrayList<>(killPlaneBlockNames), "+", MiniGames.getInstance().getLogger());
        this.obstacleBlockNames = obstacleBlockNames;
        this.obstacleBlocks = this.obstacleBlockNames == null ? null : MaterialHelper.loadMaterialList(
                new ArrayList<>(obstacleBlockNames), "+", MiniGames.getInstance().getLogger());
        this.checkpoints = checkpoints;
        this.parkourArenaData = parkourArenaData;
        this.parkourArenaHandler = arenaHandler;
        this.rewards = rewards;
    }

    /**
     * Instantiates a new parkour arena
     *
     * <p>Note that this minimal constructor can be used to quickly create a new parkour arena at the player's given
     * location, simply by them giving an arena name.</p>
     *
     * @param arenaName     <p>The name of the arena</p>
     * @param spawnLocation <p>The location players spawn in when entering the arena</p>
     * @param arenaHandler  <p>The arena handler used for saving any changes</p>
     */
    public ParkourArena(@NotNull String arenaName, @NotNull Location spawnLocation,
                        @NotNull ParkourArenaHandler arenaHandler) {
        this.arenaId = UUID.randomUUID();
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = null;
        this.winLocation = null;

        Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries = new HashMap<>();
        for (ParkourArenaGameMode arenaGameMode : ParkourArenaGameMode.values()) {
            recordRegistries.put(arenaGameMode, new ParkourArenaRecordsRegistry(this.arenaId));
        }

        this.parkourArenaData = new ParkourArenaData(this.arenaId, recordRegistries, new HashMap<>());
        this.winBlockType = Material.EMERALD_BLOCK;
        this.killPlaneBlocks = null;
        this.obstacleBlocks = null;
        this.checkpoints = new ArrayList<>();
        this.parkourArenaHandler = arenaHandler;
    }

    @Override
    public @NotNull ParkourArenaData getData() {
        return this.parkourArenaData;
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
        return this.spawnLocation;
    }

    @Override
    public @Nullable Location getExitLocation() {
        return this.exitLocation;
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
        if (this.rewards.containsKey(rewardCondition)) {
            return this.rewards.get(rewardCondition);
        } else {
            return new HashSet<>();
        }
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
     * The location a player has to reach to win this arena
     *
     * <p></p>
     *
     * @return <p>The win trigger's location</p>
     */
    public @Nullable Location getWinLocation() {
        return this.winLocation != null ? this.winLocation.clone() : null;
    }

    /**
     * Gets the block types used for this parkour arena's kill plane
     *
     * @return <p>The types of blocks that cause a loss</p>
     */
    public @NotNull Set<Material> getKillPlaneBlocks() {
        if (this.killPlaneBlocks != null) {
            return new HashSet<>(this.killPlaneBlocks);
        } else {
            return MiniGames.getInstance().getParkourConfiguration().getKillPlaneBlocks();
        }
    }

    /**
     * Gets the names of the block types used for this parkour arena's kill plane
     *
     * @return <p>The names of the types of blocks that cause a loss</p>
     */
    public @Nullable Set<String> getKillPlaneBlockNames() {
        return this.killPlaneBlockNames;
    }

    /**
     * Gets the block types used for this parkour arena's obstacle blocks
     *
     * @return <p>The types of blocks used as obstacles</p>
     */
    public @NotNull Set<Material> getObstacleBlocks() {
        if (this.obstacleBlocks != null) {
            return new HashSet<>(this.obstacleBlocks);
        } else {
            return MiniGames.getInstance().getParkourConfiguration().getObstacleBlocks();
        }
    }

    /**
     * Gets the names of the blocks used as this arena's obstacle blocks
     *
     * @return <p>The names of the blocks used as this arena's obstacle blocks</p>
     */
    public @Nullable Set<String> getObstacleBlockNames() {
        return this.obstacleBlockNames;
    }

    /**
     * Gets all checkpoint locations for this arena
     *
     * @return <p>All checkpoint locations for this arena</p>
     */
    public List<Location> getCheckpoints() {
        List<Location> copy = new ArrayList<>(this.checkpoints.size());
        for (Location location : this.checkpoints) {
            copy.add(location.clone());
        }
        return copy;
    }

    /**
     * Gets whether this arena has no checkpoints
     *
     * @return <p>True if this arena has no checkpoints</p>
     */
    public boolean hasNoCheckpoints() {
        return this.checkpoints.isEmpty();
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
        return ParkourArenaStorageHelper.removeParkourArenaData(getArenaId());
    }

    @Override
    public boolean saveData() {
        try {
            ParkourArenaStorageHelper.saveParkourArenaData(getData());
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    @Override
    public boolean willCauseWin(Block block) {
        return (this.winLocation != null && this.winLocation.getBlock().equals(block)) ||
                (this.winLocation == null && this.winBlockType == block.getType());
    }

    @Override
    public boolean willCauseLoss(Block block) {
        return this.getKillPlaneBlocks().contains(block.getType()) || this.getObstacleBlocks().contains(block.getType());
    }

    @Override
    public boolean winLocationIsSolid() {
        return (this.winLocation != null && this.winLocation.getBlock().getType().isSolid()) ||
                this.winBlockType.isSolid();
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
            this.parkourArenaHandler.updateLookupName(oldName, this.getArenaNameSanitized());
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
     * Sets the location players need to reach to win this arena
     *
     * @param newLocation <p>The location players have to reach</p>
     * @return <p>True if successfully changed</p>
     */
    public boolean setWinLocation(@NotNull Location newLocation) {
        if (isInvalid(newLocation)) {
            return false;
        } else {
            this.winLocation = newLocation.clone();
            this.saveArena();
            return true;
        }
    }

    /**
     * Sets the type of blocks constituting this arena's kill plane
     *
     * @param killPlaneBlockNames <p>The names of the blocks that will cause players to lose</p>
     */
    public boolean setKillPlaneBlocks(@NotNull Set<String> killPlaneBlockNames) {
        if (killPlaneBlockNames.isEmpty()) {
            this.killPlaneBlockNames = null;
            this.killPlaneBlocks = null;
        } else {
            Set<Material> parsed = MaterialHelper.loadMaterialList(new ArrayList<>(killPlaneBlockNames), "+",
                    MiniGames.getInstance().getLogger());
            if (parsed.isEmpty()) {
                return false;
            }
            this.killPlaneBlockNames = killPlaneBlockNames;
            this.killPlaneBlocks = parsed;
        }
        this.saveArena();
        return true;
    }

    /**
     * Sets the type of blocks used as obstacle blocks
     *
     * @param obstacleBlockNames <p>The names of the obstacle blocks</p>
     */
    public boolean setObstacleBlocks(@NotNull Set<String> obstacleBlockNames) {
        if (obstacleBlockNames.isEmpty()) {
            this.obstacleBlockNames = null;
            this.obstacleBlocks = null;
        } else {
            Set<Material> parsed = MaterialHelper.loadMaterialList(new ArrayList<>(obstacleBlockNames), "+",
                    MiniGames.getInstance().getLogger());
            if (parsed.isEmpty()) {
                return false;
            }
            this.obstacleBlockNames = obstacleBlockNames;
            this.obstacleBlocks = parsed;
        }
        this.saveArena();
        return true;
    }

    /**
     * Adds a checkpoint to this arena
     *
     * @param checkpoint <p>The checkpoint to add</p>
     * @return <p>True if successfully added</p>
     */
    public boolean addCheckpoint(@NotNull Location checkpoint) {
        if (isInvalid(checkpoint)) {
            return false;
        }

        this.checkpoints.add(checkpoint.clone());
        this.saveArena();
        return true;
    }

    /**
     * Clears all checkpoints from this arena
     *
     * @return <p>True if successfully cleared</p>
     */
    public boolean clearCheckpoints() {
        if (checkpoints.isEmpty()) {
            return false;
        }

        this.checkpoints.clear();
        this.saveArena();
        return true;
    }

    /**
     * Saves this arena to disk
     */
    public void saveArena() {
        try {
            ParkourArenaStorageHelper.saveSingleParkourArena(this);
        } catch (IOException exception) {
            MiniGames.log(Level.SEVERE, "Unable to save arena! " +
                    "Data loss can occur!");
            MiniGames.log(Level.SEVERE, exception.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ParkourArena otherArena)) {
            return false;
        }
        return this.getArenaNameSanitized().equals(otherArena.getArenaNameSanitized());
    }

}

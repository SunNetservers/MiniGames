package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.util.MaterialHelper;
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
     * The checkpoints for this arena. Entering a checkpoint overrides the player's spawn location.
     */
    private final @NotNull List<Location> checkpoints;

    /**
     * The arena data for this arena
     */
    private final @NotNull ParkourArenaData parkourArenaData;

    private final @NotNull ParkourArenaHandler parkourArenaHandler;

    /**
     * Instantiates a new parkour arena
     *
     * @param arenaId          <p>The id of the arena</p>
     * @param arenaName        <p>The name of the arena</p>
     * @param spawnLocation    <p>The location players spawn in when entering the arena</p>
     * @param exitLocation     <p>The location the players are teleported to when exiting the arena, or null</p>
     * @param winBlockType     <p>The material of the block players have to hit to win this parkour arena</p>
     * @param winLocation      <p>The location a player has to reach to win this arena</p>
     * @param parkourArenaData <p>The arena data keeping track of which players have done what in this arena</p>
     * @param arenaHandler     <p>The arena handler used for saving any changes</p>
     */
    public ParkourArena(@NotNull UUID arenaId, @NotNull String arenaName, @NotNull Location spawnLocation,
                        @Nullable Location exitLocation, @NotNull Material winBlockType, @Nullable Location winLocation,
                        @Nullable Set<String> killPlaneBlockNames, @NotNull List<Location> checkpoints,
                        @NotNull ParkourArenaData parkourArenaData, @NotNull ParkourArenaHandler arenaHandler) {
        this.arenaId = arenaId;
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = exitLocation;
        this.winBlockType = winBlockType;
        this.winLocation = winLocation;
        this.killPlaneBlockNames = killPlaneBlockNames;
        this.killPlaneBlocks = this.killPlaneBlockNames == null ? null : MaterialHelper.loadMaterialList(
                new ArrayList<>(killPlaneBlockNames));
        this.checkpoints = checkpoints;
        this.parkourArenaData = parkourArenaData;
        this.parkourArenaHandler = arenaHandler;
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

    /**
     * Gets this arena's exit location
     *
     * @return <p>This arena's exit location, or null if no such location is set.</p>
     */
    public @Nullable Location getExitLocation() {
        return this.exitLocation;
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
        } catch (IOException e) {
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
        return this.getKillPlaneBlocks().contains(block.getType());
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
            this.parkourArenaHandler.saveArenas();
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
            this.parkourArenaHandler.saveArenas();
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
            this.parkourArenaHandler.saveArenas();
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
            this.parkourArenaHandler.saveArenas();
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
            this.parkourArenaHandler.saveArenas();
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
            this.killPlaneBlocks = null;
        } else {
            Set<Material> parsed = MaterialHelper.loadMaterialList(new ArrayList<>(killPlaneBlockNames));
            if (parsed.isEmpty()) {
                return false;
            }
            this.killPlaneBlocks = parsed;
        }
        this.parkourArenaHandler.saveArenas();
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
        this.parkourArenaHandler.saveArenas();
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
        this.parkourArenaHandler.saveArenas();
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ParkourArena otherArena)) {
            return false;
        }
        return this.getArenaNameSanitized().equals(otherArena.getArenaNameSanitized());
    }

}

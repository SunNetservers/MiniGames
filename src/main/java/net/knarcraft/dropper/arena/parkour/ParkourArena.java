package net.knarcraft.dropper.arena.parkour;

import net.knarcraft.dropper.util.StringSanitizer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.knarcraft.dropper.util.InputValidationHelper.isInvalid;

/**
 * A representation of one dropper arena
 */
public class ParkourArena {

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
     * The material of the block players have to hit to win this dropper arena
     */
    private @NotNull Material winBlockType;

    /**
     * The location the player has to reach to win. If not set, winBlockType is used instead
     */
    private @Nullable Location winLocation;

    /**
     * The checkpoints for this arena. Entering a checkpoint overrides the player's spawn location.
     */
    private @Nullable List<Location> checkpoints;

    /**
     * The arena data for this arena
     */
    private final ParkourArenaData parkourArenaData;

    private final ParkourArenaHandler parkourArenaHandler;

    /**
     * Instantiates a new dropper arena
     *
     * @param arenaId          <p>The id of the arena</p>
     * @param arenaName        <p>The name of the arena</p>
     * @param spawnLocation    <p>The location players spawn in when entering the arena</p>
     * @param exitLocation     <p>The location the players are teleported to when exiting the arena, or null</p>
     * @param winBlockType     <p>The material of the block players have to hit to win this dropper arena</p>
     * @param parkourArenaData <p>The arena data keeping track of which players have done what in this arena</p>
     * @param arenaHandler     <p>The arena handler used for saving any changes</p>
     */
    public ParkourArena(@NotNull UUID arenaId, @NotNull String arenaName, @NotNull Location spawnLocation,
                        @Nullable Location exitLocation, @NotNull Material winBlockType,
                        @NotNull ParkourArenaData parkourArenaData, @NotNull ParkourArenaHandler arenaHandler) {
        this.arenaId = arenaId;
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = exitLocation;
        this.winBlockType = winBlockType;
        this.parkourArenaData = parkourArenaData;
        this.parkourArenaHandler = arenaHandler;
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
    public ParkourArena(@NotNull String arenaName, @NotNull Location spawnLocation,
                        @NotNull ParkourArenaHandler arenaHandler) {
        this.arenaId = UUID.randomUUID();
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = null;
        this.winLocation = null;

        Map<ParkourArenaGameMode, ParkourArenaRecordsRegistry> recordRegistries = new HashMap<>();
        for (ParkourArenaGameMode arenaGameMode : ParkourArenaGameMode.values()) {
            recordRegistries.put(arenaGameMode, new ParkourArenaRecordsRegistry(this.arenaId));
        }

        this.parkourArenaData = new ParkourArenaData(this.arenaId, recordRegistries, new HashMap<>());
        this.winBlockType = Material.EMERALD_BLOCK;
        this.parkourArenaHandler = arenaHandler;
    }

    /**
     * Gets this arena's data
     *
     * @return <p>This arena's data</p>
     */
    public @NotNull ParkourArenaData getData() {
        return this.parkourArenaData;
    }

    /**
     * Gets the id of this arena
     *
     * @return <p>This arena's identifier</p>
     */
    public @NotNull UUID getArenaId() {
        return this.arenaId;
    }

    /**
     * Gets the name of this arena
     *
     * @return <p>The name of this arena</p>
     */
    public @NotNull String getArenaName() {
        return this.arenaName;
    }

    /**
     * Gets this arena's spawn location
     *
     * <p>The spawn location is the location every player starts from when entering the dropper.</p>
     *
     * @return <p>This arena's spawn location.</p>
     */
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
     * Gets this arena's sanitized name
     *
     * @return <p>This arena's sanitized name</p>
     */
    public @NotNull String getArenaNameSanitized() {
        return StringSanitizer.sanitizeArenaName(this.getArenaName());
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
            parkourArenaHandler.saveArenas();
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
            parkourArenaHandler.saveArenas();
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
            parkourArenaHandler.updateLookupName(oldName, this.getArenaNameSanitized());
            parkourArenaHandler.saveArenas();
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
            parkourArenaHandler.saveArenas();
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
            this.exitLocation = newLocation;
            parkourArenaHandler.saveArenas();
            return true;
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

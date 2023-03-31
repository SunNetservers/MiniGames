package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.property.ArenaGameMode;
import net.knarcraft.dropper.util.StringSanitizer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A representation of one dropper arena
 */
public class DropperArena {

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

    private static DropperArenaHandler dropperArenaHandler = null;

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
     * @param dropperArenaData         <p>The arena data keeping track of which players have done what in this arena</p>
     */
    public DropperArena(@NotNull UUID arenaId, @NotNull String arenaName, @NotNull Location spawnLocation,
                        @Nullable Location exitLocation, double playerVerticalVelocity, float playerHorizontalVelocity,
                        @NotNull Material winBlockType, @NotNull DropperArenaData dropperArenaData) {
        this.arenaId = arenaId;
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = exitLocation;
        this.playerVerticalVelocity = playerVerticalVelocity;
        this.playerHorizontalVelocity = playerHorizontalVelocity;
        this.winBlockType = winBlockType;
        this.dropperArenaData = dropperArenaData;

        if (dropperArenaHandler == null) {
            dropperArenaHandler = Dropper.getInstance().getArenaHandler();
        }
    }

    /**
     * Instantiates a new dropper arena
     *
     * <p>Note that this minimal constructor can be used to quickly create a new dropper arena at the player's given
     * location, simply by them giving an arena name.</p>
     *
     * @param arenaName     <p>The name of the arena</p>
     * @param spawnLocation <p>The location players spawn in when entering the arena</p>
     */
    public DropperArena(@NotNull String arenaName, @NotNull Location spawnLocation) {
        this.arenaId = UUID.randomUUID();
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = null;
        this.playerVerticalVelocity = 3.92;
        this.playerHorizontalVelocity = 1;

        Map<ArenaGameMode, DropperArenaRecordsRegistry> recordRegistries = new HashMap<>();
        for (ArenaGameMode arenaGameMode : ArenaGameMode.values()) {
            recordRegistries.put(arenaGameMode, new DropperArenaRecordsRegistry(this.arenaId));
        }

        this.dropperArenaData = new DropperArenaData(this.arenaId, recordRegistries, new HashMap<>());
        this.winBlockType = Material.WATER;
    }

    /**
     * Gets this arena's data
     *
     * @return <p>This arena's data</p>
     */
    public @NotNull DropperArenaData getData() {
        return this.dropperArenaData;
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
            dropperArenaHandler.saveArenas();
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
            dropperArenaHandler.saveArenas();
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
            dropperArenaHandler.updateLookupName(oldName, this.getArenaNameSanitized());
            dropperArenaHandler.saveArenas();
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
            dropperArenaHandler.saveArenas();
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
            dropperArenaHandler.saveArenas();
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
            dropperArenaHandler.saveArenas();
            return true;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DropperArena otherArena)) {
            return false;
        }
        return this.getArenaNameSanitized().equals(otherArena.getArenaNameSanitized());
    }

    /**
     * Checks whether the given location is valid
     *
     * @param location <p>The location to validate</p>
     * @return <p>False if the location is valid</p>
     */
    private boolean isInvalid(Location location) {
        World world = location.getWorld();
        return world == null || !world.getWorldBorder().isInside(location);
    }

}

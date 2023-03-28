package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.util.ArenaStorageHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
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
    private final @NotNull String arenaName;

    /**
     * The location players are teleported to when joining this arena.
     */
    private final @NotNull Location spawnLocation;

    /**
     * The location players will be sent to when they win or lose the arena. If not set, their entry location should be
     * used instead.
     */
    private final @Nullable Location exitLocation;

    /**
     * The velocity in the y-direction to apply to all players in this arena.
     */
    private final double playerVerticalVelocity;

    /**
     * The velocity in the x-direction to apply to all players in this arena
     *
     * <p>This is technically the fly speed</p>
     */
    private final float playerHorizontalVelocity;

    /**
     * The material of the block players have to hit to win this dropper arena
     */
    private final @NotNull Material winBlockType;

    /**
     * The arena data for this arena
     */
    private final DropperArenaData dropperArenaData;

    //TODO: It should be possible to get records in sorted order (smallest to largest)

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
        this.playerVerticalVelocity = 1;
        this.playerHorizontalVelocity = 1;
        this.dropperArenaData = new DropperArenaData(this.arenaId, new DropperArenaRecordsRegistry(this.arenaId),
                new HashSet<>());
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
        return ArenaStorageHelper.sanitizeArenaName(this.getArenaName());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DropperArena otherArena)) {
            return false;
        }
        return this.getArenaNameSanitized().equals(otherArena.getArenaNameSanitized());
    }

}

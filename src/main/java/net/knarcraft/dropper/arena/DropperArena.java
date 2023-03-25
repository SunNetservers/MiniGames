package net.knarcraft.dropper.arena;

import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A representation of one dropper arena
 */
public class DropperArena {

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
    private final double playerHorizontalVelocity;

    /**
     * The stage number of this arena. If not null, the previous stage number must be cleared before access.
     */
    private final @Nullable Integer stage;

    /**
     * The registry used to save this arena's records
     */
    private final @NotNull DropperArenaRecordsRegistry recordsRegistry;

    /**
     * The material of the block players have to hit to win this dropper arena
     */
    private final @NotNull Material winBlockType;

    //TODO: Store records for this arena (maps with player->deaths/time). It should be possible to get those in sorted 
    // order (smallest to largest)

    /**
     * Instantiates a new dropper arena
     *
     * @param arenaName                <p>The name of the arena</p>
     * @param spawnLocation            <p>The location players spawn in when entering the arena</p>
     * @param exitLocation             <p>The location the players are teleported to when exiting the arena, or null</p>
     * @param playerVerticalVelocity   <p>The velocity to use for players' vertical velocity</p>
     * @param playerHorizontalVelocity <p>The velocity to use for players' horizontal velocity</p>
     * @param stage                    <p>The stage number of this stage, or null if not limited to stages</p>
     * @param winBlockType             <p>The material of the block players have to hit to win this dropper arena</p>
     * @param recordsRegistry          <p>The registry keeping track of all of this arena's records</p>
     */
    public DropperArena(@NotNull String arenaName, @NotNull Location spawnLocation, @Nullable Location exitLocation,
                        double playerVerticalVelocity, double playerHorizontalVelocity, @Nullable Integer stage, @NotNull Material winBlockType,
                        @NotNull DropperArenaRecordsRegistry recordsRegistry) {
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = exitLocation;
        this.playerVerticalVelocity = playerVerticalVelocity;
        this.playerHorizontalVelocity = playerHorizontalVelocity;
        this.stage = stage;
        this.winBlockType = winBlockType;
        this.recordsRegistry = recordsRegistry;
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
        this.arenaName = arenaName;
        this.spawnLocation = spawnLocation;
        this.exitLocation = null;
        this.playerVerticalVelocity = 1;
        this.playerHorizontalVelocity = 1;
        this.stage = null;
        this.recordsRegistry = new DropperArenaRecordsRegistry();
        this.winBlockType = Material.WATER;
    }

    /**
     * Gets the registry keeping track of this arena's records
     *
     * @return <p>This arena's record registry</p>
     */
    public @NotNull DropperArenaRecordsRegistry getRecordsRegistry() {
        return this.recordsRegistry;
    }

    /**
     * Gets the name of this arena
     *
     * @return <p>The name of this arena.</p>
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
    public double getPlayerHorizontalVelocity() {
        return this.playerHorizontalVelocity;
    }

    /**
     * Gets the stage this arena belongs to
     *
     * <p>It's assumed that arena stages go from 1,2,3,4,... and upwards. If the stage number is set, this arena can
     * only be played if all previous stages have been beaten. If not set, however, this arena can be used freely.</p>
     *
     * @return <p>This arena's stage number</p>
     */
    public @Nullable Integer getStage() {
        return this.stage;
    }

    /**
     * Gets the type of block a player has to hit to win this arena
     *
     * @return <p>The kind of block players must hit</p>
     */
    public @NotNull Material getWinBlockType() {
        return this.winBlockType;
    }

}

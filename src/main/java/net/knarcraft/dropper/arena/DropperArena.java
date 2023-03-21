package net.knarcraft.dropper.arena;

import org.bukkit.Location;

public class DropperArena {

    /**
     * A name used when listing this arena. Only used for differentiation.
     */
    private String arenaName;

    /**
     * The location players are teleported to when joining this arena.
     */
    private Location spawnLocation;

    /**
     * The location players will be sent to when they win or lose the arena. If not set, their entry location should be
     * used instead.
     */
    private Location exitLocation;

    /**
     * The velocity in the y-direction to apply to all players in this arena.
     */
    private double playerVelocity;

    /**
     * The stage number of this arena. If not null, the previous stage number must be cleared before access.
     */
    private Integer stage;

    //TODO: Add the appropriate getters/setters and other methods

}

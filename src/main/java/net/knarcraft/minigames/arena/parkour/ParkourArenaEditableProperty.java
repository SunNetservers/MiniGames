package net.knarcraft.minigames.arena.parkour;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * All editable properties of a parkour arena
 */
public enum ParkourArenaEditableProperty {

    /**
     * The name of the arena
     */
    NAME("name", ParkourArena::getArenaName),

    /**
     * The arena's spawn location
     */
    SPAWN_LOCATION("spawnLocation", (arena) -> String.valueOf(arena.getSpawnLocation())),

    /**
     * The arena's exit location
     */
    EXIT_LOCATION("exitLocation", (arena) -> String.valueOf(arena.getExitLocation())),

    /**
     * The arena's win block type
     */
    WIN_BLOCK_TYPE("winBlockType", (arena) -> arena.getWinBlockType().toString()),

    /**
     * The arena's win location (overrides the win block type)
     */
    WIN_LOCATION("winLocation", (arena) -> {
        if (arena.getWinLocation() != null) {
            return arena.getWinLocation().toString();
        } else {
            return "null";
        }
    }),

    /**
     * The arena's check points. Specifically used for adding.
     */
    CHECKPOINT_ADD("checkpointAdd", (arena) -> String.valueOf(arena.getCheckpoints())),

    /**
     * The arena's check points. Specifically used for clearing.
     */
    CHECKPOINT_CLEAR("checkpointClear", (arena) -> String.valueOf(arena.getCheckpoints())),

    /**
     * The blocks constituting the arena's lethal blocks
     */
    KILL_PLANE_BLOCKS("killPlaneBlocks", (arena) -> String.valueOf(arena.getKillPlaneBlockNames())),
    ;

    private final @NotNull String argumentString;
    private final Function<ParkourArena, String> currentValueProvider;

    /**
     * Instantiates a new arena editable property
     *
     * @param argumentString <p>The argument string used to specify this property</p>
     */
    ParkourArenaEditableProperty(@NotNull String argumentString, Function<ParkourArena, String> currentValueProvider) {
        this.argumentString = argumentString;
        this.currentValueProvider = currentValueProvider;
    }

    /**
     * Gets the string representation of this property's current value
     *
     * @param arena <p>The arena to check the value for</p>
     * @return <p>The current value as a string</p>
     */
    public String getCurrentValueAsString(ParkourArena arena) {
        return this.currentValueProvider.apply(arena);
    }

    /**
     * Gets the argument string used to specify this property
     *
     * @return <p>The argument string</p>
     */
    public @NotNull String getArgumentString() {
        return this.argumentString;
    }

    /**
     * Gets the editable property corresponding to the given argument string
     *
     * @param argumentString <p>The argument string used to specify an editable property</p>
     * @return <p>The corresponding editable property, or null if not found</p>
     */
    public static @Nullable ParkourArenaEditableProperty getFromArgumentString(String argumentString) {
        for (ParkourArenaEditableProperty property : ParkourArenaEditableProperty.values()) {
            if (property.argumentString.equalsIgnoreCase(argumentString)) {
                return property;
            }
        }
        return null;
    }

}

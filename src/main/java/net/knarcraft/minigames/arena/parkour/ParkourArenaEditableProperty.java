package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.arena.EditablePropertyType;
import net.knarcraft.minigames.util.ArenaStorageHelper;
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
    NAME("name", ParkourArena::getArenaName, EditablePropertyType.ARENA_NAME),

    /**
     * The arena's spawn location
     */
    SPAWN_LOCATION("spawnLocation", (arena) -> String.valueOf(arena.getSpawnLocation()),
            EditablePropertyType.LOCATION),

    /**
     * The arena's exit location
     */
    EXIT_LOCATION("exitLocation", (arena) -> String.valueOf(arena.getExitLocation()),
            EditablePropertyType.LOCATION),

    /**
     * The arena's win block type
     */
    WIN_BLOCK_TYPE("winBlockType", (arena) -> arena.getWinBlockType().toString(),
            EditablePropertyType.BLOCK_TYPE),

    /**
     * The arena's win location (overrides the win block type)
     */
    WIN_LOCATION("winLocation", (arena) -> {
        if (arena.getWinLocation() != null) {
            return arena.getWinLocation().toString();
        } else {
            return "null";
        }
    }, EditablePropertyType.LOCATION),

    /**
     * The arena's check points. Specifically used for adding.
     */
    CHECKPOINT_ADD("checkpointAdd", (arena) -> String.valueOf(arena.getCheckpoints()),
            EditablePropertyType.LOCATION),

    /**
     * The arena's check points. Specifically used for clearing.
     */
    CHECKPOINT_CLEAR("checkpointClear", (arena) -> String.valueOf(arena.getCheckpoints()),
            EditablePropertyType.CHECKPOINT_CLEAR),

    /**
     * The blocks constituting the arena's lethal blocks
     */
    KILL_PLANE_BLOCKS("killPlaneBlocks", (arena) -> String.valueOf(arena.getKillPlaneBlockNames()),
            EditablePropertyType.MATERIAL_LIST),

    /**
     * The blocks used as this arena's obstacle blocks
     */
    OBSTACLE_BLOCKS("obstacleBlocks", (arena) -> String.valueOf(arena.getObstacleBlockNames()),
            EditablePropertyType.MATERIAL_LIST),

    /**
     * The arena's max players
     */
    MAX_PLAYERS("maxPlayers", (arena) -> String.valueOf(arena.getMaxPlayers()),
            EditablePropertyType.INTEGER),

    /**
     * The arena's allowed damage causes
     */
    ALLOWED_DAMAGE_CAUSES("allowedDamageCauses", (arena) -> String.valueOf(
            ArenaStorageHelper.getDamageCauseNames(arena.getAllowedDamageCauses())),
            EditablePropertyType.DAMAGE_CAUSE_LIST),

    /**
     * The arena's loss trigger damage causes
     */
    LOSS_TRIGGER_DAMAGE_CAUSES("lossTriggerDamageCauses", (arena) -> String.valueOf(
            ArenaStorageHelper.getDamageCauseNames(arena.getLossTriggerDamageCauses())),
            EditablePropertyType.DAMAGE_CAUSE_LIST),
    ;

    private final @NotNull String argumentString;
    private final Function<ParkourArena, String> currentValueProvider;
    private final EditablePropertyType propertyType;

    /**
     * Instantiates a new arena editable property
     *
     * @param argumentString <p>The argument string used to specify this property</p>
     */
    ParkourArenaEditableProperty(@NotNull String argumentString, Function<ParkourArena, String> currentValueProvider,
                                 EditablePropertyType propertyType) {
        this.argumentString = argumentString;
        this.currentValueProvider = currentValueProvider;
        this.propertyType = propertyType;
    }

    /**
     * Gets the type of property this editable property represents
     *
     * @return <p>The type of this property</p>
     */
    public EditablePropertyType getPropertyType() {
        return this.propertyType;
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

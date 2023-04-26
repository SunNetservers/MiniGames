package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.arena.EditablePropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * All editable properties of a dropper arena
 */
public enum DropperArenaEditableProperty {

    /**
     * The name of the arena
     */
    NAME("name", DropperArena::getArenaName, EditablePropertyType.ARENA_NAME),

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
     * The arena's vertical velocity
     */
    VERTICAL_VELOCITY("verticalVelocity", (arena) -> String.valueOf(arena.getPlayerVerticalVelocity()),
            EditablePropertyType.VERTICAL_VELOCITY),

    /**
     * The arena's horizontal velocity
     */
    HORIZONTAL_VELOCITY("horizontalVelocity", (arena) -> String.valueOf(arena.getPlayerHorizontalVelocity()),
            EditablePropertyType.HORIZONTAL_VELOCITY),

    /**
     * The arena's win block type
     */
    WIN_BLOCK_TYPE("winBlockType", (arena) -> arena.getWinBlockType().toString(),
            EditablePropertyType.BLOCK_TYPE),
    ;

    private final @NotNull String argumentString;
    private final Function<DropperArena, String> currentValueProvider;
    private final EditablePropertyType propertyType;

    /**
     * Instantiates a new arena editable property
     *
     * @param argumentString <p>The argument string used to specify this property</p>
     */
    DropperArenaEditableProperty(@NotNull String argumentString, Function<DropperArena, String> currentValueProvider,
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
    public String getCurrentValueAsString(DropperArena arena) {
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
    public static @Nullable DropperArenaEditableProperty getFromArgumentString(String argumentString) {
        for (DropperArenaEditableProperty property : DropperArenaEditableProperty.values()) {
            if (property.argumentString.equalsIgnoreCase(argumentString)) {
                return property;
            }
        }
        return null;
    }

}

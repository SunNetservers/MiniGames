package net.knarcraft.dropper.property;

import net.knarcraft.dropper.arena.DropperArena;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * All editable properties of a dropper arena
 */
public enum ArenaEditableProperty {

    /**
     * The name of the arena
     */
    NAME("name", DropperArena::getArenaName),

    /**
     * The arena's spawn location
     */
    SPAWN_LOCATION("spawnLocation", (arena) -> String.valueOf(arena.getSpawnLocation())),

    /**
     * The arena's exit location
     */
    EXIT_LOCATION("exitLocation", (arena) -> String.valueOf(arena.getExitLocation())),

    /**
     * The arena's vertical velocity
     */
    VERTICAL_VELOCITY("verticalVelocity", (arena) -> String.valueOf(arena.getPlayerVerticalVelocity())),

    /**
     * The arena's horizontal velocity
     */
    HORIZONTAL_VELOCITY("horizontalVelocity", (arena) -> String.valueOf(arena.getPlayerHorizontalVelocity())),

    /**
     * The arena's win block type
     */
    WIN_BLOCK_TYPE("winBlockType", (arena) -> arena.getWinBlockType().toString()),
    ;

    private final @NotNull String argumentString;
    private final Function<DropperArena, String> currentValueProvider;

    /**
     * Instantiates a new arena editable property
     *
     * @param argumentString <p>The argument string used to specify this property</p>
     */
    ArenaEditableProperty(@NotNull String argumentString, Function<DropperArena, String> currentValueProvider) {
        this.argumentString = argumentString;
        this.currentValueProvider = currentValueProvider;
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
    public static @Nullable ArenaEditableProperty getFromArgumentString(String argumentString) {
        for (ArenaEditableProperty property : ArenaEditableProperty.values()) {
            if (property.argumentString.equalsIgnoreCase(argumentString)) {
                return property;
            }
        }
        return null;
    }

}

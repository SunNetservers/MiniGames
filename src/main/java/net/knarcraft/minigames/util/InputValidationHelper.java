package net.knarcraft.minigames.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A helper class for validating whether given input is valid
 */
public final class InputValidationHelper {

    private InputValidationHelper() {

    }

    /**
     * Checks whether the given location is valid
     *
     * @param location <p>The location to validate</p>
     * @return <p>False if the location is valid</p>
     */
    public static boolean isInvalid(@Nullable Location location) {
        if (location == null) {
            return true;
        }
        World world = location.getWorld();
        return world == null || !world.getWorldBorder().isInside(location);
    }

    /**
     * Checks whether the given value can be considered "empty"
     *
     * @param value <p>The value to check</p>
     * @return <p>True if the value can be considered as empty</p>
     */
    public static boolean isEmptyValue(@NotNull String value) {
        return value.equalsIgnoreCase("null") || value.equalsIgnoreCase("0") ||
                value.equalsIgnoreCase("clear") || value.equalsIgnoreCase("none");
    }

}

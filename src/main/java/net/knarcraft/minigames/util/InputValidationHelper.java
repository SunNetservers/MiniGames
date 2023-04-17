package net.knarcraft.minigames.util;

import org.bukkit.Location;
import org.bukkit.World;

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
    public static boolean isInvalid(Location location) {
        World world = location.getWorld();
        return world == null || !world.getWorldBorder().isInside(location);
    }

}

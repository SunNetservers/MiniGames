package net.knarcraft.minigames.util;

import net.knarcraft.minigames.MiniGames;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

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
        return value.equalsIgnoreCase("null") || value.equalsIgnoreCase("clear") ||
                value.equalsIgnoreCase("none");
    }

    /**
     * Parses a set of damage causes from a set of damage cause names
     *
     * @param input <p>The damage cause names to parse</p>
     * @return <p>The resulting damage causes</p>
     */
    @NotNull
    public static Set<EntityDamageEvent.DamageCause> parseDamageCauses(@Nullable Set<String> input) {
        Set<EntityDamageEvent.DamageCause> output = new HashSet<>();

        if (input == null) {
            return output;
        }

        for (String causeName : input) {
            try {
                output.add(EntityDamageEvent.DamageCause.valueOf(causeName));
            } catch (IllegalArgumentException | NullPointerException exception) {
                MiniGames.log(Level.WARNING, "The damage cause " + causeName +
                        " is invalid, and will be ignored.");
            }
        }
        return output;
    }

}

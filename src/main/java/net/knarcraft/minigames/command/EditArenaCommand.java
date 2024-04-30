package net.knarcraft.minigames.command;

import net.knarcraft.minigames.config.DropperConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract arena edit command, implementing input validation
 */
public abstract class EditArenaCommand implements CommandExecutor {

    private final DropperConfiguration configuration;

    /**
     * Instantiates a new edit arena command
     *
     * @param configuration <p>The configuration to use</p>
     */
    public EditArenaCommand(DropperConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Parses the specified max players
     *
     * @param maxPlayers <p>The max players string to parse</p>
     * @return <p>The parsed value, or -1 if not parse-able</p>
     */
    protected int parseMaxPlayers(@NotNull String maxPlayers) {
        try {
            return Integer.parseInt(maxPlayers);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Sanitizes the player's specified vertical velocity
     *
     * @param velocityString <p>The string to parse into a velocity</p>
     * @return <p>The parsed velocity, defaulting to 0.5 if not parse-able</p>
     */
    protected double sanitizeVerticalVelocity(@NotNull String velocityString) {
        // Vertical velocity should not be negative, as it would make the player go upwards. There is technically not a
        // max speed limit, but setting it too high makes the arena unplayable
        double velocity;
        try {
            velocity = Double.parseDouble(velocityString);
        } catch (NumberFormatException exception) {
            velocity = configuration.getVerticalVelocity();
        }

        // Require at least speed of 0.001, and at most 75 blocks/s
        return Math.min(Math.max(velocity, 0.001), 75);
    }

    /**
     * Sanitizes the user's specified horizontal velocity
     *
     * @param velocityString <p>The string to parse into a velocity</p>
     * @return <p>The parsed velocity, defaulting to 1 if not parse-able</p>
     */
    protected float sanitizeHorizontalVelocity(@NotNull String velocityString) {
        // Horizontal velocity is valid between -1 and 1, where negative values swaps directions
        float velocity;
        try {
            velocity = Float.parseFloat(velocityString);
        } catch (NumberFormatException exception) {
            velocity = configuration.getHorizontalVelocity();
        }

        // If outside bonds, choose the most extreme value
        return Math.min(Math.max(0.1f, velocity), 1);
    }

    /**
     * Parses the given location string
     *
     * @param player         <p>The player changing a location</p>
     * @param locationString <p>The location string to parse</p>
     * @return <p>The parsed location, or the player's location if not parse-able</p>
     */
    protected @NotNull Location parseLocation(Player player, String locationString) {
        if ((locationString.trim() + ",").matches("([0-9]+.?[0-9]*,){3}")) {
            String[] parts = locationString.split(",");
            Location newLocation = player.getLocation().clone();
            newLocation.setX(Double.parseDouble(parts[0].trim()));
            newLocation.setY(Double.parseDouble(parts[1].trim()));
            newLocation.setZ(Double.parseDouble(parts[2].trim()));
            return newLocation;
        } else {
            return player.getLocation().clone();
        }
    }

    /**
     * Parses the given material name
     *
     * @param materialName <p>The material name to parse</p>
     * @return <p>The parsed material, or AIR if not valid</p>
     */
    protected @NotNull Material parseMaterial(String materialName) {
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            material = Material.AIR;
        }
        return material;
    }

}

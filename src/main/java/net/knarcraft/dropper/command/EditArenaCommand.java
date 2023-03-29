package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.property.ArenaEditableProperty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command for editing an existing dropper arena
 */
public class EditArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command must be used by a player");
            return false;
        }

        if (arguments.length < 2) {
            return false;
        }

        DropperArena specifiedArena = Dropper.getInstance().getArenaHandler().getArena(arguments[0]);
        if (specifiedArena == null) {
            commandSender.sendMessage("Unable to find the specified dropper arena.");
            return false;
        }

        ArenaEditableProperty editableProperty = ArenaEditableProperty.getFromArgumentString(arguments[1]);
        if (editableProperty == null) {
            commandSender.sendMessage("Unknown property specified.");
            return false;
        }

        String currentValueFormat = "Current value of %s is: %s";

        if (arguments.length < 3) {
            // Print the current value of the property
            String value = editableProperty.getCurrentValueAsString(specifiedArena);
            commandSender.sendMessage(String.format(currentValueFormat, editableProperty.getArgumentString(), value));
            return true;
        } else {
            boolean successful = changeValue(specifiedArena, editableProperty, arguments[2], player);
            if (successful) {
                player.sendMessage(String.format("Property %s changed to: %s", editableProperty, arguments[2]));
            } else {
                player.sendMessage("Unable to change the property. Make sure your input is valid!");
            }
            return successful;
        }
    }

    /**
     * Changes the given property to the given value
     *
     * @param arena    <p>The arena to change the property for</p>
     * @param property <p>The property to change</p>
     * @param value    <p>The new value of the property</p>
     * @param player   <p>The player trying to change the value</p>
     * @return <p>True if the value was successfully changed</p>
     */
    private boolean changeValue(@NotNull DropperArena arena, @NotNull ArenaEditableProperty property,
                                @NotNull String value, @NotNull Player player) {
        return switch (property) {
            case WIN_BLOCK_TYPE -> arena.setWinBlockType(parseMaterial(value));
            case HORIZONTAL_VELOCITY -> arena.setHorizontalVelocity(sanitizeHorizontalVelocity(value));
            case VERTICAL_VELOCITY -> arena.setVerticalVelocity(sanitizeVerticalVelocity(value));
            case SPAWN_LOCATION -> arena.setSpawnLocation(parseLocation(player, value));
            case NAME -> arena.setName(value);
            case EXIT_LOCATION -> arena.setExitLocation(parseLocation(player, value));
        };
    }

    /**
     * Sanitizes the player's specified vertical velocity
     *
     * @param velocityString <p>The string to parse into a velocity</p>
     * @return <p>The parsed velocity, defaulting to 0.5 if not parse-able</p>
     */
    private double sanitizeVerticalVelocity(@NotNull String velocityString) {
        // Vertical velocity should not be negative, as it would make the player go upwards. There is technically not a
        // max speed limit, but setting it too high makes the arena unplayable
        double velocity;
        try {
            velocity = Double.parseDouble(velocityString);
        } catch (NumberFormatException exception) {
            velocity = 0.5;
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
    private float sanitizeHorizontalVelocity(@NotNull String velocityString) {
        // Horizontal velocity is valid between -1 and 1, where negative values swaps directions
        float velocity;
        try {
            velocity = Float.parseFloat(velocityString);
        } catch (NumberFormatException exception) {
            velocity = 1;
        }

        // Make sure the velocity isn't exactly 0
        if (velocity == 0) {
            velocity = 0.5f;
        }

        // If outside bonds, choose the most extreme value
        return Math.min(Math.max(-1, velocity), 1);
    }

    /**
     * Parses the given location string
     *
     * @param player         <p>The player changing a location</p>
     * @param locationString <p>The location string to parse</p>
     * @return <p>The parsed location, or the player's location if not parse-able</p>
     */
    private @NotNull Location parseLocation(Player player, String locationString) {
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
    private @NotNull Material parseMaterial(String materialName) {
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            material = Material.AIR;
        }
        return material;
    }

}

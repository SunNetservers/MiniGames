package net.knarcraft.minigames.command.parkour;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaEditableProperty;
import net.knarcraft.minigames.config.MiniGameMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

/**
 * The command for editing an existing dropper arena
 */
public class EditParkourArenaCommand implements CommandExecutor {

    /**
     * Instantiates a new edit arena command
     */
    public EditParkourArenaCommand() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        if (!(commandSender instanceof Player player)) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_PLAYER_ONLY);
            return false;
        }

        if (arguments.length < 2) {
            return false;
        }

        ParkourArena specifiedArena = MiniGames.getInstance().getParkourArenaHandler().getArena(arguments[0]);
        if (specifiedArena == null) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_ARENA_NOT_FOUND);
            return false;
        }

        ParkourArenaEditableProperty editableProperty = ParkourArenaEditableProperty.getFromArgumentString(arguments[1]);
        if (editableProperty == null) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_UNKNOWN_PROPERTY);
            return false;
        }

        if (arguments.length < 3) {
            // Print the current value of the property
            String value = editableProperty.getCurrentValueAsString(specifiedArena);
            stringFormatter.displaySuccessMessage(commandSender, stringFormatter.replacePlaceholders(
                    MiniGameMessage.SUCCESS_CURRENT_VALUE, new String[]{"{property}", "{value}"},
                    new String[]{editableProperty.getArgumentString(), value}));
            return true;
        } else {
            boolean successful;
            try {
                successful = changeValue(specifiedArena, editableProperty, arguments[2], player);
            } catch (NumberFormatException exception) {
                successful = false;
            }
            if (successful) {
                stringFormatter.displaySuccessMessage(player, stringFormatter.replacePlaceholder(
                        MiniGameMessage.SUCCESS_PROPERTY_CHANGED, "{property}",
                        editableProperty.getArgumentString()));
            } else {
                stringFormatter.displayErrorMessage(player, MiniGameMessage.ERROR_PROPERTY_INPUT_INVALID);
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
     * @throws NumberFormatException <p>If unable to parse a given numeric value</p>
     */
    private boolean changeValue(@NotNull ParkourArena arena, @NotNull ParkourArenaEditableProperty property,
                                @NotNull String value, @NotNull Player player) throws NumberFormatException {
        return switch (property) {
            case WIN_BLOCK_TYPE -> arena.setWinBlockType(parseMaterial(value));
            case SPAWN_LOCATION -> arena.setSpawnLocation(parseLocation(player, value));
            case NAME -> arena.setName(value);
            case EXIT_LOCATION -> arena.setExitLocation(parseLocation(player, value));
            case WIN_LOCATION -> arena.setWinLocation(parseLocation(player, value));
            case CHECKPOINT_ADD -> arena.addCheckpoint(parseLocation(player, value));
            case CHECKPOINT_CLEAR -> arena.clearCheckpoints();
            case KILL_PLANE_BLOCKS -> arena.setKillPlaneBlocks(new HashSet<>(List.of(value.split(","))));
            case HORIZONTAL_KILL_PLANE_HIT_BOX -> arena.setHorizontalKillPlaneHitBox(Double.parseDouble(value));
        };
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

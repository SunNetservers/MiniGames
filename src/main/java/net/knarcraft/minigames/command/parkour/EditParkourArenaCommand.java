package net.knarcraft.minigames.command.parkour;

import net.knarcraft.knarlib.formatting.FormatBuilder;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaEditableProperty;
import net.knarcraft.minigames.command.EditArenaCommand;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.util.InputValidationHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command for editing an existing dropper arena
 */
public class EditParkourArenaCommand extends EditArenaCommand {

    /**
     * Instantiates a new edit arena command
     */
    public EditParkourArenaCommand() {
        super(null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (!(commandSender instanceof Player player)) {
            new FormatBuilder(MiniGameMessage.ERROR_PLAYER_ONLY).error(commandSender);
            return false;
        }

        if (arguments.length < 2) {
            return false;
        }

        ParkourArena specifiedArena = MiniGames.getInstance().getParkourArenaHandler().getArena(arguments[0]);
        if (specifiedArena == null) {
            new FormatBuilder(MiniGameMessage.ERROR_ARENA_NOT_FOUND).error(commandSender);
            return false;
        }

        ParkourArenaEditableProperty editableProperty = ParkourArenaEditableProperty.getFromArgumentString(arguments[1]);
        if (editableProperty == null) {
            new FormatBuilder(MiniGameMessage.ERROR_UNKNOWN_PROPERTY).error(commandSender);
            return false;
        }

        if (arguments.length < 3) {
            // Print the current value of the property
            String value = editableProperty.getCurrentValueAsString(specifiedArena);
            new FormatBuilder(MiniGameMessage.SUCCESS_CURRENT_VALUE).
                    replace("{property}", editableProperty.getArgumentString()).
                    replace("{value}", value).success(commandSender);
            return true;
        } else {
            boolean successful;
            try {
                successful = changeValue(specifiedArena, editableProperty, arguments[2], player);
            } catch (NumberFormatException exception) {
                successful = false;
            }
            if (successful) {
                new FormatBuilder(MiniGameMessage.SUCCESS_PROPERTY_CHANGED).replace("{property}",
                        editableProperty.getArgumentString()).success(player);
            } else {
                new FormatBuilder(MiniGameMessage.ERROR_PROPERTY_INPUT_INVALID).error(player);
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
            case KILL_PLANE_BLOCKS -> arena.setKillPlaneBlocks(asSet(value));
            case OBSTACLE_BLOCKS -> arena.setObstacleBlocks(asSet(value));
            case MAX_PLAYERS -> arena.setMaxPlayers(parseMaxPlayers(value));
            case ALLOWED_DAMAGE_CAUSES ->
                    arena.setAllowedDamageCauses(InputValidationHelper.parseDamageCauses(asSet(value)));
            case LOSS_TRIGGER_DAMAGE_CAUSES ->
                    arena.setLossTriggerDamageCauses(InputValidationHelper.parseDamageCauses(asSet(value)));
        };
    }

}

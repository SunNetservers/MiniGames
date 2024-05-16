package net.knarcraft.minigames.command.dropper;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaEditableProperty;
import net.knarcraft.minigames.command.EditArenaCommand;
import net.knarcraft.minigames.config.DropperConfiguration;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.util.InputValidationHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command for editing an existing dropper arena
 */
public class EditDropperArenaCommand extends EditArenaCommand {

    /**
     * Instantiates a new edit arena command
     *
     * @param configuration <p>The configuration to use</p>
     */
    public EditDropperArenaCommand(DropperConfiguration configuration) {
        super(configuration);
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

        DropperArena specifiedArena = MiniGames.getInstance().getDropperArenaHandler().getArena(arguments[0]);
        if (specifiedArena == null) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_ARENA_NOT_FOUND);
            return false;
        }

        DropperArenaEditableProperty editableProperty = DropperArenaEditableProperty.getFromArgumentString(arguments[1]);
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
            boolean successful = changeValue(specifiedArena, editableProperty, arguments[2], player);
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
     */
    private boolean changeValue(@NotNull DropperArena arena, @NotNull DropperArenaEditableProperty property,
                                @NotNull String value, @NotNull Player player) {
        return switch (property) {
            case WIN_BLOCK_TYPE -> arena.setWinBlockType(parseMaterial(value));
            case HORIZONTAL_VELOCITY -> arena.setHorizontalVelocity(sanitizeHorizontalVelocity(value));
            case VERTICAL_VELOCITY -> arena.setVerticalVelocity(sanitizeVerticalVelocity(value));
            case SPAWN_LOCATION -> arena.setSpawnLocation(parseLocation(player, value));
            case NAME -> arena.setName(value);
            case EXIT_LOCATION -> arena.setExitLocation(parseLocation(player, value));
            case MAX_PLAYERS -> arena.setMaxPlayers(parseMaxPlayers(value));
            case ALLOWED_DAMAGE_CAUSES ->
                    arena.setAllowedDamageCauses(InputValidationHelper.parseDamageCauses(asSet(value)));
            case LOSS_TRIGGER_DAMAGE_CAUSES ->
                    arena.setLossTriggerDamageCauses(InputValidationHelper.parseDamageCauses(asSet(value)));
        };
    }

}

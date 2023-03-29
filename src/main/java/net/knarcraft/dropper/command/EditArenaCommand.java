package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.property.ArenaEditableProperty;
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
        if (!(commandSender instanceof Player)) {
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
        } else {
            // TODO: Expect a new value for the option, which needs to be validated, and possibly set
        }
        return true;
    }

}

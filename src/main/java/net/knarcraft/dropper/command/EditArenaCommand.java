package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
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

        //TODO: If an arena name and a property is given, display the current value
        //TODO: If an arena name, a property and a value is given, check if it's valid, and update the property
        return false;
    }

}

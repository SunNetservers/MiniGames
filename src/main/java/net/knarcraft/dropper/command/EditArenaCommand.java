package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The command for editing an existing dropper arena
 */
public class EditArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        //TODO: Make sure the console cannot run this
        //TODO: If an arena name and a property is given, display the current value
        //TODO: If an arena name, a property and a value is given, check if it's valid, and update the property
        return false;
    }

}

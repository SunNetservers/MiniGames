package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The command for creating a new dropper arena
 */
public class CreateArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        //TODO: Make sure the console cannot run this
        //TODO: Make sure the arena name isn't a duplicate and doesn't contain any unwanted characters
        //TODO: Create a new arena
        //TODO: Register the new arena in the arena handler
        //TODO: Tell the user of success
        return false;
    }

}

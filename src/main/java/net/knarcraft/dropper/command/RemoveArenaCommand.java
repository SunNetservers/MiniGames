package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The method used for removing an existing arena
 */
public class RemoveArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        //TODO: Make sure to kick any playing players if the arena is currently in use, by triggering their sessions' 
        // triggerQuit() method
        //TODO: Remove the arena from DropperArenaHandler
        //TODO: Notify the user of success
        return false;
    }

}

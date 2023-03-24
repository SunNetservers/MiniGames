package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The command used to leave the current dropper arena
 */
public class LeaveArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        //TODO: Make sure the console cannot run this
        //TODO: If the player isn't currently in an arena, just display an error message
        //TODO: Trigger the player's session's triggerQuit() method
        return false;
    }

}

package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * A command for listing existing dropper arenas
 */
public class ListArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        //TODO: List all existing arenas, and possibly information about a specified arena
        return false;
    }

}

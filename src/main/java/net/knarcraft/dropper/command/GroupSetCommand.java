package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GroupSetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        // TODO: Check if the given group is valid
        // TODO: Try and get the group from ArenaHandler.getGroup
        // TODO: Create a new group if not found
        // TODO: Set the group of the arena
        // TODO: Announce success
        return false;
    }

}

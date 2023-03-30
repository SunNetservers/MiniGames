package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GroupListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        // TODO: Display all groups if no argument is specified.
        // TODO: If a group is set and it exists, list all arenas in the correct order, and numbered (the order denotes
        //  the order players need to complete the arenas in)
        return false;
    }

}

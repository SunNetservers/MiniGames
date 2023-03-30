package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GroupSwapCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        // TODO: Make sure the two arenas exist
        // TODO: Make sure the two arenas belong to the same group
        // TODO: Swap the order of the two groups
        // TODO: Announce success
        return false;
    }

}

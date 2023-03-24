package net.knarcraft.dropper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The tab-completer for the edit arena command
 */
public class EditArenaTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        //TODO: Tab-complete existing arena names
        //TODO: If an arena name is given, tab-complete change-able properties
        return null;
    }

}

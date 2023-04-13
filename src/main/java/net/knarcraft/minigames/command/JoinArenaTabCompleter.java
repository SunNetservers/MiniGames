package net.knarcraft.minigames.command;

import net.knarcraft.minigames.util.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The tab-completer for the join command
 */
public class JoinArenaTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return TabCompleteHelper.getArenas();
        } else if (arguments.length == 2) {
            List<String> gameModes = new ArrayList<>();
            gameModes.add("default");
            gameModes.add("inverted");
            gameModes.add("random");
            return gameModes;
        } else {
            return new ArrayList<>();
        }
    }

}

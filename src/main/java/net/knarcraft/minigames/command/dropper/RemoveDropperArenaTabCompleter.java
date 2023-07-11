package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.util.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.knarcraft.knarlib.util.TabCompletionHelper.filterMatchingContains;

/**
 * The tab-completer for the remove arena command
 */
public class RemoveDropperArenaTabCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return filterMatchingContains(TabCompleteHelper.getDropperArenas(), arguments[0]);
        } else {
            return new ArrayList<>();
        }
    }

}

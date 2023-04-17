package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.util.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The tab-completer for the edit arena command
 */
public class EditDropperArenaTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return TabCompleteHelper.getDropperArenas();
        } else if (args.length == 2) {
            return TabCompleteHelper.getDropperArenaProperties();
        } else if (args.length == 3) {
            //TODO: Tab-complete possible values for the given property
            return null;
        } else {
            return new ArrayList<>();
        }
    }

}

package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.arena.dropper.DropperArenaEditableProperty;
import net.knarcraft.minigames.util.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.knarcraft.minigames.util.TabCompleteHelper.filterMatchingContains;

/**
 * The tab-completer for the edit arena command
 */
public class EditDropperArenaTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return filterMatchingContains(TabCompleteHelper.getDropperArenas(), arguments[0]);
        } else if (arguments.length == 2) {
            return filterMatchingContains(TabCompleteHelper.getDropperArenaProperties(), arguments[1]);
        } else if (arguments.length == 3) {
            DropperArenaEditableProperty property = DropperArenaEditableProperty.getFromArgumentString(arguments[1]);
            if (property == null) {
                return new ArrayList<>();
            }
            return filterMatchingContains(TabCompleteHelper.getTabCompleteSuggestions(property.getPropertyType()),
                    arguments[2]);
        } else {
            return new ArrayList<>();
        }
    }

}

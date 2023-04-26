package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.arena.parkour.ParkourArenaEditableProperty;
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
public class EditParkourArenaTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return filterMatchingContains(TabCompleteHelper.getParkourArenas(), arguments[0]);
        } else if (arguments.length == 2) {
            return filterMatchingContains(TabCompleteHelper.getParkourArenaProperties(), arguments[1]);
        } else if (arguments.length == 3) {
            ParkourArenaEditableProperty property = ParkourArenaEditableProperty.getFromArgumentString(arguments[1]);
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

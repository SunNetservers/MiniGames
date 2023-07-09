package net.knarcraft.minigames.command.parkour;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.arena.parkour.ParkourArenaHandler;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.util.StringSanitizer;
import net.knarcraft.minigames.util.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.knarcraft.minigames.util.TabCompleteHelper.filterMatchingContains;

/**
 * The command for setting the group of an arena
 */
public class ParkourGroupSetCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (arguments.length < 2) {
            return false;
        }

        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        ParkourArenaHandler arenaHandler = MiniGames.getInstance().getParkourArenaHandler();

        ParkourArena specifiedArena = arenaHandler.getArena(arguments[0]);
        if (specifiedArena == null) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_ARENA_NOT_FOUND);
            return false;
        }

        String groupName = StringSanitizer.removeUnwantedCharacters(arguments[1]);

        if (groupName.isBlank()) {
            return false;
        }

        ParkourArenaGroup arenaGroup;
        if (groupName.equalsIgnoreCase("null") || groupName.equalsIgnoreCase("none")) {
            arenaGroup = null;
        } else {
            arenaGroup = arenaHandler.getGroup(groupName);
            if (arenaGroup == null) {
                arenaGroup = new ParkourArenaGroup(groupName);
            }
        }

        arenaHandler.setGroup(specifiedArena.getArenaId(), arenaGroup);

        stringFormatter.displaySuccessMessage(commandSender, MiniGameMessage.SUCCESS_ARENA_GROUP_UPDATED);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return filterMatchingContains(TabCompleteHelper.getParkourArenas(), arguments[0]);
        } else if (arguments.length == 2) {
            List<String> possibleValues = new ArrayList<>();
            possibleValues.add("none");
            possibleValues.add("GroupName");
            for (ParkourArenaGroup group : MiniGames.getInstance().getParkourArenaHandler().getAllGroups()) {
                possibleValues.add(group.getGroupName());
            }
            return filterMatchingContains(possibleValues, arguments[1]);
        } else {
            return new ArrayList<>();
        }
    }

}

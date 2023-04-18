package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import net.knarcraft.minigames.arena.dropper.DropperArenaHandler;
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
public class DropperGroupSetCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (arguments.length < 2) {
            return false;
        }

        DropperArenaHandler arenaHandler = MiniGames.getInstance().getDropperArenaHandler();

        DropperArena specifiedArena = arenaHandler.getArena(arguments[0]);
        if (specifiedArena == null) {
            commandSender.sendMessage("Unable to find the specified dropper arena.");
            return false;
        }

        String groupName = StringSanitizer.removeUnwantedCharacters(arguments[1]);

        if (groupName.isBlank()) {
            return false;
        }

        DropperArenaGroup arenaGroup;
        if (groupName.equalsIgnoreCase("null") || groupName.equalsIgnoreCase("none")) {
            arenaGroup = null;
        } else {
            arenaGroup = arenaHandler.getGroup(groupName);
            if (arenaGroup == null) {
                arenaGroup = new DropperArenaGroup(groupName);
            }
        }

        arenaHandler.setGroup(specifiedArena.getArenaId(), arenaGroup);

        commandSender.sendMessage("The arena's group has been updated");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return filterMatchingContains(TabCompleteHelper.getDropperArenas(), arguments[0]);
        } else if (arguments.length == 2) {
            List<String> possibleValues = new ArrayList<>();
            possibleValues.add("none");
            possibleValues.add("GroupName");
            for (DropperArenaGroup group : MiniGames.getInstance().getDropperArenaHandler().getAllGroups()) {
                possibleValues.add(group.getGroupName());
            }
            return filterMatchingContains(possibleValues, arguments[1]);
        } else {
            return new ArrayList<>();
        }
    }

}

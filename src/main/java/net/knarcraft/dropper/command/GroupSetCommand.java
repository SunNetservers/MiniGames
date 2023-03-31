package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.arena.DropperArenaGroup;
import net.knarcraft.dropper.arena.DropperArenaHandler;
import net.knarcraft.dropper.util.StringSanitizer;
import net.knarcraft.dropper.util.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The command for setting the group of an arena
 */
public class GroupSetCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (arguments.length < 2) {
            return false;
        }

        DropperArenaHandler arenaHandler = Dropper.getInstance().getArenaHandler();

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
            return TabCompleteHelper.getArenas();
        } else if (arguments.length == 2) {
            List<String> possibleValues = new ArrayList<>();
            possibleValues.add("none");
            possibleValues.add("GroupName");
            for (DropperArenaGroup group : Dropper.getInstance().getArenaHandler().getAllGroups()) {
                possibleValues.add(group.getGroupName());
            }
            return possibleValues;
        } else {
            return new ArrayList<>();
        }
    }

}

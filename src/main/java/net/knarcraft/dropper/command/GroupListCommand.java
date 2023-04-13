package net.knarcraft.dropper.command;

import net.knarcraft.dropper.MiniGames;
import net.knarcraft.dropper.arena.dropper.DropperArena;
import net.knarcraft.dropper.arena.dropper.DropperArenaGroup;
import net.knarcraft.dropper.arena.dropper.DropperArenaHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The command for listing groups and the stages within
 */
public class GroupListCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        DropperArenaHandler arenaHandler = MiniGames.getInstance().getDropperArenaHandler();
        if (arguments.length == 0) {
            displayExistingGroups(arenaHandler, commandSender);
            return true;
        } else if (arguments.length == 1) {
            return displayOrderedArenaNames(arenaHandler, commandSender, arguments[0]);
        } else {
            return false;
        }
    }

    /**
     * Displays all currently existing dropper arena groups
     *
     * @param arenaHandler <p>The arena handler to get groups from</p>
     * @param sender       <p>The command sender to display the groups to</p>
     */
    private void displayExistingGroups(@NotNull DropperArenaHandler arenaHandler, @NotNull CommandSender sender) {
        StringBuilder builder = new StringBuilder("Dropper arena groups:").append("\n");
        arenaHandler.getAllGroups().stream().sorted().forEachOrdered((group) ->
                builder.append(group.getGroupName()).append("\n"));
        sender.sendMessage(builder.toString());
    }

    /**
     * Displays the ordered stages in a specified group to the specified command sender
     *
     * @param arenaHandler <p>The arena handler to get groups from</p>
     * @param sender       <p>The command sender to display the stages to</p>
     * @param groupName    <p>The name of the group to display stages for</p>
     * @return <p>True if the stages were successfully displayed</p>
     */
    private boolean displayOrderedArenaNames(@NotNull DropperArenaHandler arenaHandler, @NotNull CommandSender sender,
                                             @NotNull String groupName) {
        DropperArenaGroup arenaGroup = arenaHandler.getGroup(groupName);
        if (arenaGroup == null) {
            sender.sendMessage("Unable to find the specified group!");
            return false;
        }

        // Send a list of all stages (arenas in the group)
        StringBuilder builder = new StringBuilder(groupName).append("'s stages:").append("\n");
        int counter = 1;
        for (UUID arenaId : arenaGroup.getArenas()) {
            DropperArena arena = arenaHandler.getArena(arenaId);
            if (arena != null) {
                builder.append(counter++).append(". ").append(arena.getArenaName()).append("\n");
            }
        }
        sender.sendMessage(builder.toString());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        if (arguments.length == 1) {
            List<String> groupNames = new ArrayList<>();
            for (DropperArenaGroup group : MiniGames.getInstance().getDropperArenaHandler().getAllGroups()) {
                groupNames.add(group.getGroupName());
            }
            return groupNames;
        } else {
            return new ArrayList<>();
        }
    }

}

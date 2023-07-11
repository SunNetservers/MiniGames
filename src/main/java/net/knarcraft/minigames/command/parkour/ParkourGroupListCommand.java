package net.knarcraft.minigames.command.parkour;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.arena.parkour.ParkourArenaHandler;
import net.knarcraft.minigames.config.MiniGameMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static net.knarcraft.knarlib.util.TabCompletionHelper.filterMatchingContains;

/**
 * The command for listing groups and the stages within
 */
public class ParkourGroupListCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        ParkourArenaHandler arenaHandler = MiniGames.getInstance().getParkourArenaHandler();
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
     * Displays all currently existing parkour arena groups
     *
     * @param arenaHandler <p>The arena handler to get groups from</p>
     * @param sender       <p>The command sender to display the groups to</p>
     */
    private void displayExistingGroups(@NotNull ParkourArenaHandler arenaHandler, @NotNull CommandSender sender) {
        StringBuilder builder = new StringBuilder("Parkour arena groups:").append("\n");
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
    private boolean displayOrderedArenaNames(@NotNull ParkourArenaHandler arenaHandler, @NotNull CommandSender sender,
                                             @NotNull String groupName) {
        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        ParkourArenaGroup arenaGroup = arenaHandler.getGroup(groupName);
        if (arenaGroup == null) {
            stringFormatter.displayErrorMessage(sender, MiniGameMessage.ERROR_GROUP_NOT_FOUND);
            return false;
        }

        // Send a list of all stages (arenas in the group)
        StringBuilder builder = new StringBuilder(stringFormatter.replacePlaceholder(
                MiniGameMessage.SUCCESS_GROUP_STAGES, "{group}", groupName));
        int counter = 1;
        for (UUID arenaId : arenaGroup.getArenas()) {
            ParkourArena arena = arenaHandler.getArena(arenaId);
            if (arena != null) {
                builder.append("\n").append(counter++).append(". ").append(arena.getArenaName());
            }
        }
        stringFormatter.displaySuccessMessage(sender, builder.toString());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        if (arguments.length == 1) {
            List<String> groupNames = new ArrayList<>();
            Set<ParkourArenaGroup> arenaGroups = MiniGames.getInstance().getParkourArenaHandler().getAllGroups();
            for (ParkourArenaGroup group : arenaGroups) {
                groupNames.add(group.getGroupName());
            }
            return filterMatchingContains(groupNames, arguments[0]);
        } else {
            return new ArrayList<>();
        }
    }

}

package net.knarcraft.minigames.command;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaGroup;
import net.knarcraft.minigames.arena.ArenaHandler;
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
 * A command for listing the arenas in a group
 *
 * @param <K> <p>The type of arena handler to get arenas from</p>
 * @param <L> <p>The type of arena to list</p>
 * @param <M> <p>The type of arena group to list</p>
 */
public abstract class GroupListCommand<
        K extends ArenaHandler<L, M>,
        L extends Arena,
        M extends ArenaGroup<L, M>
        > implements TabExecutor {

    /**
     * Displays all currently existing dropper arena groups
     *
     * @param arenaHandler <p>The arena handler to get groups from</p>
     * @param sender       <p>The command sender to display the groups to</p>
     */
    protected void displayExistingGroups(@NotNull K arenaHandler, @NotNull CommandSender sender) {
        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        StringBuilder builder = new StringBuilder(stringFormatter.getUnFormattedMessage(
                MiniGameMessage.SUCCESS_GROUPS)).append("\n");
        arenaHandler.getAllGroups().stream().sorted().forEachOrdered((group) ->
                builder.append("- ").append(group.getGroupName()).append("\n"));
        stringFormatter.displaySuccessMessage(sender, builder.toString());
    }

    /**
     * Displays the ordered stages in a specified group to the specified command sender
     *
     * @param arenaHandler <p>The arena handler to get groups from</p>
     * @param sender       <p>The command sender to display the stages to</p>
     * @param groupName    <p>The name of the group to display stages for</p>
     * @return <p>True if the stages were successfully displayed</p>
     */
    protected boolean displayOrderedArenaNames(@NotNull K arenaHandler, @NotNull CommandSender sender,
                                               @NotNull String groupName) {
        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        M arenaGroup = arenaHandler.getGroup(groupName);
        if (arenaGroup == null) {
            stringFormatter.displayErrorMessage(sender, MiniGameMessage.ERROR_GROUP_NOT_FOUND);
            return false;
        }

        // Send a list of all stages (arenas in the group)
        StringBuilder builder = new StringBuilder(stringFormatter.replacePlaceholder(
                MiniGameMessage.SUCCESS_GROUP_STAGES, "{group}", groupName));
        int counter = 1;
        for (UUID arenaId : arenaGroup.getArenas()) {
            L arena = arenaHandler.getArena(arenaId);
            if (arena != null) {
                builder.append("\n").append(counter++).append(". ").append(arena.getArenaName());
            }
        }
        stringFormatter.displaySuccessMessage(sender, builder.toString());
        return true;
    }

    /**
     * Gets all available groups
     *
     * @return <p>All available groups</p>
     */
    protected abstract Set<M> getGroups();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        if (arguments.length == 1) {
            List<String> groupNames = new ArrayList<>();
            for (M group : getGroups()) {
                groupNames.add(group.getGroupName());
            }
            return filterMatchingContains(groupNames, arguments[0]);
        } else {
            return new ArrayList<>();
        }
    }

}

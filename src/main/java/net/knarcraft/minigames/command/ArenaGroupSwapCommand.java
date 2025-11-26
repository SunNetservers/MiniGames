package net.knarcraft.minigames.command;

import net.knarcraft.knarlib.formatting.FormatBuilder;
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
import java.util.UUID;

import static net.knarcraft.knarlib.util.TabCompletionHelper.filterMatchingContains;

/**
 * The command for swapping the order of two arenas in a group
 */
public abstract class ArenaGroupSwapCommand<ArenaType extends Arena, GroupType extends ArenaGroup<ArenaType, GroupType>> implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (arguments.length < 2) {
            return false;
        }

        ArenaHandler<ArenaType, GroupType> arenaHandler = getArenaHandler();

        ArenaType arena1 = arenaHandler.getArena(arguments[0]);
        if (arena1 == null) {
            new FormatBuilder(MiniGameMessage.ERROR_ARENA_1_NOT_FOUND).error(commandSender);
            return false;
        }

        ArenaType arena2 = arenaHandler.getArena(arguments[1]);
        if (arena2 == null) {
            new FormatBuilder(MiniGameMessage.ERROR_ARENA_2_NOT_FOUND).error(commandSender);
            return false;
        }

        GroupType arena1Group = arenaHandler.getGroup(arena1.getArenaId());
        GroupType arena2Group = arenaHandler.getGroup(arena2.getArenaId());
        if (arena1Group == null || !arena1Group.equals(arena2Group)) {
            new FormatBuilder(MiniGameMessage.ERROR_SWAP_DIFFERENT_GROUPS).error(commandSender);
            return false;
        }

        arena1Group.swapArenas(arena1Group.getArenas().indexOf(arena1.getArenaId()),
                arena1Group.getArenas().indexOf(arena2.getArenaId()));
        new FormatBuilder(MiniGameMessage.SUCCESS_ARENAS_SWAPPED).success(commandSender);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        ArenaHandler<ArenaType, GroupType> arenaHandler = getArenaHandler();
        if (arguments.length == 1) {
            List<String> arenaNames = new ArrayList<>();
            for (ArenaType arena : arenaHandler.getArenasInAGroup()) {
                arenaNames.add(arena.getArenaName());
            }
            return filterMatchingContains(arenaNames, arguments[0]);
        } else if (arguments.length == 2) {
            return filterMatchingContains(getArenaNamesInSameGroup(arguments[0]), arguments[1]);
        } else {
            return new ArrayList<>();
        }
    }

    protected abstract ArenaHandler<ArenaType, GroupType> getArenaHandler();

    /**
     * Gets the names of all arenas in the same group as the specified arena
     *
     * @param arenaName <p>The name of the specified arena</p>
     * @return <p>The names of the arenas in the same group</p>
     */
    private List<String> getArenaNamesInSameGroup(String arenaName) {
        ArenaHandler<ArenaType, GroupType> arenaHandler = getArenaHandler();
        ArenaType arena1 = arenaHandler.getArena(arenaName);
        if (arena1 == null) {
            return new ArrayList<>();
        }

        // Only display other arenas in the selected group
        List<String> arenaNames = new ArrayList<>();
        GroupType group = arenaHandler.getGroup(arena1.getArenaId());
        if (group == null) {
            return new ArrayList<>();
        }
        for (UUID arenaId : group.getArenas()) {
            ArenaType arena = arenaHandler.getArena(arenaId);
            if (arena != null && arena.getArenaId() != arena1.getArenaId()) {
                arenaNames.add(arena.getArenaName());
            }
        }
        return arenaNames;
    }

}

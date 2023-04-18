package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.arena.parkour.ParkourArenaHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.knarcraft.minigames.util.TabCompleteHelper.filterMatchingContains;

/**
 * The command for swapping the order of two arenas in a group
 */
public class ParkourGroupSwapCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (arguments.length < 2) {
            return false;
        }

        ParkourArenaHandler arenaHandler = MiniGames.getInstance().getParkourArenaHandler();

        ParkourArena arena1 = arenaHandler.getArena(arguments[0]);
        if (arena1 == null) {
            commandSender.sendMessage("Unable to find the first specified parkour arena.");
            return false;
        }

        ParkourArena arena2 = arenaHandler.getArena(arguments[1]);
        if (arena2 == null) {
            commandSender.sendMessage("Unable to find the second specified parkour arena.");
            return false;
        }

        ParkourArenaGroup arena1Group = arenaHandler.getGroup(arena1.getArenaId());
        ParkourArenaGroup arena2Group = arenaHandler.getGroup(arena2.getArenaId());
        if (arena1Group == null || !arena1Group.equals(arena2Group)) {
            commandSender.sendMessage("You cannot swap arenas in different groups!");
            return false;
        }

        arena1Group.swapArenas(arena1Group.getArenas().indexOf(arena1.getArenaId()),
                arena1Group.getArenas().indexOf(arena2.getArenaId()));
        commandSender.sendMessage("The arenas have been swapped!");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        ParkourArenaHandler arenaHandler = MiniGames.getInstance().getParkourArenaHandler();
        if (arguments.length == 1) {
            List<String> arenaNames = new ArrayList<>();
            for (ParkourArena parkourArena : arenaHandler.getArenasInAGroup()) {
                arenaNames.add(parkourArena.getArenaName());
            }
            return filterMatchingContains(arenaNames, arguments[0]);
        } else if (arguments.length == 2) {
            return filterMatchingContains(getArenaNamesInSameGroup(arguments[0]), arguments[1]);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Gets the names of all arenas in the same group as the specified arena
     *
     * @param arenaName <p>The name of the specified arena</p>
     * @return <p>The names of the arenas in the same group</p>
     */
    private List<String> getArenaNamesInSameGroup(String arenaName) {
        ParkourArenaHandler arenaHandler = MiniGames.getInstance().getParkourArenaHandler();
        ParkourArena arena1 = arenaHandler.getArena(arenaName);
        if (arena1 == null) {
            return new ArrayList<>();
        }

        // Only display other arenas in the selected group
        List<String> arenaNames = new ArrayList<>();
        ParkourArenaGroup group = arenaHandler.getGroup(arena1.getArenaId());
        if (group == null) {
            return new ArrayList<>();
        }
        for (UUID arenaId : group.getArenas()) {
            ParkourArena arena = arenaHandler.getArena(arenaId);
            if (arena != null && arena.getArenaId() != arena1.getArenaId()) {
                arenaNames.add(arena.getArenaName());
            }
        }
        return arenaNames;
    }

}

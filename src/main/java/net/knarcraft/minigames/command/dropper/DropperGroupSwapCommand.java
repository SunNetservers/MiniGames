package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import net.knarcraft.minigames.arena.dropper.DropperArenaHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The command for swapping the order of two arenas in a group
 */
public class DropperGroupSwapCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (arguments.length < 2) {
            return false;
        }

        DropperArenaHandler arenaHandler = MiniGames.getInstance().getDropperArenaHandler();

        DropperArena arena1 = arenaHandler.getArena(arguments[0]);
        if (arena1 == null) {
            commandSender.sendMessage("Unable to find the first specified dropper arena.");
            return false;
        }

        DropperArena arena2 = arenaHandler.getArena(arguments[1]);
        if (arena2 == null) {
            commandSender.sendMessage("Unable to find the second specified dropper arena.");
            return false;
        }

        DropperArenaGroup arena1Group = arenaHandler.getGroup(arena1.getArenaId());
        DropperArenaGroup arena2Group = arenaHandler.getGroup(arena2.getArenaId());
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
        DropperArenaHandler arenaHandler = MiniGames.getInstance().getDropperArenaHandler();
        if (arguments.length == 1) {
            List<String> arenaNames = new ArrayList<>();
            for (DropperArena dropperArena : arenaHandler.getArenasInAGroup()) {
                arenaNames.add(dropperArena.getArenaName());
            }
            return arenaNames;
        } else if (arguments.length == 2) {
            return getArenaNamesInSameGroup(arguments[0]);
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
        DropperArenaHandler arenaHandler = MiniGames.getInstance().getDropperArenaHandler();
        DropperArena arena1 = arenaHandler.getArena(arenaName);
        if (arena1 == null) {
            return new ArrayList<>();
        }

        // Only display other arenas in the selected group
        List<String> arenaNames = new ArrayList<>();
        DropperArenaGroup group = arenaHandler.getGroup(arena1.getArenaId());
        if (group == null) {
            return new ArrayList<>();
        }
        for (UUID arenaId : group.getArenas()) {
            DropperArena arena = arenaHandler.getArena(arenaId);
            if (arena != null && arena.getArenaId() != arena1.getArenaId()) {
                arenaNames.add(arena.getArenaName());
            }
        }
        return arenaNames;
    }

}

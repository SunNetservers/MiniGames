package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.arena.parkour.ParkourArenaHandler;
import net.knarcraft.minigames.command.GroupListCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * The command for listing groups and the stages within
 */
public class ParkourGroupListCommand extends GroupListCommand<ParkourArenaHandler, ParkourArena, ParkourArenaGroup> {

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

    @Override
    protected Set<ParkourArenaGroup> getGroups() {
        return MiniGames.getInstance().getParkourArenaHandler().getAllGroups();
    }

}

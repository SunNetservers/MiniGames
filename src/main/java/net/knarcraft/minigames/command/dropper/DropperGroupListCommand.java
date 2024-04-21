package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import net.knarcraft.minigames.arena.dropper.DropperArenaHandler;
import net.knarcraft.minigames.command.GroupListCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * The command for listing groups and the stages within
 */
public class DropperGroupListCommand extends GroupListCommand<DropperArenaHandler, DropperArena, DropperArenaGroup> {

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

    @Override
    protected Set<DropperArenaGroup> getGroups() {
        return MiniGames.getInstance().getDropperArenaHandler().getAllGroups();
    }

}

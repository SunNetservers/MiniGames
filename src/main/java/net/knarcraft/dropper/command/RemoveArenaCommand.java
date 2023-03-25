package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.util.ArenaStorageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The method used for removing an existing arena
 */
public class RemoveArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        // Get the specified arena
        String arenaName = arguments[0];
        String sanitized = ArenaStorageHelper.sanitizeArenaName(arenaName);
        DropperArena targetArena = null;
        for (DropperArena arena : Dropper.getInstance().getArenaHandler().getArenas()) {
            if (sanitized.equals(ArenaStorageHelper.sanitizeArenaName(arena.getArenaName()))) {
                targetArena = arena;
            }
        }

        if (targetArena == null) {
            commandSender.sendMessage("Unable to find the specified arena");
            return false;
        }

        // Remove the arena
        Dropper.getInstance().getArenaHandler().removeArena(targetArena);
        commandSender.sendMessage("The specified arena has been successfully removed");
        return true;
    }

}

package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
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
        DropperArena targetArena = Dropper.getInstance().getArenaHandler().getArena(arguments[0]);
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

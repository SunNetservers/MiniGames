package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The method used for removing an existing arena
 */
public class RemoveParkourArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        // Abort if no name was specified
        if (arguments.length < 1) {
            return false;
        }

        // Get the specified arena
        ParkourArena targetArena = MiniGames.getInstance().getParkourArenaHandler().getArena(arguments[0]);
        if (targetArena == null) {
            commandSender.sendMessage("Unable to find the specified arena");
            return false;
        }

        // Remove the arena
        MiniGames.getInstance().getParkourArenaHandler().removeArena(targetArena);
        commandSender.sendMessage("The specified arena has been successfully removed");
        return true;
    }

}
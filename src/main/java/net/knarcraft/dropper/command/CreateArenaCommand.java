package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.util.ArenaStorageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command for creating a new dropper arena
 */
public class CreateArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command must be used by a player");
            return false;
        }

        // Abort if no name was specified
        if (arguments.length < 1) {
            return false;
        }

        String arenaName = arguments[0];
        String sanitized = ArenaStorageHelper.sanitizeArenaName(arenaName);

        for (DropperArena arena : Dropper.getInstance().getArenaHandler().getArenas()) {
            if (sanitized.equals(ArenaStorageHelper.sanitizeArenaName(arena.getArenaName()))) {
                commandSender.sendMessage("There already exists a dropper arena with that name!");
                return false;
            }
        }

        //TODO: Make sure the arena name doesn't contain any unwanted characters

        DropperArena arena = new DropperArena(arenaName, player.getLocation());
        Dropper.getInstance().getArenaHandler().addArena(arena);
        commandSender.sendMessage("The arena was successfully created!");
        return true;
    }

}

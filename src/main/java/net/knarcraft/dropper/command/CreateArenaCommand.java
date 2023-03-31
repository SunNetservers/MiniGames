package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.arena.DropperArenaHandler;
import net.knarcraft.dropper.util.StringSanitizer;
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

        // Remove known characters that are likely to cause trouble if used in an arena name
        String arenaName = StringSanitizer.removeUnwantedCharacters(arguments[0]);

        // An arena name is required
        if (arenaName.isBlank()) {
            return false;
        }

        DropperArenaHandler arenaHandler = Dropper.getInstance().getArenaHandler();

        DropperArena existingArena = arenaHandler.getArena(arenaName);
        if (existingArena != null) {
            commandSender.sendMessage("There already exists a dropper arena with that name!");
            return false;
        }

        DropperArena arena = new DropperArena(arenaName, player.getLocation(), arenaHandler);
        arenaHandler.addArena(arena);
        commandSender.sendMessage("The arena was successfully created!");
        return true;
    }

}

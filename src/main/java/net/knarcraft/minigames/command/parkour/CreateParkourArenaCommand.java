package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaHandler;
import net.knarcraft.minigames.util.StringSanitizer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command for creating a new parkour arena
 */
public class CreateParkourArenaCommand implements CommandExecutor {

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

        ParkourArenaHandler arenaHandler = MiniGames.getInstance().getParkourArenaHandler();

        ParkourArena existingArena = arenaHandler.getArena(arenaName);
        if (existingArena != null) {
            commandSender.sendMessage("There already exists a parkour arena with that name!");
            return false;
        }

        ParkourArena arena = new ParkourArena(arenaName, player.getLocation(), arenaHandler);
        arenaHandler.addArena(arena);
        commandSender.sendMessage("The arena was successfully created!");
        return true;
    }

}

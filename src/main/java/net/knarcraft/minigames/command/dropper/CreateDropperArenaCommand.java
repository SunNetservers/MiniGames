package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaHandler;
import net.knarcraft.minigames.config.Message;
import net.knarcraft.minigames.util.StringSanitizer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command for creating a new dropper arena
 */
public class CreateDropperArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Message.ERROR_PLAYER_ONLY.getMessage());
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

        DropperArenaHandler arenaHandler = MiniGames.getInstance().getDropperArenaHandler();

        DropperArena existingArena = arenaHandler.getArena(arenaName);
        if (existingArena != null) {
            commandSender.sendMessage(Message.ERROR_ARENA_NAME_COLLISION.getMessage());
            return false;
        }

        DropperArena arena = new DropperArena(arenaName, player.getLocation(), arenaHandler);
        arenaHandler.addArena(arena);
        commandSender.sendMessage(Message.SUCCESS_ARENA_CREATED.getMessage());
        return true;
    }

}

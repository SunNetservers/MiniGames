package net.knarcraft.dropper.command;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.arena.DropperArenaPlayerRegistry;
import net.knarcraft.dropper.arena.DropperArenaSession;
import net.knarcraft.dropper.property.ArenaGameMode;
import net.knarcraft.dropper.util.PlayerTeleporter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command used to join a dropper arena
 */
public class JoinArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command must be used by a player");
            return false;
        }

        if (arguments.length < 1) {
            return false;
        }

        // Disallow joining if the player is already in a dropper arena
        DropperArenaSession existingSession = Dropper.getInstance().getPlayerRegistry().getArenaSession(player);
        if (existingSession != null) {
            commandSender.sendMessage("You are already in a dropper arena!");
            return false;
        }

        // Make sure the arena exists
        String arenaName = arguments[0].trim();
        DropperArena specifiedArena = null;
        for (DropperArena arena : Dropper.getInstance().getArenaHandler().getArenas()) {
            if (arena.getArenaName().equalsIgnoreCase(arenaName)) {
                specifiedArena = arena;
                break;
            }
        }
        if (specifiedArena == null) {
            commandSender.sendMessage("Unable to find the specified dropper arena.");
            return false;
        }

        // Find the specified game-mode
        ArenaGameMode gameMode;
        if (arguments.length > 1) {
            gameMode = ArenaGameMode.matchGamemode(arguments[1]);
        } else {
            gameMode = ArenaGameMode.DEFAULT;
        }

        //TODO: Check if the arena has been beaten if the non-default game-mode has been chosen

        // Register the player's session
        DropperArenaSession newSession = new DropperArenaSession(specifiedArena, player, gameMode);
        DropperArenaPlayerRegistry playerRegistry = Dropper.getInstance().getPlayerRegistry();
        playerRegistry.registerPlayer(player, newSession);

        // Try to teleport the player to the arena
        boolean teleported = PlayerTeleporter.teleportPlayer(player, specifiedArena.getSpawnLocation(), false);
        if (!teleported) {
            commandSender.sendMessage("Unable to teleport you to the dropper arena. Make sure you're not in a vehicle," +
                    "and is not carrying a passenger!");
            newSession.triggerQuit();
            return false;
        } else {
            return true;
        }
    }

}

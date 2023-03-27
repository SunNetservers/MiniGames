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
        DropperArenaSession existingSession = Dropper.getInstance().getPlayerRegistry().getArenaSession(player.getUniqueId());
        if (existingSession != null) {
            commandSender.sendMessage("You are already in a dropper arena!");
            return false;
        }

        // Make sure the arena exists
        DropperArena specifiedArena = Dropper.getInstance().getArenaHandler().getArena(arguments[0]);
        if (specifiedArena == null) {
            commandSender.sendMessage("Unable to find the specified dropper arena.");
            return false;
        }

        // Deny vehicles as allowing this is tricky, and will cause problems in some cases
        if (player.isInsideVehicle() || !player.getPassengers().isEmpty()) {
            commandSender.sendMessage("You cannot join an arena while inside a vehicle or carrying a passenger.");
            return false;
        }

        return joinArena(specifiedArena, player, arguments);
    }

    /**
     * Performs the actual arena joining
     *
     * @param specifiedArena <p>The arena the player wants to join</p>
     * @param player         <p>The player joining the arena</p>
     * @param arguments      <p>The arguments given</p>
     * @return <p>Whether the arena was joined successfully</p>
     */
    private boolean joinArena(DropperArena specifiedArena, Player player, String[] arguments) {
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
        playerRegistry.registerPlayer(player.getUniqueId(), newSession);

        // Try to teleport the player to the arena
        boolean teleported = PlayerTeleporter.teleportPlayer(player, specifiedArena.getSpawnLocation(), false, false);
        if (!teleported) {
            player.sendMessage("Unable to teleport you to the dropper arena. Make sure you're not in a vehicle," +
                    "and not carrying a passenger!");
            newSession.triggerQuit(false);
            return false;
        } else {
            // Make sure to update the state again in the air to remove a potential swimming state
            newSession.getEntryState().setArenaState(specifiedArena.getPlayerHorizontalVelocity());
            return true;
        }
    }

}

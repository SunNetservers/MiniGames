package net.knarcraft.dropper.command;

import net.knarcraft.dropper.MiniGames;
import net.knarcraft.dropper.arena.dropper.DropperArena;
import net.knarcraft.dropper.arena.dropper.DropperArenaGameMode;
import net.knarcraft.dropper.arena.dropper.DropperArenaGroup;
import net.knarcraft.dropper.arena.dropper.DropperArenaPlayerRegistry;
import net.knarcraft.dropper.arena.dropper.DropperArenaSession;
import net.knarcraft.dropper.config.DropperConfiguration;
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
        DropperArenaSession existingSession = MiniGames.getInstance().getDropperArenaPlayerRegistry().getArenaSession(player.getUniqueId());
        if (existingSession != null) {
            commandSender.sendMessage("You are already in a dropper arena!");
            return false;
        }

        // Make sure the arena exists
        DropperArena specifiedArena = MiniGames.getInstance().getDropperArenaHandler().getArena(arguments[0]);
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
        DropperArenaGameMode gameMode;
        if (arguments.length > 1) {
            gameMode = DropperArenaGameMode.matchGamemode(arguments[1]);
        } else {
            gameMode = DropperArenaGameMode.DEFAULT;
        }

        // Make sure the player has beaten the necessary levels
        DropperArenaGroup arenaGroup = MiniGames.getInstance().getDropperArenaHandler().getGroup(specifiedArena.getArenaId());
        if (arenaGroup != null && !doGroupChecks(specifiedArena, arenaGroup, gameMode, player)) {
            return false;
        }

        // Make sure the player has beaten the arena once in normal mode before playing another mode
        if (MiniGames.getInstance().getDropperConfiguration().mustDoNormalModeFirst() &&
                gameMode != DropperArenaGameMode.DEFAULT &&
                specifiedArena.getData().hasNotCompleted(DropperArenaGameMode.DEFAULT, player)) {
            player.sendMessage("You must complete this arena in normal mode first!");
            return false;
        }

        // Register the player's session
        DropperArenaSession newSession = new DropperArenaSession(specifiedArena, player, gameMode);
        DropperArenaPlayerRegistry playerRegistry = MiniGames.getInstance().getDropperArenaPlayerRegistry();
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

    /**
     * Performs necessary check for the given arena's group
     *
     * @param dropperArena  <p>The arena the player is trying to join</p>
     * @param arenaGroup    <p>The arena group the arena belongs to</p>
     * @param arenaGameMode <p>The game-mode the player selected</p>
     * @param player        <p>The the player trying to join the arena</p>
     * @return <p>False if any checks failed</p>
     */
    private boolean doGroupChecks(@NotNull DropperArena dropperArena, @NotNull DropperArenaGroup arenaGroup,
                                  @NotNull DropperArenaGameMode arenaGameMode, @NotNull Player player) {
        DropperConfiguration configuration = MiniGames.getInstance().getDropperConfiguration();
        // Require that players beat all arenas in the group in the normal game-mode before trying challenge modes
        if (configuration.mustDoNormalModeFirst() && arenaGameMode != DropperArenaGameMode.DEFAULT &&
                !arenaGroup.hasBeatenAll(DropperArenaGameMode.DEFAULT, player)) {
            player.sendMessage("You have not yet beaten all arenas in this group!");
            return false;
        }

        // Require that the player has beaten the previous arena on the same game-mode before trying this one
        if (configuration.mustDoGroupedInSequence() &&
                !arenaGroup.canPlay(arenaGameMode, player, dropperArena.getArenaId())) {
            player.sendMessage("You have not yet beaten the previous arena!");
            return false;
        }

        return true;
    }

}

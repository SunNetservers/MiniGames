package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGameMode;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.arena.parkour.ParkourArenaPlayerRegistry;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import net.knarcraft.minigames.config.ParkourConfiguration;
import net.knarcraft.minigames.util.PlayerTeleporter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command used to join a parkour arena
 */
public class JoinParkourArenaCommand implements CommandExecutor {

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

        // Disallow joining if the player is already in a mini-game arena
        if (MiniGames.getInstance().getSession(player.getUniqueId()) != null) {
            commandSender.sendMessage("You are already playing a mini-game!");
            return false;
        }

        // Make sure the arena exists
        ParkourArena specifiedArena = MiniGames.getInstance().getParkourArenaHandler().getArena(arguments[0]);
        if (specifiedArena == null) {
            commandSender.sendMessage("Unable to find the specified parkour arena.");
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
    private boolean joinArena(ParkourArena specifiedArena, Player player, String[] arguments) {
        // Find the specified game-mode
        ParkourArenaGameMode gameMode;
        if (arguments.length > 1) {
            gameMode = ParkourArenaGameMode.matchGamemode(arguments[1]);
        } else {
            gameMode = ParkourArenaGameMode.DEFAULT;
        }

        // Make sure the player has beaten the necessary levels
        ParkourArenaGroup arenaGroup = MiniGames.getInstance().getParkourArenaHandler().getGroup(specifiedArena.getArenaId());
        if (arenaGroup != null && !doGroupChecks(specifiedArena, arenaGroup, gameMode, player)) {
            return false;
        }

        // Register the player's session
        ParkourArenaSession newSession = new ParkourArenaSession(specifiedArena, player, gameMode);
        ParkourArenaPlayerRegistry playerRegistry = MiniGames.getInstance().getParkourArenaPlayerRegistry();
        playerRegistry.registerPlayer(player.getUniqueId(), newSession);

        // Try to teleport the player to the arena
        boolean teleported = PlayerTeleporter.teleportPlayer(player, specifiedArena.getSpawnLocation(), false, false);
        if (!teleported) {
            player.sendMessage("Unable to teleport you to the parkour arena. Make sure you're not in a vehicle," +
                    "and not carrying a passenger!");
            newSession.triggerQuit(false);
            return false;
        } else {
            // Make sure to update the state again in the air to remove a potential swimming state
            newSession.getEntryState().setArenaState();
            return true;
        }
    }

    /**
     * Performs necessary check for the given arena's group
     *
     * @param parkourArena  <p>The arena the player is trying to join</p>
     * @param arenaGroup    <p>The arena group the arena belongs to</p>
     * @param arenaGameMode <p>The game-mode the player selected</p>
     * @param player        <p>The the player trying to join the arena</p>
     * @return <p>False if any checks failed</p>
     */
    private boolean doGroupChecks(@NotNull ParkourArena parkourArena, @NotNull ParkourArenaGroup arenaGroup,
                                  @NotNull ParkourArenaGameMode arenaGameMode, @NotNull Player player) {
        ParkourConfiguration configuration = MiniGames.getInstance().getParkourConfiguration();

        // Require that the player has beaten the previous arena on the same game-mode before trying this one
        if (configuration.mustDoGroupedInSequence() &&
                arenaGroup.cannotPlay(arenaGameMode, player, parkourArena.getArenaId())) {
            player.sendMessage("You have not yet beaten the previous arena!");
            return false;
        }

        return true;
    }

}
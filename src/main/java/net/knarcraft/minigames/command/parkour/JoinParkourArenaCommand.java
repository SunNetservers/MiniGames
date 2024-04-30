package net.knarcraft.minigames.command.parkour;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaPlayerRegistry;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGameMode;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.config.ParkourConfiguration;
import net.knarcraft.minigames.util.GUIHelper;
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
        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        if (!(commandSender instanceof Player player)) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_PLAYER_ONLY);
            return false;
        }

        if (arguments.length < 1) {
            return false;
        }

        // Disallow joining if the player is already in a mini-game arena
        if (MiniGames.getInstance().getSession(player.getUniqueId()) != null) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_ALREADY_PLAYING);
            return true;
        }

        // Make sure the arena exists
        ParkourArena specifiedArena = MiniGames.getInstance().getParkourArenaHandler().getArena(arguments[0]);
        if (specifiedArena == null) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_ARENA_NOT_FOUND);
            return false;
        }

        // Deny vehicles as allowing this is tricky, and will cause problems in some cases
        if (player.isInsideVehicle() || !player.getPassengers().isEmpty()) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_JOIN_IN_VEHICLE_OR_PASSENGER);
            return true;
        }

        // Deny joining full arenas
        int playingNow = MiniGames.getInstance().getParkourArenaPlayerRegistry().getPlayingPlayers(specifiedArena).size();
        if (specifiedArena.getMaxPlayers() > 0 && playingNow >= specifiedArena.getMaxPlayers()) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_JOIN_ARENA_FULL);
            return true;
        }

        joinArena(specifiedArena, player, arguments);
        return true;
    }

    /**
     * Performs the actual arena joining
     *
     * @param specifiedArena <p>The arena the player wants to join</p>
     * @param player         <p>The player joining the arena</p>
     * @param arguments      <p>The arguments given</p>
     */
    private void joinArena(ParkourArena specifiedArena, Player player, String[] arguments) {
        // Find the specified game-mode
        ParkourArenaGameMode gameMode;
        if (arguments.length > 1) {
            gameMode = ParkourArenaGameMode.matchGameMode(arguments[1]);
        } else {
            gameMode = ParkourArenaGameMode.DEFAULT;
        }

        // Don't allow joining the hardcore game-mode if there are no checkpoints to skip
        if (specifiedArena.hasNoCheckpoints() && gameMode == ParkourArenaGameMode.HARDCORE) {
            MiniGames.getInstance().getStringFormatter().displayErrorMessage(player,
                    MiniGameMessage.ERROR_HARDCORE_NO_CHECKPOINTS);
            return;
        }

        // Make sure the player has beaten the necessary levels
        ParkourArenaGroup arenaGroup = MiniGames.getInstance().getParkourArenaHandler().getGroup(specifiedArena.getArenaId());
        if (arenaGroup != null && !doGroupChecks(specifiedArena, arenaGroup, gameMode, player)) {
            return;
        }

        // Register the player's session
        ParkourArenaSession newSession = new ParkourArenaSession(specifiedArena, player, gameMode);
        ArenaPlayerRegistry<ParkourArena> playerRegistry = MiniGames.getInstance().getParkourArenaPlayerRegistry();
        playerRegistry.registerPlayer(player.getUniqueId(), newSession);

        // Update visibility and hit-box for the player
        MiniGames.getInstance().getPlayerVisibilityManager().updateHiddenPlayers(playerRegistry, player);

        // Try to teleport the player to the arena
        boolean teleported = PlayerTeleporter.teleportPlayer(player, specifiedArena.getSpawnLocation(), false, false);
        if (!teleported) {
            MiniGames.getInstance().getStringFormatter().displayErrorMessage(player,
                    MiniGameMessage.ERROR_ARENA_TELEPORT_FAILED);
            newSession.triggerQuit(false, true);
        } else {
            // Update the player's state to follow the arena's rules
            newSession.getEntryState().setArenaState();

            player.getInventory().addItem(GUIHelper.getGUIOpenItem(player));
            MiniGames.getInstance().getStringFormatter().displaySuccessMessage(player,
                    MiniGameMessage.SUCCESS_ARENA_JOINED);
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
            MiniGames.getInstance().getStringFormatter().displayErrorMessage(player,
                    MiniGameMessage.ERROR_PREVIOUS_ARENA_REQUIRED);
            return false;
        }

        return true;
    }

}

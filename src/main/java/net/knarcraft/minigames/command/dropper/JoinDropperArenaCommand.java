package net.knarcraft.minigames.command.dropper;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaPlayerRegistry;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import net.knarcraft.minigames.arena.dropper.DropperArenaSession;
import net.knarcraft.minigames.config.DropperConfiguration;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.util.GUIHelper;
import net.knarcraft.minigames.util.GeyserHelper;
import net.knarcraft.minigames.util.PlayerTeleporter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The command used to join a dropper arena
 */
public class JoinDropperArenaCommand implements CommandExecutor {

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

        if (GeyserHelper.isGeyserPlayer(player)) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_GEYSER_DROPPER);
            return true;
        }

        // Disallow joining if the player is already in a mini-game arena
        if (MiniGames.getInstance().getSession(player.getUniqueId()) != null) {
            stringFormatter.displayErrorMessage(commandSender, MiniGameMessage.ERROR_ALREADY_PLAYING);
            return true;
        }

        // Make sure the arena exists
        DropperArena specifiedArena = MiniGames.getInstance().getDropperArenaHandler().getArena(arguments[0]);
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
        int playingNow = MiniGames.getInstance().getDropperArenaPlayerRegistry().getPlayingPlayers(specifiedArena).size();
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
    private void joinArena(DropperArena specifiedArena, Player player, String[] arguments) {
        // Find the specified game-mode
        DropperArenaGameMode gameMode;
        if (arguments.length > 1) {
            gameMode = DropperArenaGameMode.matchGameMode(arguments[1]);
        } else {
            gameMode = DropperArenaGameMode.DEFAULT;
        }

        // Make sure the player has beaten the necessary levels
        DropperArenaGroup arenaGroup = MiniGames.getInstance().getDropperArenaHandler().getGroup(specifiedArena.getArenaId());
        if (arenaGroup != null && !doGroupChecks(specifiedArena, arenaGroup, gameMode, player)) {
            return;
        }

        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();

        // Make sure the player has beaten the arena once in normal mode before playing another mode
        if (MiniGames.getInstance().getDropperConfiguration().mustDoNormalModeFirst() &&
                gameMode != DropperArenaGameMode.DEFAULT &&
                specifiedArena.getData().hasNotCompleted(DropperArenaGameMode.DEFAULT, player)) {
            stringFormatter.displayErrorMessage(player, MiniGameMessage.ERROR_NORMAL_MODE_REQUIRED);
            return;
        }

        // Register the player's session
        DropperArenaSession newSession = new DropperArenaSession(specifiedArena, player, gameMode);
        ArenaPlayerRegistry<DropperArena> playerRegistry = MiniGames.getInstance().getDropperArenaPlayerRegistry();
        playerRegistry.registerPlayer(player.getUniqueId(), newSession);

        // Update visibility and hit-box for the player
        MiniGames.getInstance().getPlayerVisibilityManager().updateHiddenPlayers(playerRegistry, player);

        // Try to teleport the player to the arena
        boolean teleported = PlayerTeleporter.teleportPlayer(player, specifiedArena.getSpawnLocation(), false, false);
        if (!teleported) {
            stringFormatter.displayErrorMessage(player, MiniGameMessage.ERROR_ARENA_TELEPORT_FAILED);
            newSession.triggerQuit(false, true);
        } else {
            // Update the player's state to follow the arena's rules
            newSession.getEntryState().setArenaState();

            player.getInventory().addItem(GUIHelper.getGUIOpenItem(player));
            stringFormatter.displaySuccessMessage(player, MiniGameMessage.SUCCESS_ARENA_JOINED);
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
        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        DropperConfiguration configuration = MiniGames.getInstance().getDropperConfiguration();
        // Require that players beat all arenas in the group in the normal game-mode before trying challenge modes
        if (configuration.mustDoNormalModeFirst() && arenaGameMode != DropperArenaGameMode.DEFAULT &&
                !arenaGroup.hasBeatenAll(DropperArenaGameMode.DEFAULT, player)) {
            stringFormatter.displayErrorMessage(player, MiniGameMessage.ERROR_GROUP_NORMAL_MODE_REQUIRED);
            return false;
        }

        // Require that the player has beaten the previous arena on the same game-mode before trying this one
        if (configuration.mustDoGroupedInSequence() &&
                arenaGroup.cannotPlay(arenaGameMode, player, dropperArena.getArenaId())) {
            stringFormatter.displayErrorMessage(player, MiniGameMessage.ERROR_PREVIOUS_ARENA_REQUIRED);
            return false;
        }

        return true;
    }

}

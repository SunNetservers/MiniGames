package net.knarcraft.minigames.listener;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaPlayerRegistry;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.PlayerEntryState;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.logging.Level;

/**
 * A listener for players leaving/joining the server, or leaving the server unexpectedly
 */
public class PlayerStateChangeListener implements Listener {

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();

        // Restore any lingering arena states
        Location restoreLocation;
        restoreLocation = restoreStateIfNecessary(player, MiniGames.getInstance().getDropperArenaPlayerRegistry());
        if (restoreLocation != null) {
            event.setSpawnLocation(restoreLocation);
        }
        restoreLocation = restoreStateIfNecessary(player, MiniGames.getInstance().getParkourArenaPlayerRegistry());
        if (restoreLocation != null) {
            event.setSpawnLocation(restoreLocation);
        }
    }

    /**
     * Prevent the player from teleporting away from an arena for any reason
     *
     * @param event <p>The triggered teleport event</p>
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location targetLocation = event.getTo();
        if (targetLocation == null) {
            return;
        }

        // Ignore if not in an arena session
        ArenaSession arenaSession = MiniGames.getInstance().getSession(event.getPlayer().getUniqueId());
        if (arenaSession == null) {
            return;
        }

        // If teleported to the arena's spawn, it's fine
        if (targetLocation.equals(arenaSession.getArena().getSpawnLocation())) {
            return;
        }

        // If teleported to the arena's checkpoint, it's fine
        if (arenaSession instanceof ParkourArenaSession parkourArenaSession &&
                targetLocation.equals(parkourArenaSession.getRegisteredCheckpoint())) {
            return;
        }

        event.setCancelled(true);
    }

    /**
     * Restores the state of the given player if a lingering session is found in the given player registry
     *
     * @param player         <p>The player whose state should be checked</p>
     * @param playerRegistry <p>The registry to check for a lingering state</p>
     * @return <p>The location the player should spawn in, or null if not restored</p>
     */
    private Location restoreStateIfNecessary(Player player, ArenaPlayerRegistry<?> playerRegistry) {
        PlayerEntryState entryState = playerRegistry.getEntryState(player.getUniqueId());
        if (entryState != null) {
            MiniGames.log(Level.INFO, "Found existing state for joining player " + player +
                    ". Attempting to restore the player's state.");
            playerRegistry.removePlayer(player.getUniqueId(), false);

            entryState.restore(player);
            return entryState.getEntryLocation();
        } else {
            return null;
        }
    }

}

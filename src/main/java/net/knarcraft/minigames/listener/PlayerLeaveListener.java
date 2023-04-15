package net.knarcraft.minigames.listener;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A listener for players leaving the server or the arena
 */
public class PlayerLeaveListener implements Listener {

    private final Map<UUID, ArenaSession> leftSessions = new HashMap<>();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ArenaSession arenaSession = MiniGames.getInstance().getSession(event.getPlayer().getUniqueId());
        if (arenaSession == null) {
            return;
        }

        MiniGames.log(Level.WARNING, "Found player " + player.getUniqueId() +
                " leaving in the middle of a session!");
        leftSessions.put(player.getUniqueId(), arenaSession);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        // Force the player to quit from the session once they re-join
        if (leftSessions.containsKey(playerId)) {
            MiniGames.log(Level.WARNING, "Found un-exited dropper session!");
            Bukkit.getScheduler().runTaskLater(MiniGames.getInstance(), () -> {
                leftSessions.get(playerId).triggerQuit(false);
                MiniGames.log(Level.WARNING, "Triggered a quit!");
                leftSessions.remove(playerId);
            }, 80);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location targetLocation = event.getTo();
        if (targetLocation == null || event.isCancelled()) {
            return;
        }

        ArenaSession arenaSession = MiniGames.getInstance().getSession(event.getPlayer().getUniqueId());
        if (arenaSession == null) {
            return;
        }

        if (targetLocation.equals(arenaSession.getArena().getSpawnLocation())) {
            return;
        }

        if (arenaSession instanceof ParkourArenaSession parkourArenaSession &&
                targetLocation.equals(parkourArenaSession.getRegisteredCheckpoint())) {
            return;
        }

        arenaSession.triggerQuit(false);
    }

}

package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A listener for players leaving the server or the arena
 */
public class PlayerLeaveListener implements Listener {

    private final Map<UUID, DropperArenaSession> leftSessions = new HashMap<>();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        DropperArenaSession arenaSession = getSession(player);
        if (arenaSession == null) {
            return;
        }

        Dropper.getInstance().getLogger().log(Level.WARNING, "Found player " + player.getUniqueId() +
                " leaving in the middle of a session!");
        leftSessions.put(player.getUniqueId(), arenaSession);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        // Force the player to quit from the session once they re-join
        if (leftSessions.containsKey(playerId)) {
            Dropper.getInstance().getLogger().log(Level.WARNING, "Found un-exited dropper session!");
            Bukkit.getScheduler().runTaskLater(Dropper.getInstance(), () -> {
                leftSessions.get(playerId).triggerQuit();
                Dropper.getInstance().getLogger().log(Level.WARNING, "Triggered a quit!");
                leftSessions.remove(playerId);
            }, 80);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null || event.isCancelled()) {
            return;
        }

        DropperArenaSession arenaSession = getSession(event.getPlayer());
        if (arenaSession == null) {
            return;
        }

        if (event.getTo().equals(arenaSession.getArena().getSpawnLocation())) {
            return;
        }

        arenaSession.triggerQuit();
    }

    /**
     * Gets the arena session for the given player
     *
     * @param player <p>The player to get the arena session for</p>
     * @return <p>The player's session, or null if not in a session</p>
     */
    private @Nullable DropperArenaSession getSession(@NotNull Player player) {
        return Dropper.getInstance().getPlayerRegistry().getArenaSession(player.getUniqueId());
    }

}

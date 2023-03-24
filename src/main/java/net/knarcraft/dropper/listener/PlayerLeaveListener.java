package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A listener for players leaving the server or the arena
 */
public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        triggerQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        DropperArenaSession arenaSession = getSession(event.getPlayer());
        if (arenaSession == null) {
            return;
        }

        if (event.getTo().equals(arenaSession.getArena().getSpawnLocation())) {
            return;
        }

        triggerQuit(event.getPlayer());
    }

    /**
     * Forces the given player to quit their current arena
     *
     * @param player <p>The player to trigger a quit for</p>
     */
    private void triggerQuit(Player player) {
        DropperArenaSession arenaSession = getSession(player);
        if (arenaSession == null) {
            return;
        }

        arenaSession.triggerQuit();

        //TODO: It might not be possible to alter a leaving player's location here. It might be necessary to move them once 
        // they join again
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

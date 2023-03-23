package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
        triggerQuit(event.getPlayer());
    }

    /**
     * Forces the given player to quit their current arena
     *
     * @param player <p>The player to trigger a quit for</p>
     */
    private void triggerQuit(Player player) {
        DropperArenaSession arenaSession = Dropper.getInstance().getPlayerRegistry().getArenaSession(player);
        if (arenaSession == null) {
            return;
        }

        arenaSession.triggerQuit();

        //TODO: It might not be possible to alter the player's location here. It might be necessary to move them once 
        // they join again
    }

}

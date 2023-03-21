package net.knarcraft.dropper.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        //TODO: If in an arena, kick the player.
        //TODO: Teleport the player away from the arena. It might only be possible to teleport the player when they join 
        // again.
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        //TODO: Treat this the same as onPlayerLeave if the player is in an arena. If the player doesn't change worlds,
        // it should be safe to immediately teleport the player to the arena's exit.
    }

}

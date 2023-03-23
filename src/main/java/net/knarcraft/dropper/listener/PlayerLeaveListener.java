package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (!Dropper.getInstance().getPlayerRegistry().isInArena(event.getPlayer())) {
            return;
        }
        //TODO: If in an arena, kick the player.
        //TODO: Teleport the player away from the arena. It might only be possible to teleport the player when they join 
        // again.
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!Dropper.getInstance().getPlayerRegistry().isInArena(event.getPlayer())) {
            return;
        }
        //TODO: Treat this the same as onPlayerLeave if the player is in an arena. If the player doesn't change worlds,
        // it should be safe to immediately teleport the player to the arena's exit.
        //TODO: Because of this, make sure to remove the player from the arena before teleporting the player out
    }

}

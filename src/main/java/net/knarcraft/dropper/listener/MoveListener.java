package net.knarcraft.dropper.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        //TODO: Return if player is not in an arena
        //TODO: If the player is moving to a block of water, register the win, and teleport the player out
        //TODO: If the player is about to hit a non-water and non-air block (within a margin of about 1/16 of a block 
        // in the y-direction), treat that the same as @see{DamageListener#onPlayerDamage}
    }

}

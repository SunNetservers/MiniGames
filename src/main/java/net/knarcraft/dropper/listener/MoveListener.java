package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * A listener for players moving inside a dropper arena
 */
public class MoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!Dropper.getInstance().getPlayerRegistry().isInArena(event.getPlayer())) {
            return;
        }

        Block targetBlock = event.getTo().getBlock();

        // Hitting water is the trigger for winning
        if (targetBlock.getType() == Material.WATER) {
            //TODO: Register the win, and teleport the player out
            return;
        }

        Location targetLocation = targetBlock.getLocation();
        Block beneathPlayer = targetLocation.getWorld().getBlockAt(targetLocation.add(0, -0.1, 0));

        // If hitting something which is not air or water, it must be a solid block, and would end in a loss
        if (!targetBlock.getType().isAir() || (beneathPlayer.getType() != Material.WATER &&
                !beneathPlayer.getType().isAir())) {
            //TODO: This that the same as @see{DamageListener#onPlayerDamage}
        }
    }

}

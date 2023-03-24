package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaPlayerRegistry;
import net.knarcraft.dropper.arena.DropperArenaSession;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * A listener for players moving inside a dropper arena
 */
public class MoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Ignore if no actual movement is happening
        if (event.getFrom().equals(event.getTo())) {
            return;
        }

        Player player = event.getPlayer();
        DropperArenaPlayerRegistry playerRegistry = Dropper.getInstance().getPlayerRegistry();
        DropperArenaSession arenaSession = playerRegistry.getArenaSession(player.getUniqueId());
        if (arenaSession == null) {
            return;
        }

        // Prevent the player from flying upwards while in flight mode
        if (event.getFrom().getY() < event.getTo().getY()) {
            event.setCancelled(true);
            return;
        }

        // Only do block type checking if the block beneath the player changes
        if (event.getFrom().getBlock() != event.getTo().getBlock()) {
            Block targetBlock = event.getTo().getBlock();
            Material targetBlockType = targetBlock.getType();

            // Hitting water is the trigger for winning
            if (targetBlockType == Material.WATER) {
                arenaSession.triggerWin();
                return;
            }

            Location targetLocation = targetBlock.getLocation();
            Material beneathPlayerType = targetLocation.getWorld().getBlockAt(
                    targetLocation.add(0, -0.1, 0)).getType();

            // If hitting something which is not air or water, it must be a solid block, and would end in a loss
            if (!targetBlockType.isAir() || (beneathPlayerType != Material.WATER &&
                    !beneathPlayerType.isAir())) {
                arenaSession.triggerLoss();
                return;
            }
        }

        //Updates the player's velocity to the one set by the arena
        updatePlayerVelocity(arenaSession);
    }

    /**
     * Updates the velocity of the player in the given session
     *
     * @param session <p>The session to update the velocity for</p>
     */
    private void updatePlayerVelocity(DropperArenaSession session) {
        Player player = session.getPlayer();
        Vector playerVelocity = player.getVelocity();
        double arenaVelocity = session.getArena().getPlayerVelocity();
        Vector newVelocity = new Vector(playerVelocity.getX(), -arenaVelocity, playerVelocity.getZ());
        player.setVelocity(newVelocity);
    }

}

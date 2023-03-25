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

import java.util.HashSet;
import java.util.Set;

/**
 * A listener for players moving inside a dropper arena
 */
public class MoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Ignore if no actual movement is happening
        if (event.getFrom().equals(event.getTo()) || event.getTo() == null) {
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
            // Check if the player enters water
            Material winBlockType = arenaSession.getArena().getWinBlockType();
            // For water, only trigger when the player enters the water, but trigger earlier for everything else
            int depth = winBlockType == Material.WATER ? 0 : 1;
            for (Block block : getBlocksBeneathLocation(event.getTo(), depth)) {
                if (block.getType() == winBlockType) {
                    arenaSession.triggerWin();
                    return;
                }
            }

            // Check if the player is about to hit a non-air and non-liquid block
            for (Block block : getBlocksBeneathLocation(event.getTo(), 1)) {
                if (!block.getType().isAir() && block.getType() != Material.STRUCTURE_VOID &&
                        block.getType() != Material.WATER && block.getType() != Material.LAVA) {
                    arenaSession.triggerLoss();
                    return;
                }
            }
        }

        //Updates the player's velocity to the one set by the arena
        updatePlayerVelocity(arenaSession);
    }

    /**
     * Gets the blocks at the given location that will be affected by the player's hit-box
     *
     * @param location <p>The location to check</p>
     * @return <p>The blocks beneath the player</p>
     */
    private Set<Block> getBlocksBeneathLocation(Location location, double depth) {
        Set<Block> blocksBeneath = new HashSet<>();
        double halfPlayerWidth = 0.3;
        blocksBeneath.add(location.clone().subtract(halfPlayerWidth, depth, halfPlayerWidth).getBlock());
        blocksBeneath.add(location.clone().subtract(-halfPlayerWidth, depth, halfPlayerWidth).getBlock());
        blocksBeneath.add(location.clone().subtract(halfPlayerWidth, depth, -halfPlayerWidth).getBlock());
        blocksBeneath.add(location.clone().subtract(-halfPlayerWidth, depth, -halfPlayerWidth).getBlock());
        return blocksBeneath;
    }

    /**
     * Updates the velocity of the player in the given session
     *
     * @param session <p>The session to update the velocity for</p>
     */
    private void updatePlayerVelocity(DropperArenaSession session) {
        Player player = session.getPlayer();
        Vector playerVelocity = player.getVelocity();
        double arenaVelocity = session.getArena().getPlayerVerticalVelocity();
        Vector newVelocity = new Vector(playerVelocity.getX(), -arenaVelocity, playerVelocity.getZ());
        player.setVelocity(newVelocity);
    }

}

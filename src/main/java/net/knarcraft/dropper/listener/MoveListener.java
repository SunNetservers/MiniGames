package net.knarcraft.dropper.listener;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArenaPlayerRegistry;
import net.knarcraft.dropper.arena.DropperArenaSession;
import net.knarcraft.dropper.property.ArenaGameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
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
        if (event.getFrom().getBlock() != event.getTo().getBlock() &&
                checkForSpecialBlock(arenaSession, event.getTo())) {
            return;
        }

        //Updates the player's velocity to the one set by the arena
        updatePlayerVelocity(arenaSession);
    }

    /**
     * Check if the player in the session is triggering a block with a special significance
     *
     * <p>This basically looks for the win block, or whether the player is hitting a solid block.</p>
     *
     * @param arenaSession <p>The arena session to check for</p>
     * @param toLocation   <p>The location the player's session is about to hit</p>
     * @return <p>True if a special block has been hit</p>
     */
    private boolean checkForSpecialBlock(DropperArenaSession arenaSession, Location toLocation) {
        /* This decides how far inside a non-solid block the player must go before detection triggers. The closer to -1
           it is, the more accurate it will seem to the player, but the likelihood of not detecting the hit decreases */
        double liquidDepth = -0.8;
        /* This decides the distance the player must be from the block below before a hit triggers. If too low, the 
           likelihood of detecting the hit decreases, but the immersion increases. */
        double solidDepth = 0.2;

        // Check if the player enters water
        Material winBlockType = arenaSession.getArena().getWinBlockType();
        // For water, only trigger when the player enters the water, but trigger earlier for everything else
        double depth = !winBlockType.isSolid() ? liquidDepth : solidDepth;
        for (Block block : getBlocksBeneathLocation(toLocation, depth)) {
            if (block.getType() == winBlockType) {
                arenaSession.triggerWin();
                return true;
            }
        }

        // Check if the player is about to hit a non-air and non-liquid block
        for (Block block : getBlocksBeneathLocation(toLocation, solidDepth)) {
            if (!block.getType().isAir() && block.getType() != Material.STRUCTURE_VOID &&
                    block.getType() != Material.WATER && block.getType() != Material.LAVA) {
                arenaSession.triggerLoss();
                return true;
            }
        }

        return false;
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
    private void updatePlayerVelocity(@NotNull DropperArenaSession session) {
        Player player = session.getPlayer();
        Vector playerVelocity = player.getVelocity();
        double arenaVelocity = session.getArena().getPlayerVerticalVelocity();
        Vector newVelocity = new Vector(playerVelocity.getX(), -arenaVelocity, playerVelocity.getZ());
        player.setVelocity(newVelocity);

        // Toggle the direction of the player's flying, as necessary
        toggleFlyInversion(session);
    }

    /**
     * Toggles the player's flying direction inversion if playing on the random game-mode
     *
     * @param session <p>The session to possibly invert flying for</p>
     */
    private void toggleFlyInversion(@NotNull DropperArenaSession session) {
        if (session.getGameMode() != ArenaGameMode.RANDOM_INVERTED) {
            return;
        }
        Player player = session.getPlayer();
        float horizontalVelocity = session.getArena().getPlayerHorizontalVelocity();
        float secondsBetweenToggle = 7;
        int seconds = Calendar.getInstance().get(Calendar.SECOND);

        /*
         * A trick to make the inversion change after a customizable amount of seconds
         * If the quotient of dividing the current number of seconds with the set amount of seconds is even, invert.
         * So, if the number of seconds between toggles is 5, that would mean for the first 5 seconds, the flying would
         * be inverted. Once 5 seconds have passed, the quotient becomes 1, which is odd, so the flying is no longer
         * inverted. After 10 seconds, the quotient is 2, which is even, and inverts the flying.
         */
        boolean invert = Math.floor(seconds / secondsBetweenToggle) % 2 == 0;
        player.setFlySpeed(invert ? -horizontalVelocity : horizontalVelocity);
    }

}

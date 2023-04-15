package net.knarcraft.minigames.listener;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import net.knarcraft.minigames.arena.dropper.DropperArenaSession;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import net.knarcraft.minigames.config.DropperConfiguration;
import net.knarcraft.minigames.config.SharedConfiguration;
import org.bukkit.Location;
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

    private final DropperConfiguration configuration;

    /**
     * Instantiates a new move listener
     *
     * @param configuration <p>The configuration to use</p>
     */
    public MoveListener(DropperConfiguration configuration) {
        this.configuration = configuration;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Ignore if no actual movement is happening
        if (event.getFrom().equals(event.getTo()) || event.getTo() == null) {
            return;
        }

        ArenaSession session = MiniGames.getInstance().getSession(event.getPlayer().getUniqueId());
        if (session instanceof DropperArenaSession dropperSession) {
            doDropperArenaChecks(event, dropperSession);
        } else if (session instanceof ParkourArenaSession parkourSession) {
            doParkourArenaChecks(event, parkourSession);
        }
    }

    /**
     * Performs the necessary checks and tasks for the player's session
     *
     * @param event        <p>The move event triggered</p>
     * @param arenaSession <p>The dropper session of the player triggering the event</p>
     */
    private void doParkourArenaChecks(@NotNull PlayerMoveEvent event, ParkourArenaSession arenaSession) {
        // Ignore movement which won't cause the player's block to change
        if (event.getTo() == null || event.getFrom().getBlock() == event.getTo().getBlock()) {
            return;
        }

        // Only do block type checking if the block beneath the player changes
        if (checkForSpecialBlock(arenaSession, event.getTo())) {
            return;
        }

        // Check if the player reached one of the checkpoints for the arena
        for (Location checkpoint : arenaSession.getArena().getCheckpoints()) {
            if (checkpoint.getBlock().equals(event.getTo().getBlock()) &&
                    !checkpoint.equals(arenaSession.getRegisteredCheckpoint())) {
                arenaSession.registerCheckpoint(checkpoint.clone());
                event.getPlayer().sendMessage("Checkpoint reached!");
                return;
            }
        }
    }

    /**
     * Performs the necessary checks and tasks for the player's session
     *
     * @param event        <p>The move event triggered</p>
     * @param arenaSession <p>The dropper session of the player triggering the event</p>
     */
    private void doDropperArenaChecks(@NotNull PlayerMoveEvent event, @NotNull DropperArenaSession arenaSession) {
        if (event.getTo() == null) {
            return;
        }
        // Prevent the player from flying upwards while in flight mode
        if (event.getFrom().getY() < event.getTo().getY() ||
                (configuration.blockSneaking() && event.getPlayer().isSneaking()) ||
                (configuration.blockSprinting() && event.getPlayer().isSprinting())) {
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
    private boolean checkForSpecialBlock(ArenaSession arenaSession, Location toLocation) {
        SharedConfiguration sharedConfiguration = MiniGames.getInstance().getSharedConfiguration();
        double liquidDepth = sharedConfiguration.getLiquidHitBoxDepth();
        double solidDepth = sharedConfiguration.getSolidHitBoxDistance();

        Arena arena = arenaSession.getArena();

        // For water, only trigger when the player enters the water, but trigger earlier for everything else
        double depth = arena.winLocationIsSolid() ? solidDepth : liquidDepth;
        for (Block block : getBlocksBeneathLocation(toLocation, depth)) {
            if (arena.willCauseWin(block)) {
                arenaSession.triggerWin();
                return true;
            }
        }

        // Check if the player is about to hit a non-air and non-liquid block
        for (Block block : getBlocksBeneathLocation(toLocation, solidDepth)) {
            if (!block.getType().isAir() && arena.willCauseLoss(block)) {
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
        // Override the vertical velocity
        Player player = session.getPlayer();
        Vector playerVelocity = player.getVelocity();
        double arenaVelocity = session.getArena().getPlayerVerticalVelocity();
        Vector newVelocity = new Vector(playerVelocity.getX() * 5, -arenaVelocity, playerVelocity.getZ() * 5);
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
        if (session.getGameMode() != DropperArenaGameMode.RANDOM_INVERTED) {
            return;
        }
        Player player = session.getPlayer();
        float horizontalVelocity = session.getArena().getPlayerHorizontalVelocity();
        float secondsBetweenToggle = configuration.getRandomlyInvertedTimer();
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

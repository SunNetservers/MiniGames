package net.knarcraft.minigames.listener;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import net.knarcraft.minigames.arena.dropper.DropperArenaSession;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGameMode;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import net.knarcraft.minigames.config.DropperConfiguration;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.config.ParkourConfiguration;
import net.knarcraft.minigames.config.SharedConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A listener for players moving inside a dropper arena
 */
public class MoveListener implements Listener {

    private static final BoundingBox fullBlockBox = new BoundingBox(0, 0, 0, 1, 1, 1);

    private final DropperConfiguration dropperConfiguration;
    private final ParkourConfiguration parkourConfiguration;

    /**
     * Instantiates a new move listener
     *
     * @param dropperConfiguration <p>The dropper configuration to use</p>
     * @param parkourConfiguration <p>The parkour configuration to use</p>
     */
    public MoveListener(DropperConfiguration dropperConfiguration, ParkourConfiguration parkourConfiguration) {
        this.dropperConfiguration = dropperConfiguration;
        this.parkourConfiguration = parkourConfiguration;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Ignore if no actual movement is happening
        if (event.getTo() == null) {
            return;
        }

        ArenaSession session = MiniGames.getInstance().getSession(event.getPlayer().getUniqueId());
        if (session instanceof DropperArenaSession dropperSession) {
            if (event.getFrom().equals(event.getTo())) {
                return;
            }
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
        if (event.getTo() == null) {
            return;
        }

        // Only do block type checking if the block beneath the player changes
        if (checkForSpecialBlock(arenaSession, event.getTo())) {
            return;
        }

        // Skip checkpoint registration if playing on hardcore
        if (arenaSession.getGameMode() == ParkourArenaGameMode.HARDCORE) {
            return;
        }

        // Check if the player reached one of the checkpoints for the arena
        updateCheckpoint(arenaSession, event.getTo().getBlock(), event.getPlayer());
    }

    /**
     * Updates the checkpoint of a player if reached
     *
     * @param arenaSession <p>The session of the player</p>
     * @param targetBlock  <p>The block the player is moving to</p>
     * @param player       <p>The player moving</p>
     */
    private void updateCheckpoint(ParkourArenaSession arenaSession, Block targetBlock, Player player) {
        ParkourArena arena = arenaSession.getArena();
        List<Location> checkpoints = arena.getCheckpoints();
        for (Location checkpoint : checkpoints) {
            Location previousCheckpoint = arenaSession.getRegisteredCheckpoint();

            // Skip if checkpoint has not been reached
            if (!checkpoint.getBlock().equals(targetBlock)) {
                continue;
            }

            // If the checkpoint is the same as the previously reached one, abort
            if (previousCheckpoint != null && checkpoint.getBlock().equals(previousCheckpoint.getBlock())) {
                return;
            }

            // If not the correct checkpoint according to the enforced order, abort
            if (parkourConfiguration.enforceCheckpointOrder()) {
                int checkpointIndex = checkpoints.indexOf(checkpoint);
                int previousIndex = previousCheckpoint == null ? -1 : checkpoints.indexOf(previousCheckpoint);
                if (checkpointIndex - previousIndex != 1) {
                    return;
                }
            }

            // Register the checkpoint
            arenaSession.registerCheckpoint(checkpoint.clone());
            MiniGames.getInstance().getStringFormatter().displaySuccessMessage(player,
                    MiniGameMessage.SUCCESS_CHECKPOINT_REACHED);
            return;
        }
    }

    /**
     * Performs the necessary checks and tasks for the player's session
     *
     * @param event        <p>The move event triggered</p>
     * @param arenaSession <p>The dropper session of the player triggering the event</p>
     */
    private void doDropperArenaChecks(@NotNull PlayerMoveEvent event, @NotNull DropperArenaSession arenaSession) {
        // If the player has yet to move in the arena, allow them to look around
        boolean startedMoving = arenaSession.getStartedMoving();
        if (event.getTo() == null ||
                (!startedMoving && isSameLocation(event.getFrom(), event.getTo()))) {
            return;
        }

        // Marks the player as started moving if necessary, so they can no longer hang in the air
        if (!startedMoving) {
            arenaSession.setStartedMoving();
        }

        // Prevent the player from flying upwards while in flight mode
        if (event.getFrom().getY() < event.getTo().getY() ||
                (dropperConfiguration.blockSneaking() && event.getPlayer().isSneaking()) ||
                (dropperConfiguration.blockSprinting() && event.getPlayer().isSprinting())) {
            event.setCancelled(true);
            // Force movement downwards once the player lets go
            Bukkit.getScheduler().scheduleSyncDelayedTask(MiniGames.getInstance(),
                    () -> updatePlayerVelocity(arenaSession), 1);
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
     * Checks if two locations are the same, excluding rotation
     *
     * @param location1 <p>The first location to check</p>
     * @param location2 <p>The second location to check</p>
     * @return <p>True if the locations are the same, excluding rotation</p>
     */
    private boolean isSameLocation(Location location1, Location location2) {
        return location1.getX() == location2.getX() && location1.getY() == location2.getY() &&
                location1.getZ() == location2.getZ();
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
    private boolean checkForSpecialBlock(@NotNull ArenaSession arenaSession, @NotNull Location toLocation) {
        SharedConfiguration sharedConfiguration = MiniGames.getInstance().getSharedConfiguration();
        double solidDepth = sharedConfiguration.getSolidHitBoxDistance();
        double liquidDepth = sharedConfiguration.getLiquidHitBoxDepth();
        Arena arena = arenaSession.getArena();

        // For water, only trigger when the player enters the water, but trigger earlier for everything else
        Set<Block> potentialWinTriggerBlocks;
        if (arena.winLocationIsSolid()) {
            potentialWinTriggerBlocks = getBlocksBeneathLocation(toLocation, solidDepth);
        } else {
            potentialWinTriggerBlocks = getBlocksBeneathLocation(toLocation, liquidDepth);
        }
        for (Block block : potentialWinTriggerBlocks) {
            if (arena.willCauseWin(block)) {
                arenaSession.triggerWin();
                return true;
            }
        }

        if (arena instanceof DropperArena) {
            // Check if the player is about to hit a non-air and non-liquid block
            for (Block block : getBlocksBeneathLocation(toLocation, solidDepth)) {
                if (!block.getType().isAir() && !block.isLiquid() && arena.willCauseLoss(block)) {
                    arenaSession.triggerLoss();
                    return true;
                }
            }

            // Check if the player has entered a liquid that causes a loss
            for (Block block : getBlocksBeneathLocation(toLocation, liquidDepth)) {
                if (block.isLiquid() && arena.willCauseLoss(block)) {
                    arenaSession.triggerLoss();
                    return true;
                }
            }
        } else if (arena instanceof ParkourArena) {
            return checkParkourDeathBlock((ParkourArenaSession) arenaSession, toLocation);
        }

        return false;
    }

    /**
     * Checks if a player is moving onto a block part of the parkour death plane
     *
     * @param arenaSession <p>The player's arena session</p>
     * @param toLocation   <p>The location the player is moving to</p>
     * @return <p>True if the player hit a death block</p>
     */
    private boolean checkParkourDeathBlock(@NotNull ParkourArenaSession arenaSession,
                                           @NotNull Location toLocation) {
        // A simple check, only for kill blocks
        if (isOnKillBlock(arenaSession, toLocation)) {
            return true;
        }

        // As the check for obstacle blocks is extensive, it's skipped if possible
        Set<Material> obstacleBlocks = arenaSession.getArena().getObstacleBlocks();
        if (obstacleBlocks.isEmpty()) {
            return false;
        }

        // Create a hit-box approximate to the player's real hit-box
        double playerHeight = 1.8;
        Player player = Bukkit.getPlayer(arenaSession.getEntryState().getPlayerId());
        if (player != null && player.isSneaking()) {
            playerHeight = 1.5;
        }
        BoundingBox playerBox = new BoundingBox(-0.05, -0.05, -0.05,
                0.6 + 0.05, playerHeight + 0.05, 0.6 + 0.05).shift(
                toLocation).shift(-0.3, -0.05, -0.3);
        BoundingBox playerPassableBox = new BoundingBox(0.2, 0.5, 0.2,
                0.4, playerHeight - 0.5, 0.4).shift(
                toLocation).shift(-0.3, 0, -0.3);
        Set<Block> possiblyHitBlocks = new HashSet<>();
        possiblyHitBlocks.addAll(getBlocksBeneathLocation(toLocation, 0, 0.01));
        possiblyHitBlocks.addAll(getBlocksBeneathLocation(toLocation, 1, 0.01));
        possiblyHitBlocks.addAll(getBlocksBeneathLocation(toLocation, -1, 0.01));
        possiblyHitBlocks.addAll(getBlocksBeneathLocation(toLocation, -2, 0.01));

        for (Block block : possiblyHitBlocks) {
            if (!obstacleBlocks.contains(block.getType())) {
                continue;
            }

            // For liquids, or anything without a proper collision shape, trigger collision if the player is partly 
            // inside when treated as a full block
            if (block.isLiquid() || block.getCollisionShape().getBoundingBoxes().isEmpty()) {
                if (playerPassableBox.overlaps(fullBlockBox.clone().shift(block.getLocation()))) {
                    arenaSession.triggerLoss();
                    return true;
                }
            }

            // Check whether the player's actual hit-box is intersecting with a block
            for (BoundingBox boundingBox : block.getCollisionShape().getBoundingBoxes()) {
                // A collision shape's bounding box is relative to 0,0 and therefore must be adjusted to the block's 
                // location. Then overlap is checked by the player's collision box and the shifted bounding box.
                if (playerBox.overlaps(boundingBox.clone().shift(block.getLocation()))) {
                    arenaSession.triggerLoss();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * As simple check for whether a player is moving on top of a kill block
     *
     * @param arenaSession <p>The arena session the player is in</p>
     * @param toLocation   <p>The location the player is moving to</p>
     * @return <p>True if the player is on a kill block, and a loss has been triggered</p>
     */
    private boolean isOnKillBlock(ParkourArenaSession arenaSession, Location toLocation) {
        // If the player is standing on a non-full block, event.getTo will give the correct block, but if not, the 
        // block below has to be checked instead.
        Set<Block> blocksBelow = getBlocksBeneathLocation(toLocation, 0);
        Set<Material> killPlaneBlocks = arenaSession.getArena().getKillPlaneBlocks();
        for (Block block : blocksBelow) {
            if (block.getType().isAir()) {
                block = block.getLocation().clone().subtract(0, 0.2, 0).getBlock();
                // Only trigger hit detection for passable blocks if the player is in the block
                if (block.isPassable()) {
                    continue;
                }
            }
            if (killPlaneBlocks.contains(block.getType())) {
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
        return getBlocksBeneathLocation(location, depth, 0);
    }

    /**
     * Gets the blocks at the given location that will be affected by the player's hit-box
     *
     * @param location   <p>The location to check</p>
     * @param extraRange <p>Extra range of the square used for finding blocks</p>
     * @return <p>The blocks beneath the player</p>
     */
    private Set<Block> getBlocksBeneathLocation(Location location, double depth, double extraRange) {
        Set<Block> blocksBeneath = new HashSet<>();
        double halfPlayerWidth = 0.3 + extraRange;
        blocksBeneath.add(location.clone().subtract(halfPlayerWidth, depth, halfPlayerWidth).getBlock());
        blocksBeneath.add(location.clone().subtract(-halfPlayerWidth, depth, halfPlayerWidth).getBlock());
        blocksBeneath.add(location.clone().subtract(halfPlayerWidth, depth, -halfPlayerWidth).getBlock());
        blocksBeneath.add(location.clone().subtract(-halfPlayerWidth, depth, -halfPlayerWidth).getBlock());
        // Once a certain size is reached, if the player is in the centre of a block, 9 must be accounted for
        if (halfPlayerWidth > 0.5) {
            blocksBeneath.add(location.getBlock());
            blocksBeneath.add(location.clone().subtract(halfPlayerWidth, depth, 0).getBlock());
            blocksBeneath.add(location.clone().subtract(-halfPlayerWidth, depth, 0).getBlock());
            blocksBeneath.add(location.clone().subtract(0, depth, -halfPlayerWidth).getBlock());
            blocksBeneath.add(location.clone().subtract(0, depth, halfPlayerWidth).getBlock());
        }
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
        float secondsBetweenToggle = dropperConfiguration.getRandomlyInvertedTimer();
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

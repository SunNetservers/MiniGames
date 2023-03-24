package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.property.ArenaGameMode;
import net.knarcraft.dropper.property.RecordResult;
import net.knarcraft.dropper.util.PlayerTeleporter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * A representation of a player's current session in a dropper arena
 */
public class DropperArenaSession {

    private final @NotNull DropperArena arena;
    private final @NotNull Player player;
    private final @NotNull ArenaGameMode gameMode;
    private final @NotNull Location entryLocation;
    private int deaths;
    private final long startTime;
    private final float playersOriginalFlySpeed;

    /**
     * Instantiates a new dropper arena session
     *
     * @param dropperArena <p>The arena that's being played in</p>
     * @param player       <p>The player playing the arena</p>
     * @param gameMode     <p>The game-mode</p>
     */
    public DropperArenaSession(@NotNull DropperArena dropperArena, @NotNull Player player,
                               @NotNull ArenaGameMode gameMode) {
        this.arena = dropperArena;
        this.player = player;
        this.gameMode = gameMode;
        this.deaths = 0;
        this.startTime = System.currentTimeMillis();
        this.entryLocation = player.getLocation();

        // Make the player fly to improve mobility in the air
        player.setAllowFlight(true);
        player.setFlying(true);
        this.playersOriginalFlySpeed = player.getFlySpeed();
        player.setFlySpeed((float) this.arena.getPlayerVelocity());
    }

    /**
     * Triggers a win for the player playing in this session
     */
    public void triggerWin() {
        // Stop this session
        stopSession();

        // Check for, and display, records
        registerRecord();

        //TODO: Give reward?

        // Register and announce any cleared stages
        Integer arenaStage = this.arena.getStage();
        if (arenaStage != null) {
            boolean clearedNewStage = Dropper.getInstance().getArenaHandler().registerStageCleared(this.player, arenaStage);
            if (clearedNewStage) {
                this.player.sendMessage("You cleared stage " + arenaStage + "!");
            }
        }

        this.player.sendMessage("You won!");

        // Teleport the player out of the arena
        teleportToExit();
    }

    /**
     * Teleports the playing player out of the arena
     */
    private void teleportToExit() {
        // Teleport the player out of the arena
        Location exitLocation;
        if (this.arena.getExitLocation() != null) {
            exitLocation = this.arena.getExitLocation();
        } else {
            exitLocation = this.entryLocation;
        }
        PlayerTeleporter.teleportPlayer(this.player, exitLocation, true);
    }

    /**
     * Removes this session from current sessions
     */
    private void removeSession() {
        // Remove this session for game sessions to stop listeners from fiddling more with the player
        boolean removedSession = Dropper.getInstance().getPlayerRegistry().removePlayer(player.getUniqueId());
        if (!removedSession) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to remove dropper arena session for " +
                    player.getName() + ". This will have unintended consequences.");
        }
    }

    /**
     * Registers the player's record if necessary, and prints record information to the player
     */
    private void registerRecord() {
        DropperArenaRecordsRegistry recordsRegistry = this.arena.getRecordsRegistry();
        RecordResult recordResult = switch (this.gameMode) {
            case LEAST_TIME -> recordsRegistry.registerTimeRecord(this.player,
                    System.currentTimeMillis() - this.startTime);
            case LEAST_DEATHS -> recordsRegistry.registerDeathRecord(this.player, this.deaths);
            case DEFAULT -> RecordResult.NONE;
        };
        switch (recordResult) {
            case WORLD_RECORD -> this.player.sendMessage("You just set a new record for this arena!");
            case PERSONAL_BEST -> this.player.sendMessage("You just got a new personal record!");
        }
    }

    /**
     * Triggers a loss for the player playing in this session
     */
    public void triggerLoss() {
        // Add to the death count if playing the least-deaths game-mode
        if (this.gameMode == ArenaGameMode.LEAST_DEATHS) {
            this.deaths++;
        }
        //Teleport the player back to the top
        PlayerTeleporter.teleportPlayer(this.player, this.arena.getSpawnLocation(), true);
    }

    /**
     * Triggers a quit for the player playing in this session
     */
    public void triggerQuit() {
        // Stop this session
        stopSession();
        // Teleport the player out of the arena
        teleportToExit();

        player.sendMessage("You quit the arena!");
    }

    /**
     * Stops this session, and disables flight mode
     */
    private void stopSession() {
        // Remove this session from game sessions to stop listeners from fiddling more with the player
        removeSession();

        // Remove flight mode
        this.player.setFlySpeed(this.playersOriginalFlySpeed);
        this.player.setFlying(false);
        this.player.setAllowFlight(false);
    }

    /**
     * Gets the arena this session is being played in
     *
     * @return <p>The session's arena</p>
     */
    public @NotNull DropperArena getArena() {
        return this.arena;
    }

    /**
     * Gets the player playing in this session
     *
     * @return <p>This session's player</p>
     */
    public @NotNull Player getPlayer() {
        return this.player;
    }

}

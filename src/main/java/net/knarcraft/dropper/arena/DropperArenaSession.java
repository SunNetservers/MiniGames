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
        // Prevent Spigot interference when traveling at high velocities
        player.setAllowFlight(true);
    }

    /**
     * Triggers a win for the player playing in this session
     */
    public void triggerWin() {
        // Remove this session from game sessions to stop listeners from fiddling more with the player
        removeSession();

        // No longer allow the player to avoid fly checks
        player.setAllowFlight(false);

        // Check for, and display, records
        registerRecord();

        //TODO: Give reward?

        // Register and announce any cleared stages
        Integer arenaStage = arena.getStage();
        if (arenaStage != null) {
            boolean clearedNewStage = Dropper.getInstance().getArenaHandler().registerStageCleared(player, arenaStage);
            if (clearedNewStage) {
                player.sendMessage("You cleared stage " + arenaStage + "!");
            }
        }

        // Teleport the player out of the arena
        teleportToExit();
    }

    /**
     * Teleports the playing player out of the arena
     */
    private void teleportToExit() {
        // Teleport the player out of the arena
        Location exitLocation;
        if (arena.getExitLocation() != null) {
            exitLocation = arena.getExitLocation();
        } else {
            exitLocation = entryLocation;
        }
        PlayerTeleporter.teleportPlayer(player, exitLocation, true);
    }

    /**
     * Removes this session from current sessions
     */
    private void removeSession() {
        // Remove this session for game sessions to stop listeners from fiddling more with the player
        boolean removedSession = Dropper.getInstance().getPlayerRegistry().removePlayer(player);
        if (!removedSession) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to remove dropper arena session for " +
                    player.getName() + ". This will have unintended consequences.");
        }
    }

    /**
     * Registers the player's record if necessary, and prints record information to the player
     */
    private void registerRecord() {
        DropperArenaRecordsRegistry recordsRegistry = arena.getRecordsRegistry();
        RecordResult recordResult = switch (gameMode) {
            case LEAST_TIME -> recordsRegistry.registerTimeRecord(player,
                    System.currentTimeMillis() - startTime);
            case LEAST_DEATHS -> recordsRegistry.registerDeathRecord(player, deaths);
            case DEFAULT -> RecordResult.NONE;
        };
        switch (recordResult) {
            case WORLD_RECORD -> player.sendMessage("You just set a new record for this arena!");
            case PERSONAL_BEST -> player.sendMessage("You just got a new personal record!");
        }
    }

    /**
     * Triggers a loss for the player playing in this session
     */
    public void triggerLoss() {
        // Add to the death count if playing the least-deaths game-mode
        if (gameMode == ArenaGameMode.LEAST_DEATHS) {
            deaths++;
        }
        //Teleport the player back to the top
        PlayerTeleporter.teleportPlayer(player, arena.getSpawnLocation(), true);
    }

    /**
     * Triggers a quit for the player playing in this session
     */
    public void triggerQuit() {
        // Remove this session from game sessions to stop listeners from fiddling more with the player
        removeSession();
        // No longer allow the player to avoid fly checks
        player.setAllowFlight(false);
        // Teleport the player out of the arena
        teleportToExit();
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

package net.knarcraft.dropper.arena.dropper;

import net.knarcraft.dropper.MiniGames;
import net.knarcraft.dropper.config.DropperConfiguration;
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
    private final @NotNull DropperArenaGameMode gameMode;
    private int deaths;
    private final long startTime;
    private final DropperPlayerEntryState entryState;

    /**
     * Instantiates a new dropper arena session
     *
     * @param dropperArena <p>The arena that's being played in</p>
     * @param player       <p>The player playing the arena</p>
     * @param gameMode     <p>The game-mode</p>
     */
    public DropperArenaSession(@NotNull DropperArena dropperArena, @NotNull Player player,
                               @NotNull DropperArenaGameMode gameMode) {
        this.arena = dropperArena;
        this.player = player;
        this.gameMode = gameMode;
        this.deaths = 0;
        this.startTime = System.currentTimeMillis();

        DropperConfiguration configuration = MiniGames.getInstance().getDropperConfiguration();
        boolean makeInvisible = configuration.makePlayersInvisible();
        boolean disableCollision = configuration.disableHitCollision();
        this.entryState = new DropperPlayerEntryState(player, gameMode, makeInvisible, disableCollision);
        // Make the player fly to improve mobility in the air
        this.entryState.setArenaState(this.arena.getPlayerHorizontalVelocity());
    }

    /**
     * Gets the game-mode the player is playing in this session
     *
     * @return <p>The game-mode for this session</p>
     */
    public @NotNull DropperArenaGameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Gets the state of the player when they joined the session
     *
     * @return <p>The player's entry state</p>
     */
    public @NotNull DropperPlayerEntryState getEntryState() {
        return this.entryState;
    }

    /**
     * Triggers a win for the player playing in this session
     */
    public void triggerWin() {
        // Stop this session
        stopSession();

        // Check for, and display, records
        MiniGames miniGames = MiniGames.getInstance();
        boolean ignore = miniGames.getDropperConfiguration().ignoreRecordsUntilGroupBeatenOnce();
        DropperArenaGroup group = miniGames.getDropperArenaHandler().getGroup(this.arena.getArenaId());
        if (!ignore || group == null || group.hasBeatenAll(this.gameMode, this.player)) {
            registerRecord();
        }

        // Mark the arena as cleared
        if (this.arena.getData().setCompleted(this.gameMode, this.player)) {
            this.player.sendMessage("You cleared the arena!");
        }
        this.player.sendMessage("You won!");

        // Teleport the player out of the arena
        teleportToExit(false);
    }

    /**
     * Teleports the playing player out of the arena
     */
    private void teleportToExit(boolean immediately) {
        // Teleport the player out of the arena
        Location exitLocation;
        if (this.arena.getExitLocation() != null) {
            exitLocation = this.arena.getExitLocation();
        } else {
            exitLocation = this.entryState.getEntryLocation();
        }
        PlayerTeleporter.teleportPlayer(this.player, exitLocation, true, immediately);
    }

    /**
     * Removes this session from current sessions
     */
    private void removeSession() {
        // Remove this session for game sessions to stop listeners from fiddling more with the player
        boolean removedSession = MiniGames.getInstance().getDropperArenaPlayerRegistry().removePlayer(player.getUniqueId());
        if (!removedSession) {
            MiniGames.log(Level.SEVERE, "Unable to remove dropper arena session for " + player.getName() + ". " +
                    "This will have unintended consequences.");
        }
    }

    /**
     * Registers the player's record if necessary, and prints record information to the player
     */
    private void registerRecord() {
        DropperArenaRecordsRegistry recordsRegistry = this.arena.getData().recordRegistries().get(this.gameMode);
        long timeElapsed = System.currentTimeMillis() - this.startTime;
        announceRecord(recordsRegistry.registerTimeRecord(this.player.getUniqueId(), timeElapsed), "time");
        announceRecord(recordsRegistry.registerDeathRecord(this.player.getUniqueId(), this.deaths), "least deaths");
    }

    /**
     * Announces a record set by this player
     *
     * @param recordResult <p>The result of the record</p>
     * @param type         <p>The type of record set (time or deaths)</p>
     */
    private void announceRecord(@NotNull RecordResult recordResult, @NotNull String type) {
        if (recordResult == RecordResult.NONE) {
            return;
        }

        // Gets a string representation of the played game-mode
        String gameModeString = switch (this.gameMode) {
            case DEFAULT -> "default";
            case INVERTED -> "inverted";
            case RANDOM_INVERTED -> "random";
        };

        String recordString = "You just set a %s on the %s game-mode!";
        recordString = switch (recordResult) {
            case WORLD_RECORD -> String.format(recordString, "new %s record", gameModeString);
            case PERSONAL_BEST -> String.format(recordString, "personal %s record", gameModeString);
            default -> throw new IllegalStateException("Unexpected value: " + recordResult);
        };
        player.sendMessage(String.format(recordString, type));
    }

    /**
     * Triggers a loss for the player playing in this session
     */
    public void triggerLoss() {
        this.deaths++;
        //Teleport the player back to the top
        PlayerTeleporter.teleportPlayer(this.player, this.arena.getSpawnLocation(), true, false);
        this.entryState.setArenaState(this.arena.getPlayerHorizontalVelocity());
    }

    /**
     * Triggers a quit for the player playing in this session
     */
    public void triggerQuit(boolean immediately) {
        // Stop this session
        stopSession();
        // Teleport the player out of the arena
        teleportToExit(immediately);

        player.sendMessage("You quit the arena!");
    }

    /**
     * Stops this session, and disables flight mode
     */
    private void stopSession() {
        // Remove this session from game sessions to stop listeners from fiddling more with the player
        removeSession();

        // Remove flight mode
        entryState.restore();
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

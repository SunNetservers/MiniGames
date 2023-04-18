package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.PlayerEntryState;
import net.knarcraft.minigames.config.Message;
import net.knarcraft.minigames.config.ParkourConfiguration;
import net.knarcraft.minigames.property.RecordResult;
import net.knarcraft.minigames.util.PlayerTeleporter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * A representation of a player's current session in a parkour arena
 */
public class ParkourArenaSession implements ArenaSession {

    private final @NotNull ParkourArena arena;
    private final @NotNull Player player;
    private final @NotNull ParkourArenaGameMode gameMode;
    private int deaths;
    private final long startTime;
    private final PlayerEntryState entryState;
    private Location reachedCheckpoint = null;

    /**
     * Instantiates a new parkour arena session
     *
     * @param parkourArena <p>The arena that's being played in</p>
     * @param player       <p>The player playing the arena</p>
     * @param gameMode     <p>The game-mode</p>
     */
    public ParkourArenaSession(@NotNull ParkourArena parkourArena, @NotNull Player player,
                               @NotNull ParkourArenaGameMode gameMode) {
        this.arena = parkourArena;
        this.player = player;
        this.gameMode = gameMode;
        this.deaths = 0;
        this.startTime = System.currentTimeMillis();

        ParkourConfiguration configuration = MiniGames.getInstance().getParkourConfiguration();
        boolean makeInvisible = configuration.makePlayersInvisible();
        this.entryState = new ParkourPlayerEntryState(player, makeInvisible);
        // Make the player fly to improve mobility in the air
        this.entryState.setArenaState();
    }

    @Override
    public @NotNull ArenaGameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Gets the state of the player when they joined the session
     *
     * @return <p>The player's entry state</p>
     */
    public @NotNull PlayerEntryState getEntryState() {
        return this.entryState;
    }

    /**
     * Registers the checkpoint this session's player has reached
     *
     * @param location <p>The location of the checkpoint</p>
     */
    public void registerCheckpoint(@NotNull Location location) {
        this.reachedCheckpoint = location;
    }

    /**
     * Gets the checkpoint currently registered as the player's spawn location
     *
     * @return <p>The registered checkpoint, or null if not set</p>
     */
    public @Nullable Location getRegisteredCheckpoint() {
        return this.reachedCheckpoint;
    }

    /**
     * Triggers a win for the player playing in this session
     */
    public void triggerWin() {
        // Stop this session
        stopSession();

        // Check for, and display, records
        MiniGames miniGames = MiniGames.getInstance();
        boolean ignore = miniGames.getParkourConfiguration().ignoreRecordsUntilGroupBeatenOnce();
        ParkourArenaGroup group = miniGames.getParkourArenaHandler().getGroup(this.arena.getArenaId());
        if (!ignore || group == null || group.hasBeatenAll(this.gameMode, this.player)) {
            registerRecord();
        }

        // Mark the arena as cleared
        if (this.arena.getData().setCompleted(this.gameMode, this.player)) {
            this.player.sendMessage(Message.SUCCESS_ARENA_FIRST_CLEAR.getMessage());
        }
        this.player.sendMessage(Message.SUCCESS_ARENA_WIN.getMessage());

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
        boolean removedSession = MiniGames.getInstance().getParkourArenaPlayerRegistry().removePlayer(player.getUniqueId());
        if (!removedSession) {
            MiniGames.log(Level.SEVERE, "Unable to remove parkour arena session for " + player.getName() + ". " +
                    "This will have unintended consequences.");
        }
    }

    /**
     * Registers the player's record if necessary, and prints record information to the player
     */
    private void registerRecord() {
        ArenaRecordsRegistry recordsRegistry = this.arena.getData().getRecordRegistries().get(this.gameMode);
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
        Location spawnLocation = this.reachedCheckpoint != null ? this.reachedCheckpoint : this.arena.getSpawnLocation();
        PlayerTeleporter.teleportPlayer(this.player, spawnLocation, true, false);
        this.entryState.setArenaState();
    }

    /**
     * Triggers a quit for the player playing in this session
     */
    public void triggerQuit(boolean immediately) {
        // Stop this session
        stopSession();
        // Teleport the player out of the arena
        teleportToExit(immediately);

        player.sendMessage(Message.SUCCESS_ARENA_QUIT.getMessage());
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
    public @NotNull ParkourArena getArena() {
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
